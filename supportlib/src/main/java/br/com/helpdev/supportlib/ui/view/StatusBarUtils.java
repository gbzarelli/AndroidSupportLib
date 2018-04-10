package br.com.grupocriar.swapandroid.ui.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.support.annotation.RequiresPermission;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by Guilherme Biff Zarelli on 1/29/18.
 */
public class StatusBarUtils {

    @RequiresPermission(Manifest.permission.SYSTEM_ALERT_WINDOW)
    public static void removeViewGroup(Activity activity, ViewGroup viewGroup) {
        WindowManager manager = (WindowManager) activity.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        if (null == manager) throw new RuntimeException("No WindowManager service");
        manager.removeView(viewGroup);
    }

    @RequiresPermission(Manifest.permission.SYSTEM_ALERT_WINDOW)
    public static ViewGroup preventStatusBarExpansion(Activity activity) {
        return preventStatusBarExpansion(activity, new DefaultCustomView(activity));
    }

    @RequiresPermission(Manifest.permission.SYSTEM_ALERT_WINDOW)
    public static ViewGroup preventStatusBarExpansion(Activity activity, ViewGroup viewGroup) {
        WindowManager manager = ((WindowManager) activity.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE));
        if (null == manager) throw new RuntimeException("No WindowManager service");

        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                // this is to enable the notification to recieve touch events
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                // Draws over status bar
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        //https://stackoverflow.com/questions/1016896/get-screen-dimensions-in-pixels
        int resId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int result = 0;
        if (resId > 0) {
            result = activity.getResources().getDimensionPixelSize(resId);
        }

        localLayoutParams.height = result;

        localLayoutParams.format = PixelFormat.TRANSPARENT;

        if (viewGroup == null) {
            viewGroup = new DefaultCustomView(activity);
        }
        manager.addView(viewGroup, localLayoutParams);
        return viewGroup;
    }

    public static class DefaultCustomView extends ViewGroup {

        public DefaultCustomView(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return true;
        }
    }
}
