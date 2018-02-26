package br.com.helpdev.supportlib.sistema.pacote;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import br.com.grupocriar.swapandroid.sistema.arquivos.Arquivos;
import br.com.grupocriar.swapandroid.utils.ThisClosable;

/**
 * Permission:
 * <p/>
 * <uses-permission android:name="android.permission.INSTALL_PACKAGES"/>
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * <uses-permission android:name="android.permission.INTERNET"/>
 *
 * @author Guilherme Biff Zarelli
 */
public class GerenciadorPacotes {

    public static final int ACTION_START = 0;
    public static final int ACTION_DOWNLOAD = 1;
    public static final int ACTION_FINISH = 2;
    public static final int ACTION_ERROR = 3;

    private Context context;
    private long total;
    private volatile long download;
    private int action;
    private Throwable error;
    private File file;

    /**
     * <uses-permission android:name="android.permission.INTERNET"/>
     *
     * @param context
     */
    public GerenciadorPacotes(Context context) {
        this.context = context;
        action = ACTION_START;
    }

    public int getAction() {
        return action;
    }

    public long getTotal() {
        return total;
    }

    public Throwable getError() {
        return error;
    }

    public long getDownload() {
        return download;
    }

    public long getProgress() {
        if (total == 0) {
            return 0;
        }
        return (download * 100) / total;
    }

    public File getFileCache(String tagName){
        return Arquivos.getPublicFileCacheDownload("gp_" + tagName + "_tmp.apk");
    }

    public File download(String endereco, String tagName) {
        try {
            URL url = new URL(endereco);
            final HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setDoInput(true);
            http.setDoOutput(false);
            http.setConnectTimeout(60_000);
            total = -1;
            http.connect();

            try {
                total = Long.parseLong(http.getHeaderField("content-Length"));
            } catch (Exception e) {
            }


            file = getFileCache(tagName);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            download = 0;
            action = ACTION_DOWNLOAD;

            FutureTask ft = new FutureTask(new Callable() {
                @Override
                public Object call() throws Exception {
                    int size;
                    InputStream dis = http.getInputStream();
                    FileOutputStream fileOut = new FileOutputStream(file);
                    try {
                        byte[] buffer = new byte[1024];
                        do {
                            if ((size = dis.read(buffer)) > 0) {
                                download += size;
                                fileOut.write(buffer, 0, size);
                            } else {
                                Thread.sleep(10);
                            }
                        } while (download < total);
                    } finally {
                        try {
                            fileOut.flush();
                        } catch (Throwable t) {
                        }
                        ThisClosable.fecha(dis);
                        ThisClosable.fecha(fileOut);
                    }
                    return true;
                }
            });
            new Thread(ft, "downloadTask").start();

            int timeout = 60;
            long tmpDownload = 0;
            while (!ft.isDone() && timeout > 0) {
                Thread.sleep(1000);
                if (tmpDownload == download) {
                    timeout--;
                } else {
                    tmpDownload = download;
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

    private void install(File file) {
        if (file != null && file.exists()) {
            PacotesUtils.instalarPacote(context, file);
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
                    install(getFile());
                }
            }
        }).start();
    }

    public File getFile() {
        return file;
    }

}
