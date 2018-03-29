package br.com.grupocriar.swapandroid.io.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import br.com.grupocriar.swapandroid.system.RuntimeAndroid;

/**
 * Created by demantoide on 17/03/16.
 */
public final class NetworkUtils {

    public static final String LOG = "NetworkUtils";

    private NetworkUtils() {
        throw new IllegalArgumentException("No NetworkUtils!");
    }

    public static boolean ping(String hostUmed) {
        try {
            Process process = RuntimeAndroid.getProcess("/system/bin/ping -c 1 " + hostUmed);
            try {
                int mExitValue = process.waitFor();
                return mExitValue == 0;
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
        return nwinf != null && nwinf.isAvailable();
    }

    public static boolean hasMobileNetworkConnected(Context context) {
        NetworkInfo nwinf = getNetWorkInfo(context, ConnectivityManager.TYPE_MOBILE);
        return nwinf != null && nwinf.isConnectedOrConnecting();
    }

    /**
     * @param type ConnectivityManager.TYPE_MOBILE
     * @return
     */
    @SuppressLint("MissingPermission")
    public static NetworkInfo getNetWorkInfo(Context context, int type) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (null == connectivityManager) ? null : connectivityManager.getNetworkInfo(type);
    }

    public static String getMobileNetworkType(TelephonyManager telephonyManager) {
        int networkType = telephonyManager.getNetworkType();
        return getMobileNetworkType(networkType);
    }


    public static final String MOBILE_NETWORK_2G = "2G";
    public static final String MOBILE_NETWORK_3G = "3G";
    public static final String MOBILE_NETWORK_4G = "4G";
    public static final String MOBILE_NETWORK_UNKNOWN = "UNKNOWN";

    /**
     * @param networkType
     * @return MOBILE_NETWORK_2G or MOBILE_NETWORK_3G or MOBILE_NETWORK_4G
     */
    public static String getMobileNetworkType(int networkType) {
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return MOBILE_NETWORK_2G;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return MOBILE_NETWORK_3G;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return MOBILE_NETWORK_4G;
            default:
                return MOBILE_NETWORK_UNKNOWN;
        }
    }

    @SuppressLint("MissingPermission")
    public static boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (null == cm) return false;
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }
}
