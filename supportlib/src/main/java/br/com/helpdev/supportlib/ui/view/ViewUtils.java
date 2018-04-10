package br.com.grupocriar.swapandroid.ui.view;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.support.annotation.IntDef;
import android.view.Surface;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class ViewUtils {
    @IntDef({
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED,
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE,
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT,
            ActivityInfo.SCREEN_ORIENTATION_USER,
            ActivityInfo.SCREEN_ORIENTATION_BEHIND,
            ActivityInfo.SCREEN_ORIENTATION_SENSOR,
            ActivityInfo.SCREEN_ORIENTATION_NOSENSOR,
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE,
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT,
            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE,
            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT,
            ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR,
            ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE,
            ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT,
            ActivityInfo.SCREEN_ORIENTATION_FULL_USER,
            ActivityInfo.SCREEN_ORIENTATION_LOCKED
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ScreenOrientation {
    }

    @IntDef({Surface.ROTATION_0, Surface.ROTATION_90, Surface.ROTATION_180, Surface.ROTATION_270})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ScreenRotation {
    }

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


    public static boolean lockScreenOrientation(Activity activity, @ScreenOrientation int screenOrientation) {
        activity.setRequestedOrientation(screenOrientation);
        return true;
    }

    public static boolean releaseScreenOrientation(Activity activity) {
        return lockScreenOrientation(activity, ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    public static boolean lockCurrentScreenOrientation(Activity activity) {
        return lockScreenOrientation(activity, activity.getRequestedOrientation());
    }


    public static boolean lockScreenRotation(Activity activity, @ScreenRotation int screenRotation) {
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
