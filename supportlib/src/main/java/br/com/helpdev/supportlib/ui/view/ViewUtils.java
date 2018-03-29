package br.com.grupocriar.swapandroid.ui.view;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.Surface;
import android.view.View;

public final class ViewUtils {

    private ViewUtils() {
        throw new RuntimeException("No ViewUtils");
    }

    /**
     * <pre>
     * Retorna a referência de uma <i>View</i> da <i>Activity</i>, à partir de seu ID
     * Button btn = ViewUtils.find(MainActivity.this, R.id.btn);
     * </pre>
     *
     * @param activity utilizada para buscar a referencia da <i>View</i>
     * @param id       da <i>View</i> presente da classe <i>R</i> do projeto
     * @param <T>      tipo da <i>View</i> retornada
     * @return referência à <i>View</i>
     */
    public static <T extends View> T find(Activity activity, int id) {
        if (null == activity) {
            throw new IllegalArgumentException("activity is null");
        }

        return activity.findViewById(id);
    }

    /**
     * <pre>
     * Retorna a referência de uma <i>View</i> à partir de seu ID com base na <i>View</i> que representa seu <i>Parent</i>
     * Button btn = ViewUtils.find(MainActivity.this, R.id.btn);
     * </pre>
     *
     * @param view que representa o <i>Parent</i> utilizado para buscar a referencia da <i>View</i>
     * @param id   da <i>View</i> presente da classe <i>R</i> do projeto
     * @param <T>  tipo da <i>View</i> retornada
     * @return referência à <i>View</i>
     */
    public static <T extends View> T find(View view, int id) {
        if (null == view) {
            throw new IllegalArgumentException("parent view is null");
        }

        return view.findViewById(id);
    }

    /**
     * @param activity
     * @param screenOrientation ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
     */
    public static boolean lockScreenOrientation(Activity activity, int screenOrientation) {
        activity.setRequestedOrientation(screenOrientation);
        return true;
    }

    public static boolean releaseScreenOrientation(Activity activity) {
        return lockScreenOrientation(activity, ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    public static boolean lockCurrentScreenOrientation(Activity activity) {
        return lockScreenOrientation(activity, activity.getRequestedOrientation());
    }

    /**
     * @param activity
     * @param screenRotation Surface.ROTATION_0 / 90 / 180 / 270
     */
    public static boolean lockScreenRotation(Activity activity, int screenRotation) {
        if (screenRotation > -1) {
            int orientation = activity.getRequestedOrientation();
            switch (screenRotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_180:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                case Surface.ROTATION_270:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
            }
            return lockScreenOrientation(activity, orientation);
        } else {
            return false;
        }
    }


}
