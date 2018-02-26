package br.com.helpdev.supportlib.utils;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by demantoide on 16/11/16.
 */

public class UnitUtils {
    private UnitUtils() {
        throw new RuntimeException("No UnitUtils!");
    }
    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToSp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().scaledDensity);
    }

    public static int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    public static int spToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dp, Resources.getSystem().getDisplayMetrics());
    }
}
