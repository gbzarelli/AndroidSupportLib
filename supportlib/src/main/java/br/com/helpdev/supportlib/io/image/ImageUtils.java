package br.com.grupocriar.swapandroid.io.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Guilherme Biff Zarelli on 8/1/17.
 */

public class ImageUtils {

    private ImageUtils() {
        throw new RuntimeException("No ImageUtils");
    }

    public static byte[] scaledImage(Uri uri, int largura, int altura, int quality, boolean overwrite) throws IOException {
        try (FileInputStream fis = new FileInputStream(new File(uri.getPath()))) {
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            return scaledImage(bitmap, uri, largura, altura, quality, overwrite);
        }
    }

    public static byte[] scaledImage(Bitmap bitmap, Uri uri, int largura, int altura, int quality, boolean overwrite) throws IOException {
        if (bitmap == null) {
            throw new IllegalArgumentException("NÃO FOI POSSÍVEL CARREGAR A IMAGEM PELA URI");
        }

        if (largura > 0 && altura > 0) {
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            if (w > h) {
                bitmap = Bitmap.createScaledBitmap(bitmap, largura, altura, false);
            } else {
                bitmap = Bitmap.createScaledBitmap(bitmap, altura, largura, false);
            }
        }

        if (quality < 0) {
            quality = 100;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] imagemScalada = baos.toByteArray();

        if (overwrite) {
            try (FileOutputStream fos = new FileOutputStream(new File(uri.getPath()), false)) {
                fos.write(imagemScalada, 0, imagemScalada.length);
                fos.flush();
            }
        }

        return imagemScalada;

    }
}
