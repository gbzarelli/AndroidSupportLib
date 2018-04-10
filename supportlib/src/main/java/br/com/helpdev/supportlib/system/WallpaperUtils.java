package br.com.helpdev.supportlib.system;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.RequiresPermission;

import java.io.IOException;

/**
 * Created by Guilherme Biff Zarelli on 01/07/16.
 */
public class WallpaperUtils {
    private WallpaperUtils() {
        throw new RuntimeException("No WallpaperUtils!");
    }

    @RequiresPermission(android.Manifest.permission.SET_WALLPAPER)
    public static void setWallpaperDrawable(Context context, int resIdDrawable) throws IOException {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        wallpaperManager.setBitmap(BitmapFactory.decodeResource(context.getResources(), resIdDrawable));
    }

}
