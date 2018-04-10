package br.com.helpdev.supportlib.system;

/**
 * Created by Guilherme Biff Zarelli on 3/27/18.
 */

import android.content.ContentResolver;
import android.provider.Settings;

/**
 * Created by Guilherme Biff Zarelli on 2/26/18.
 */
public class BrightnessSettings {

    private BrightnessSettings() {
        throw new RuntimeException("No BrightnessUtils");
    }

    /**
     * Define modo de brilho automatico.
     * <uses-permission android:name="android.permission.WRITE_SETTINGS" />
     *
     * @param contentResolver
     * @param enabled
     */
    public static void setAutomatic(ContentResolver contentResolver, boolean enabled) {
        Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, enabled ? Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC : Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }

    /**
     * @param contentResolver
     * @return -1 = impossivel recuperar ; 0 = desabilitado ; 1 = habilitado
     */
    public static int isAutomatic(ContentResolver contentResolver) {
        try {
            return Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Exception e) {
        }
        return -1;
    }

    /**
     * <uses-permission android:name="android.permission.WRITE_SETTINGS" />
     *
     * @param contentResolver
     * @param brightness      passar valor entre 1-255
     */
    public static void setValue(ContentResolver contentResolver, int brightness) {
        if (brightness < 1 || brightness > 255) {
            throw new IllegalArgumentException("brightness not in 1-255");
        }
        Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);  //brightness is an integer variable (1-255), but dont use 0
    }

    /**
     * @param contentResolver
     * @return -1 = impossivel recuperar; valor de retorno: 0-255
     */
    public static int getValue(ContentResolver contentResolver) {
        try {
            return Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);  //returns integer value 0-255
        } catch (Exception e) {
        }
        return -1;
    }

}

