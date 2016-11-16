package br.com.helpdev.supportlib.media;

import android.app.Activity;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by felipe on 18/08/16.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private int cameraToUse;
    private Activity activity;

    public CameraPreview(Activity activity, int cameraToUse) {
        super(activity);
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
                setCameraDisplayOrientation();
            }
        } catch (Exception e) {
            Log.e(getClass().getName(), "surfaceCreated", e);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        // antes de mudar a orientacao da aplicaçao, deve-se parar a previa, rotacionar e entao comecar novamente
        if (this.surfaceHolder == null || camera == null) {//checa se a superficie esta pronta para receber dados da camera
            return;
        }
        //agora, recrie a previa da camera
        try {
            camera.setPreviewDisplay(this.surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            Log.d("ERROR", "Camera error on surfaceChanged " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        // o aplicativo tem cameras em apenas uma tela, se for usar em mais telas coloque esse codigo nas activities/fragments
        parar();
    }

    public void parar() {
        if (camera != null) {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
        }
        surfaceHolder = null;
        camera = null;
    }

    public final void tirarFoto(Camera.ShutterCallback shutter, Camera.PictureCallback raw, Camera.PictureCallback jpeg) {
        camera.takePicture(shutter, raw, jpeg);
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
        camera.setDisplayOrientation(result);
    }
}
