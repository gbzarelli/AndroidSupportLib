package br.com.helpdev.supportlib.utils;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by Guilherme Biff Zarelli on 16/11/16.
 */

public class UnitUtils {

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }
}
