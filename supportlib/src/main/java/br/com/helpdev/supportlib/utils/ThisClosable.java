package br.com.helpdev.supportlib.utils;
import android.os.Build;
import android.util.Log;

import java.io.Closeable;

/**
 * Created by demantoide on 16/02/16.
 */
public final class ThisClosable {
    private ThisClosable() {
        throw new RuntimeException("No ThisClosable!");
    }
    private static final String LOG = "ThisClosable";


    public static void fecha(Object closeable) {
        if (closeable != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ((Closeable) closeable).close();
                } else {
                    closeable.getClass().getMethod("close").invoke(closeable);
                }
            } catch (Throwable e) {
                Log.e(LOG, "fecha(Closeable closeable)", e);
            }
        }
    }

    public static void fecha(Object closeable, Object... closeables) {
        fecha(closeable);
        if (closeables != null) {
            for (Object closeable1 : closeables) {
                fecha(closeable1);
            }
        }
    }
}
