package br.com.helpdev.supportlib.sistema.pacote;

import android.content.Context;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.helpdev.supportlib.sistema.arquivos.Arquivos;

/**
 * Permissoes:
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
    private long download;
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

    public File download(String endereco, String tagName) {
        try {
            URL url = new URL(endereco);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
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

            DataInputStream dis = new DataInputStream(http.getInputStream());
            
            file = Arquivos.getExternalFileCacheDownload("gp_" + tagName + "_tmp.apk");
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            FileOutputStream fileOut = new FileOutputStream(file);

            byte[] buffer;
            int size;
            download = 0;

            action = ACTION_DOWNLOAD;

            do {
                if ((size = dis.available()) > 0) {
                    buffer = new byte[size];
                    size = dis.read(buffer);
                    download += size;
                    fileOut.write(buffer, 0, size);
                } else {
                    Thread.sleep(10);
                }
            } while (download < total);

            dis.close();
            fileOut.flush();
            fileOut.close();

            action = ACTION_FINISH;

        } catch (Throwable t) {
            error = t;
            action = ACTION_ERROR;
        }
        return file;
    }

    public void install(File file) {
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
