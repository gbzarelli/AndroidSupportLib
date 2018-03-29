package br.com.helpdev.supportlib.io.install;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import br.com.helpdev.supportlib.io.files.FileUtils;

/**
 * Permission:
 * <p/>
 * <uses-permission android:name="android.permission.INSTALL_PACKAGES"/>
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * <uses-permission android:name="android.permission.INTERNET"/>
 *
 * @author Guilherme Biff Zarelli
 */
public class PackageDownloader {

    public static final int ACTION_START = 0;
    public static final int ACTION_DOWNLOAD = 1;
    public static final int ACTION_FINISH = 2;
    public static final int ACTION_ERROR = 3;

    private Context context;
    private long packageSize;
    private volatile long downloadSize;
    private int action;
    private Throwable error;
    private File file;

    /**
     * <uses-permission android:name="android.permission.INTERNET"/>
     *
     * @param context
     */
    public PackageDownloader(Context context) {
        this.context = context;
        action = ACTION_START;
    }

    public int getAction() {
        return action;
    }

    public long getPackageSize() {
        return packageSize;
    }

    public Throwable getError() {
        return error;
    }

    public long getDownloadSize() {
        return downloadSize;
    }

    public long getProgress() {
        if (packageSize == 0) {
            return 0;
        }
        return (downloadSize * 100) / packageSize;
    }

    public File getFileCache(String tagName) {
        return FileUtils.getPublicFileCacheDownload("gp_" + tagName + "_tmp.apk");
    }

    public File download(String endereco, String tagName) {
        try {
            URL url = new URL(endereco);
            final HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setDoInput(true);
            http.setDoOutput(false);
            http.setConnectTimeout(60_000);
            packageSize = -1;
            http.connect();

            try {
                packageSize = Long.parseLong(http.getHeaderField("content-Length"));
            } catch (Exception e) {
            }


            file = getFileCache(tagName);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            downloadSize = 0;
            action = ACTION_DOWNLOAD;

            FutureTask<Boolean> ft = new FutureTask<>(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    int size;
                    try (InputStream dis = http.getInputStream();
                         FileOutputStream fileOut = new FileOutputStream(file)) {
                        byte[] buffer = new byte[1024];
                        do {
                            if ((size = dis.read(buffer)) > 0) {
                                downloadSize += size;
                                fileOut.write(buffer, 0, size);
                            } else {
                                Thread.sleep(10);
                            }
                        } while (downloadSize < packageSize);
                        try {
                            fileOut.flush();
                        } catch (Throwable t) {
                        }
                    } finally {
                        try {
                            http.disconnect();
                        } catch (Throwable t) {
                        }
                    }
                    return true;
                }
            });
            new Thread(ft, "downloadTask").start();

            int timeout = 60;
            long tmpDownload = 0;
            while (!ft.isDone() && timeout > 0) {
                Thread.sleep(1000);
                if (tmpDownload == downloadSize) {
                    timeout--;
                } else {
                    tmpDownload = downloadSize;
                    timeout = 60;
                }
            }

            if (timeout <= 0) {
                ft.cancel(true);
                error = new Throwable("NÃO FOI POSSÍVEL BAIXAR O ARQUIVO. (TIMEOUT)");
                action = ACTION_ERROR;
            } else {
                action = ACTION_FINISH;
            }

        } catch (Throwable t) {
            error = t;
            action = ACTION_ERROR;
        }
        return file;
    }

    private void install(File file) throws Throwable {
        if (file != null && file.exists()) {
            Installer.install(context, file);
        }
    }

    public void downloadAsync(String endereco, String tagName) {
        run(endereco, tagName, false);
    }

    public void downloadAndInstallAsync(String endereco, String tagName) {
        run(endereco, tagName, true);
    }

    private void run(final String endereco, final String tagName, final boolean install) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                download(endereco, tagName);
                if (install) {
                    File file = getFile();
                    if (file != null)
                        try {
                            install(file);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                            try {
                                file.delete();
                            } catch (Throwable t2) {
                                t2.printStackTrace();
                            }
                        }
                }
            }
        }).start();
    }

    public File getFile() {
        return file;
    }

}
