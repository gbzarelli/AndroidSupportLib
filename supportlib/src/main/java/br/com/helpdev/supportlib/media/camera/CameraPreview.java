package br.com.helpdev.supportlib.media.camera;

import android.app.Activity;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.support.annotation.IntDef;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.ByteArrayOutputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Felipe Barata on 18/08/16.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    public static final int CAMERA_FACING_BACK = 0;
    public static final int CAMERA_FACING_FRONT = 1;
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CAMERA_FACING_BACK, CAMERA_FACING_FRONT})
    @interface CameraInfo {
    }

    public static final int DELAY_PREVIEW_PROCESS_DEFAULT = 1000;

    private SurfaceHolder surfaceHolder;
    protected Camera camera;
    @CameraInfo
    private int cameraToUse;
    protected Activity activity;
    private int orientation;
    protected static final int previewSizeWidth = 640;
    protected static final int previewSizeHeight = 480;

    private volatile boolean threadRunning = false;
    private volatile byte[] imageYuv;
    private final int delayPreviewProcess;

    public CameraPreview(Activity activity, @CameraInfo int cameraToUse) {
        this(activity, cameraToUse, DELAY_PREVIEW_PROCESS_DEFAULT);
    }

    public CameraPreview(Activity activity, @CameraInfo int cameraToUse, int delayPreviewProcess) {
        super(activity);
        this.delayPreviewProcess = delayPreviewProcess;
        this.cameraToUse = cameraToUse;
        this.activity = activity;
        // Implemente um SurfaceHolder.Callback para ser notificado quando a superficie e criada e destruida
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        // quando a superficie e criada, podemos definir para a camera desenhar imagens no surfaceholder
        try {
            if (camera == null) {
                this.camera = Camera.open(cameraToUse);
                Camera.Parameters p = this.camera.getParameters();
                p.setPreviewSize(previewSizeWidth, previewSizeHeight);
                p.setPictureSize(previewSizeWidth, previewSizeHeight);
                this.camera.setParameters(p);
            }
        } catch (Exception e) {
            Log.e(getClass().getName(), "surfaceCreated", e);
        }
    }

    /**
     *
     */
    @Override
    public void run() {
        threadRunning = true;
        while (threadRunning) {
            try {
                while (imageYuv == null) {
                    Thread.sleep(1_000);
                }
                if (!threadRunning) return;

                postProcessImage(preProcessImage(imageYuv));
            } catch (Throwable t) {
                t.printStackTrace();
            }
            try {
                Thread.sleep(delayPreviewProcess);
            } catch (Throwable t) {
            } finally {
                imageYuv = null;
            }
        }
    }

    protected byte[] preProcessImage(byte[] imageYuv) {
        Camera.Parameters parameters = camera.getParameters();
        Camera.Size size = parameters.getPreviewSize();


        if (getOrientation() == Surface.ROTATION_270) {
            imageYuv = CameraUtils.rotateYUV420Degree180(imageYuv, size.width, size.height);
        } else if (getOrientation() == Surface.ROTATION_0) {
            imageYuv = CameraUtils.rotateYUV420Degree270(imageYuv, size.width, size.height);
        } else if (getOrientation() == Surface.ROTATION_180) {
            imageYuv = CameraUtils.rotateYUV420Degree90(imageYuv, size.width, size.height);
        }

        YuvImage image = new YuvImage(imageYuv, parameters.getPreviewFormat(),
                size.width, size.height, null);

        ByteArrayOutputStream bufferImagePreviewJpg = new ByteArrayOutputStream();
        image.compressToJpeg(new Rect(0, 0, image.getWidth(), image.getHeight()), 70, bufferImagePreviewJpg);
        return bufferImagePreviewJpg.toByteArray();
    }

    public void postProcessImage(byte[] image) {

    }

    @Override
    public void surfaceChanged(final SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        // antes de mudar a orientacao da aplicaçao, deve-se stop a previa, rotate e entao comecar novamente
        if (this.surfaceHolder == null || camera == null) {//checa se a superficie esta pronta para receber dados da camera
            return;
        }
        //agora, recrie a previa da camera
        try {
            setCameraDisplayOrientation();
            camera.setPreviewDisplay(this.surfaceHolder);
            onSurfaceChangedStartPreview();
        } catch (Exception e) {
            Log.e("ERROR", "Camera error on surfaceChanged " + e.getMessage(), e);
        }
    }


    protected void onSurfaceChangedStartPreview() {
        startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        stop();
    }

    public void stop() {
        if (camera != null) {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
        }
        surfaceHolder = null;
        camera = null;
        threadRunning = false;
    }

    public final void takePicture(Camera.ShutterCallback shutter, Camera.PictureCallback raw, Camera.PictureCallback jpeg) {
        if (camera != null) {
            camera.takePicture(shutter, raw, jpeg);
        }
    }

    public void startPreview() {
        if (camera == null) return;
        camera.startPreview();
        camera.setPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] bytes, Camera camera) {
                if (imageYuv != null) return;
                imageYuv = bytes;
            }
        });
        if (!threadRunning) new Thread(this).start();
    }

    public void setCameraDisplayOrientation() {
        Camera.CameraInfo info =
                new Camera.CameraInfo();

        Camera.getCameraInfo(cameraToUse, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        orientation = rotation;
        camera.setDisplayOrientation(result);
    }

    public int getOrientation() {
        return orientation;
    }
}
