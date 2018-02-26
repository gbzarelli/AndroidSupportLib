package br.com.helpdev.supportlib.sistema.rede.wifi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;
import java.util.regex.Pattern;

/**
 * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
 * <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
 * <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />
 * Created by demantoide on 26/07/15.
 */
public class Wifi {
    public static final String SECURITY_WEP = "WEP";
    public static final String SECURITY_WPA = "WPA";
    public static final String SECURITY_OPEN = "OPEN";

    private static final String LOG = "Wifi";

    private Wifi() {
        throw new IllegalArgumentException("No Wifi");
    }


    public static WifiManager getWifiManager(Context activity) {
        return (WifiManager) activity.getSystemService(Activity.WIFI_SERVICE);
    }

    @SuppressLint("MissingPermission")
    public static boolean isWifiEnabled(WifiManager wifiManager) {
        if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED ||
                wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLING) {
            return false;
        }
        return true;
    }


    @SuppressLint("MissingPermission")
    public static void connectSSIDAsync(final Context activity, final String networkSSID, final String networkPass, final String security) {
        new Thread() {
            @Override
            public void run() {
                try {
                    connectSSID(activity, networkSSID, networkPass, security);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @SuppressLint("MissingPermission")
    public static boolean isConnectedWith(Context context, String ssid) {
        WifiManager wifiManager = getWifiManager(context);
        if (wifiManager == null || !isWifiEnabled(wifiManager)) {
            return false;
        }
        WifiInfo wi = wifiManager.getConnectionInfo();
        if (wi != null && wi.getSupplicantState() == SupplicantState.COMPLETED) {
            String ssid1 = wi.getSSID();
            if (!TextUtils.isEmpty(ssid1) && ssid1.toLowerCase().contains(ssid.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    @SuppressLint("MissingPermission")
    public static String getSSID(Context context) {
        return getSSID(getWifiManager(context));
    }

    @SuppressLint("MissingPermission")
    public static String getSSID(WifiManager wifiManager) {
        try {
            if (wifiManager == null || !isWifiEnabled(wifiManager)) {
                return null;
            }
            WifiInfo wi = wifiManager.getConnectionInfo();
            if (wi != null && wi.getSupplicantState() == SupplicantState.COMPLETED && !TextUtils.isEmpty(wi.getSSID()))
                return formatSSID(wi.getSSID());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    public static String formatSSID(String ssid) {
        return ssid.replaceAll(Pattern.quote("\""), "");
    }

    @SuppressLint("MissingPermission")
    public static int connectSSID(Context activity, String networkSSID, String networkPass, String security) throws Exception {
        final WifiManager wm = getWifiManager(activity);
        if (wm == null) {
            return -1;
        }
        try {
            if (WifiAP.getWifiApState(wm) != WifiAP.WIFI_AP_STATE_DISABLED) {
                WifiAP.desligarAP(wm);
            }
        } catch (Exception e) {
            Log.e(LOG, "run", e);
        }

        if (!wm.isWifiEnabled()) {
            wm.setWifiEnabled(true);
        }

        int tryEnable = 0;
        while (tryEnable < 10 && !wm.isWifiEnabled()) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
            tryEnable++;
        }

        if (wm.isWifiEnabled() && wm.getConnectionInfo() != null
                && wm.getConnectionInfo().getSSID() != null
                && wm.getConnectionInfo().getSSID().contains(networkSSID)) {
            Log.i(LOG, "wifi já conectado a rede");
            return wm.getConnectionInfo().getNetworkId();
        }

        WifiConfiguration wifiConfiguration = null;
        List<WifiConfiguration> list = wm.getConfiguredNetworks();
        if (list != null) {
            for (WifiConfiguration i : list) {
                if (i.SSID != null && i.SSID.contains(networkSSID)) {
                    wifiConfiguration = i;
                    break;
                }
            }
        }
        if (wifiConfiguration == null) {
            wifiConfiguration = new WifiConfiguration();
            wifiConfiguration.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain ssid in quotes
            wifiConfiguration.status = WifiConfiguration.Status.ENABLED;
            wifiConfiguration.priority = 40;

            if (security.contains(SECURITY_WEP)) {
                Log.v(LOG, "Configuring WEP");
                wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                wifiConfiguration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                wifiConfiguration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
                wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);

                if (networkPass.matches("^[0-9a-fA-F]+$")) {
                    wifiConfiguration.wepKeys[0] = networkPass;
                } else {
                    wifiConfiguration.wepKeys[0] = "\"".concat(networkPass).concat("\"");
                }

                wifiConfiguration.wepTxKeyIndex = 0;

            } else if (security.contains(SECURITY_WPA)) {
                Log.v(LOG, "Configuring WPA");

                wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

                wifiConfiguration.preSharedKey = "\"" + networkPass + "\"";

            } else {
                Log.v(LOG, "Configuring OPEN network");
                wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                wifiConfiguration.allowedAuthAlgorithms.clear();
                wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            }

            int id = wm.addNetwork(wifiConfiguration);
            wm.saveConfiguration();
            wifiConfiguration.networkId = id;
            Log.v(LOG, "Add result " + id);
        }
        if (wifiConfiguration.networkId != -1) {
            connectNetworkConfiguration(wm, wifiConfiguration.networkId);
        }
        return wifiConfiguration.networkId;
    }

    @SuppressLint("MissingPermission")
    public static void connectNetworkConfiguration(WifiManager wm, int networkId) throws Exception {
        wm.disconnect();
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
        }
        wm.enableNetwork(networkId, true);
    }


    @SuppressLint("MissingPermission")
    public static void enableWifi(Activity activity) {
        WifiManager wifiManager = Wifi.getWifiManager(activity);
        if (!Wifi.isWifiEnabled(wifiManager)) {
            wifiManager.setWifiEnabled(true);
        }
    }
}
