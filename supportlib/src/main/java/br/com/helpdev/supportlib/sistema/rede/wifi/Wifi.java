package br.com.helpdev.supportlib.sistema.rede.wifi;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

/**
 * Created by Guilherme Biff Zarelli on 26/07/15.
 */
public class Wifi {
    private static final String LOG = "Wifi";

    public static void conectarApAsync(final Activity activity, final String ssid, final String pass) {
        new Thread() {
            @Override
            public void run() {
                try {
                    conectarAp(activity, ssid, pass);
                } catch (Exception e) {
                    Log.e(LOG, "connectAP", e);
                }
            }
        }.start();
    }

    public static void conectarAp(final Activity activity, final String ssid, final String pass) throws Exception {
        WifiAP wap = new WifiAP(activity);
        WifiConfiguration wc = new WifiConfiguration();
        wc.SSID = ssid;

        wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        wc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
//                    wc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
//                    wc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);

        wc.priority = 1;
        wc.preSharedKey = pass;
        wc.wepTxKeyIndex = 0;

        wap.setWifiApEnable(null, false);
        wap.setWifiApEnable(wc, true);
    }

    public static void enableWifi(Activity activity) {
        WifiManager wifiManager = getWifiManager(activity);
        if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED ||
                wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLING) {
            wifiManager.setWifiEnabled(true);
        }
    }

    public static void desligarAP(Activity activity) throws Exception {
        WifiAP wap = new WifiAP(activity);
        wap.setWifiApEnable(null, false);
    }

    public static WifiManager getWifiManager(Context activity) {
        return (WifiManager) activity.getSystemService(Activity.WIFI_SERVICE);
    }

    public static void connectSSIDAsync(final Activity activity, final String networkSSID, final String networkPass) {
        new Thread() {
            @Override
            public void run() {
                try {
                    connectSSID(activity, networkSSID, networkPass);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static boolean isConnectedWith(Context context, String ssid) {
        WifiManager wifiManager = getWifiManager(context);
        if (wifiManager != null && wifiManager.getConnectionInfo() != null) {
            String ssid1 = wifiManager.getConnectionInfo().getSSID();
            if (!TextUtils.isEmpty(ssid1) && ssid1.toLowerCase().contains(ssid.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static void connectSSID(Activity activity, String networkSSID, String networkPass) throws Exception {
        Log.i(LOG, "networkSSID: {" + networkSSID + "}");
        Log.i(LOG, "networkPass: {" + networkPass + "}");
        final WifiManager wm = getWifiManager(activity);
        if (wm == null) {
            return;
        }
        try {
            WifiAP wifiAP = new WifiAP(activity);
            if (wifiAP.getWifiApState() != WifiAP.WIFI_AP_STATE_DISABLED) {
                wifiAP.setWifiApEnable(null, false);
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
            return;
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
            Log.i(LOG, "wifiConfiguration not found, creating network...");
            WifiConfiguration wc = new WifiConfiguration();
            wc.SSID = networkSSID;

            wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);

            wc.preSharedKey = networkPass;
            wc.wepTxKeyIndex = 0;

            int id = wm.addNetwork(wc);
            wm.saveConfiguration();
            wc.networkId = id;
        }
        connectNetworkConfiguration(wm, wifiConfiguration);
    }


    public static void connectNetworkConfiguration(WifiManager wm, WifiConfiguration wifiConfiguration) throws Exception {
        Log.i(LOG, "idNetWork: {" + wifiConfiguration.networkId + "}");
        wm.disconnect();
        Log.i(LOG, "Desconectando...");

        try {
            Thread.sleep(2000);
        } catch (Exception e) {
        }

        Log.i(LOG, "Conectando...");
        if (wm.enableNetwork(wifiConfiguration.networkId, true)) {
            Log.i(LOG, "conectado");
        } else {
            Log.i(LOG, "falha ao conectarEMS");
        }
    }
}
