package br.com.helpdev.supportlib.io.files;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Guilherme Biff Zarelli on 10/07/15.
 */
public class FileUtils {

    private FileUtils() {
        throw new RuntimeException("No FileUtils!");
    }

    public static File getTempFile(Context context, String header, String extensao) throws IOException {
        return createTempFile(context.getCacheDir(), header, extensao);
    }

    public static File getExternalTempFile(Context context, String fullNameFille) throws IOException {
        return createTempFile(context.getExternalCacheDir(), fullNameFille);
    }

    public static File getExternalTempFile(Context context, String header, String extensao) throws IOException {
        return createTempFile(context.getExternalCacheDir(), header, extensao);
    }

    public static File getPublicFileCacheDownload(String header, String extensao) {
        return getPublicFileCacheDownload("tempfile_" + System.nanoTime() + "_" + header + "." + extensao);
    }

    public static File getPublicFileCacheDownload(String fullFileName) {
        File file = new File(Environment.getExternalStorageDirectory(), "file_cache");
        if (!file.exists())
            file.mkdir();

        return new File(file, fullFileName);
    }

    private static File createTempFile(File dir, String header, String extensao) throws IOException {
        return createTempFile(dir, "tempfile_" + System.nanoTime() + "_" + header + "." + extensao);
    }

    private static File createTempFile(File dir, String nameFile) throws IOException {
        File file = new File(dir, nameFile);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        return file;
    }

    public static String encodeFileToBase64Binary(byte[] bytes) {
        byte[] encoded = android.util.Base64.encode(bytes, 0);
        String encodedString = new String(encoded);
        return encodedString;
    }

    public static String encodeFileToBase64Binary(File file)
            throws IOException {
        byte[] bytes = loadFile(file);
        return encodeFileToBase64Binary(bytes);
    }

    public static void writeFile(byte[] data, File pathFile, String nameFile) throws FileNotFoundException, IOException {
        File fileWrite = new File(pathFile, nameFile);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        if (!fileWrite.exists()) {
            fileWrite.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(fileWrite);
        fos.write(data);
        fos.flush();
        try {
            fos.close();
        } catch (Throwable t) {
        }
    }

    public static byte[] loadFile(File file) throws IOException {
        byte[] bytes;
        InputStream is = new FileInputStream(file);
        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            throw new IOException("File to large " + file.getName());
        }
        bytes = new byte[(int) length];
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        try {
            is.close();
        } catch (Throwable t) {
        }
        return bytes;
    }
}
