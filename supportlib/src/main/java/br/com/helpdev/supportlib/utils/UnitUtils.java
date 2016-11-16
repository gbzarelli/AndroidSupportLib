package br.com.helpdev.supportlib.utils;

import android.content.res.Resources;

/**
 * Created by Guilherme Biff Zarelli on 16/11/16.
 */

public class UnitUtils {

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
}
