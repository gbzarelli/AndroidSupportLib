package br.com.grupocriar.swapandroid.system;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.BitmapFactory;

import java.io.IOException;

/**
 * Created by demantoide on 01/07/16.
 */
public class WallpaperUtils {
    private WallpaperUtils() {
        throw new RuntimeException("No WallpaperUtils!");
    }

    @SuppressLint("MissingPermission")
    public static void setWallpaperDrawable(Context context, int resIdDrawable) {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        try {
            wallpaperManager.setBitmap(BitmapFactory.decodeResource(context.getResources(), resIdDrawable));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
