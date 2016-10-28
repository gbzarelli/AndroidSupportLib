package br.com.helpdev.supportlib.sistema.arquivos;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by demantoide on 10/07/15.
 */
public class Arquivos {

    public static File getTempFile(Context context, String header, String extensao) throws IOException {
        File dir = context.getCacheDir();
        return createTempFile(dir, header, extensao);
    }


    public static File getExternalTempFile(Context context, String header, String extensao) throws IOException {
        File dir = context.getExternalCacheDir();
        return createTempFile(dir, header, extensao);
    }

    public static File getExternalFileCacheDownload(String nameFile) {
        File file = new File(Environment.getExternalStorageDirectory(), "file_cache");
        if (!file.exists())
            file.mkdir();

        return new File(file, nameFile);
    }

    private static File createTempFile(File dir, String header, String extensao) throws IOException {
        File file = new File(dir, "tempfile_" + System.nanoTime() + "_" + header + "." + extensao);
        file.createNewFile();
        return file;
    }
}
