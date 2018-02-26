package br.com.helpdev.supportlib.media;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;

import java.util.ArrayList;
import java.util.List;

import br.com.grupocriar.swapandroid.utils.UnitUtils;

/**
 * Created by Guilherme Biff Zarelli on 12/5/17.
 */

public abstract class CameraPreviewFacesAbs extends CameraPreview {

    private int facesDetectadas;


    private Paint paintText, paintRect;
    private boolean drawRectFace;
    private boolean faceDetectOperacional;
    private boolean faceDetectNativeEnabled;
    private boolean faceDetectNonNativeEnabled;
    private boolean drawTextFacesDetects;
    private List<Rect> rects = new ArrayList<>();

    public CameraPreviewFacesAbs(Activity activity, int cameraToUse, boolean drawRectFace) {
        this(activity, cameraToUse, drawRectFace, DELAY_PREVIEW_PROCESS_DEFAULT);
    }

    public CameraPreviewFacesAbs(Activity activity, int cameraToUse, boolean drawRectFace, int delayPreviewProcess) {
        super(activity, cameraToUse, delayPreviewProcess);
        this.drawRectFace = drawRectFace;
        drawTextFacesDetects = true;
        paintText = new Paint();
        paintText.setColor(Color.rgb(255, 0, 0));
        paintText.setFakeBoldText(true);
        paintText.setTextSize(UnitUtils.pxToDp(20));

        paintRect = new Paint();
        paintRect.setColor(Color.GREEN);
        paintRect.setStyle(Paint.Style.STROKE);
        paintRect.setStrokeWidth(5);
    }

    public boolean isDrawTextFacesDetects() {
        return drawTextFacesDetects;
    }

    public Paint getPaintText() {
        return paintText;
    }

    public void setPaintText(Paint paintText) {
        this.paintText = paintText;
    }

    public Paint getPaintRect() {
        return paintRect;
    }

    public void setPaintRect(Paint paintRect) {
        this.paintRect = paintRect;
    }

    public void setDrawTextFacesDetects(boolean drawTextFacesDetects) {
        this.drawTextFacesDetects = drawTextFacesDetects;
    }

    /**
     * Bitmap bmp = BitmapFactory.decodeByteArray(imageJpg, 0, imageJpg.length);
     *
     * @param imageJpg
     */
    @Override
    public void postProcessImage(byte[] imageJpg) {
        super.postProcessImage(imageJpg);
        if (!faceDetectNativeEnabled) {
            if (faceDetectNonNativeEnabled) {
                if (drawRectFace) {
                    List<Rect> rects = processFacesNonNativeRects(imageJpg);
                    facesDetectadas = rects.size();
                    this.rects = rects;
                } else {
                    facesDetectadas = processFacesNonNative(imageJpg);
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        invalidate();
                    }
                });
            }
        }
    }


    @Override
    protected void onSurfaceChangedStartPreview() {
        super.onSurfaceChangedStartPreview();
        enableDetectFaces();
    }

    //
    private void enableDetectFaces() {
        //**DETECTCAO FACIAL**//

        setWillNotDraw(false);
        if (camera.getParameters().getMaxNumDetectedFaces() > 0) {
            faceDetectNativeEnabled = true;
        }
        boolean ret = onConfigureEnableFaceDetect(faceDetectNativeEnabled);
        if (faceDetectNativeEnabled && !ret) {
            faceDetectNativeEnabled = false;
        } else if (!faceDetectNativeEnabled && ret) {
            faceDetectNonNativeEnabled = true;
            faceDetectOperacional = true;
        }

        if (ret && faceDetectNativeEnabled) {
            faceDetectOperacional = true;
            camera.setFaceDetectionListener(new Camera.FaceDetectionListener() {
                @Override
                public void onFaceDetection(Camera.Face[] fs, Camera camera) {
                    boolean invalidate = fs.length != facesDetectadas || fs.length > 0;
                    facesDetectadas = fs.length;
                    rects.clear();
                    if (fs.length > 0) {
                        if (drawRectFace) {
                            for (Camera.Face face : fs) {
                                rects.add(newRect(face));
                            }
                        }
                    }
                    processedFacesNative(facesDetectadas);
                    if (invalidate) {
                        invalidate();
                    }
                }
            });
            try {
                camera.startFaceDetection();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    public Rect newRect(Camera.Face face) {
        double viewWidth = getWidth();
        double viewHeight = getHeight();
        double imageWidth = previewSizeWidth;
        double imageHeight = previewSizeHeight;

        double scale = Math.min(viewWidth / imageWidth, viewHeight / imageHeight);

        return new Rect(
                (int) ((viewWidth - face.rect.left) * scale),//invertendo horizontalmente (sem inverter tirar 'viewWidth-')
                (int) (face.rect.top * scale),
                (int) (((viewWidth - face.rect.left) + face.rect.width()) * scale),//invertendo horizontalmente (sem inverter tirar 'viewWidth-')
                (int) ((face.rect.top + face.rect.height()) * scale)
        );
    }

    public int getFacesDetectadas() {
        return facesDetectadas;
    }

    public boolean isDrawRectFace() {
        return drawRectFace;
    }

    public boolean isFaceDetectOperacional() {
        return faceDetectOperacional;
    }

    public boolean isFaceDetectNativeEnabled() {
        return faceDetectNativeEnabled;
    }

    public boolean isFaceDetectNonNativeEnabled() {
        return faceDetectNonNativeEnabled;
    }

    private String textToDraw = null;

    public String getTextToDraw() {
        return textToDraw;
    }

    public void setTextToDraw(String textToDraw) {
        this.textToDraw = textToDraw;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (drawTextFacesDetects) {
            if (faceDetectOperacional) {
                canvas.drawText(textToDraw == null ? "FACES DETECTADAS: " + facesDetectadas : textToDraw, 10, canvas.getHeight() - 10, paintText);
            } else {
                canvas.drawText("DETECTOR DE FACES NÃO DISPONÍVEL", 10, canvas.getHeight() - 10, paintText);
            }
        }
        if (drawRectFace) {
            for (Rect rect : rects) {
                canvas.drawRect(rect, paintRect);
            }
        }
    }


    /**
     * Passa como parametro a variavel que diz se o forma nativa esta habilitada ou nao.
     * Se o parametro de entrada for false e o retorno for true habilita a deteccao nao nativa.
     *
     * @param enableNative retorna true caso a deteccao nativa esteja habilitada.
     * @return true para habilitar, false para desabilitar deteccao facial
     */
    public abstract boolean onConfigureEnableFaceDetect(boolean enableNative);

    public abstract List<Rect> processFacesNonNativeRects(byte[] jpeg);

    public abstract int processFacesNonNative(byte[] jpeg);

    public abstract void processedFacesNative(int faces);
}
