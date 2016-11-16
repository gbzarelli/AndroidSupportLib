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
        System.out.println("executeCommand");
        RuntimeAndroid runtimeAndroid = new RuntimeAndroid();
        try {
            int mExitValue = runtimeAndroid.getProcess("/system/bin/ping -c 1 " + hostUmed).waitFor();
            System.out.println(" mExitValue " + mExitValue);
            if (mExitValue == 0) {
                return true;
            } else {
                return false;
            }
        } catch (InterruptedException ignore) {
            Log.e(LOG, "ping", ignore);
        } catch (IOException e) {
            Log.e(LOG, "ping", e);
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
