package br.com.helpdev.supportlib.sistema.rede;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;

import br.com.helpdev.supportlib.sistema.RuntimeAndroid;

/**
 * Created by Guilherme Biff Zarelli on 17/03/16.
 */
public final class NetworkUtils {


    public static final String LOG = "NetworkUtils";

    public static boolean ping(String hostUmed) {
        RuntimeAndroid runtimeAndroid = new RuntimeAndroid();
        try {
            Process process = runtimeAndroid.getProcess("/system/bin/ping -c 1 " + hostUmed);
            try {
                int mExitValue = process.waitFor();
                if (mExitValue == 0) {
                    return true;
                } else {
                    return false;
                }
            } finally {
                process.destroy();
            }
        } catch (Throwable ignore) {
            Log.e(LOG, "ping", ignore);
        }
        return false;
    }

    public static boolean hasMobileNetworkAvailable(Context context) {
        NetworkInfo nwinf = getNetWorkInfo(context, ConnectivityManager.TYPE_MOBILE);
        if (nwinf != null && nwinf.isAvailable()) {
            return true;
        }
        return false;
    }

    public static boolean hasMobileNetworkConnected(Context context) {
        NetworkInfo nwinf = getNetWorkInfo(context, ConnectivityManager.TYPE_MOBILE);
        if (nwinf != null && nwinf.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    /**
     * @param type ConnectivityManager.TYPE_MOBILE
     * @return
     */
    public static NetworkInfo getNetWorkInfo(Context context, int type) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nwinf = connectivityManager.getNetworkInfo(type);
        return nwinf;
    }
}
