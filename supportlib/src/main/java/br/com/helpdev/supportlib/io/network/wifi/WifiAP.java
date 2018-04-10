package br.com.helpdev.supportlib.io.network.wifi;

/**
 * Created by Guilherme Biff Zarelli on 29/09/15.
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.annotation.IntDef;
import android.support.annotation.RequiresPermission;
import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

/**
 * <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
 * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
 *
 * @author guilherme
 */
public class WifiAP {

    private static final String LOG = "WifiAP";

    /**
     * WIFI_AP_STATE_DISABLED = (Integer) WifiManager.class.getDeclaredField("WIFI_AP_STATE_DISABLED").get(Integer.class);
     * WIFI_AP_STATE_ENABLING = (Integer) WifiManager.class.getDeclaredField("WIFI_AP_STATE_ENABLING").get(Integer.class);
     * WIFI_AP_STATE_ENABLED = (Integer) WifiManager.class.getDeclaredField("WIFI_AP_STATE_ENABLED").get(Integer.class);
     * WIFI_AP_STATE_FAILED = (Integer) WifiManager.class.getDeclaredField("WIFI_AP_STATE_FAILED").get(Integer.class);
     * WIFI_AP_STATE_DISABLING = (Integer) WifiManager.class.getDeclaredField("WIFI_AP_STATE_DISABLING").get(Integer.class);
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({WIFI_AP_STATE_DISABLED, WIFI_AP_STATE_ENABLING, WIFI_AP_STATE_ENABLED, WIFI_AP_STATE_FAILED, WIFI_AP_STATE_DISABLING})
    @interface WifiAPState {
    }

    public static final int WIFI_AP_STATE_DISABLED = 11;
    public static final int WIFI_AP_STATE_ENABLING = 12;
    public static final int WIFI_AP_STATE_ENABLED = 13;
    public static final int WIFI_AP_STATE_FAILED = 14;
    public static final int WIFI_AP_STATE_DISABLING = 10;

    /**
     * Permissões necessarias:
     * <p/>
     * <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     */
    private WifiAP() {
        throw new RuntimeException("No WifiAP");
    }

    public static WifiManager getWifiManager(Context context) {
        return (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public static WifiConfiguration getWifiAPConfiguration(WifiManager wifi_manager) throws Exception {
        Method wifiApConfigurationMethod = wifi_manager.getClass().getMethod("getWifiApConfiguration");
        return (WifiConfiguration) wifiApConfigurationMethod.invoke(wifi_manager);
    }

    /**
     * <pre>
     *  Para utilizar a configuração de AccessPoint, os IPS dos dispositivos devem seguir o padrão 192.168.43.XXX
     * Exemplo:
     * <br>WifiConfiguration netConfig = new WifiConfiguration();
     * <br>netConfig.SSID = "EVE03";
     * <br>netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
     * <br>netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
     * <br>netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
     * <br>netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
     *
     * Exemplo com senha e tipo de segurança WPA2 PSK
     * WifiConfiguration netConfig = new WifiConfiguration();
     * netConfig.SSID = "UMED-0000";
     * netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
     * netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
     * netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
     * netConfig.allowedKeyManagement.set(4);
     * netConfig.preSharedKey = "12345678";
     *
     * </pre>
     *
     * @param wifiConfiguration passar null para manter as configurações atuais
     * @param enable            habilitar ou nao o hotspot/wifi ap
     * @throws Exception
     */
    @RequiresPermission(anyOf = {Manifest.permission.CHANGE_WIFI_STATE})
    public static void setWifiAPEnable(WifiManager wifi_manager, WifiConfiguration wifiConfiguration, boolean enable) throws Exception {
        wifi_manager.setWifiEnabled(false);

        if (enable && !getWifiAPConfiguration(wifi_manager).SSID.equals(wifiConfiguration.SSID)) {
            Method wifiApConfigurationMethod = wifi_manager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            wifiApConfigurationMethod.invoke(wifi_manager, getWifiAPConfiguration(wifi_manager), false);
        }

        Method wifiApConfigurationMethod = wifi_manager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
        wifiApConfigurationMethod.invoke(wifi_manager, wifiConfiguration, enable);
        wifi_manager.saveConfiguration();
    }

    /**
     * @param wifi_manager
     * @return
     * @throws Exception
     */
    public static @WifiAPState
    int getWifiAPState(WifiManager wifi_manager) throws Exception {
        Method wifiApState = wifi_manager.getClass().getMethod("getWifiApState");
        return (Integer) wifiApState.invoke(wifi_manager);
    }

    @RequiresPermission(anyOf = {Manifest.permission.CHANGE_WIFI_STATE})
    public static void connectAPAsync(final WifiManager wifiManager, final String ssid, final String pass) {
        new Thread("connectAPAsync") {
            @SuppressLint("MissingPermission")
            @Override
            public void run() {
                try {
                    connectAP(wifiManager, ssid, pass);
                } catch (Exception e) {
                    Log.e(LOG, "connectAP", e);
                }
            }
        }.start();
    }

    @RequiresPermission(anyOf = {Manifest.permission.CHANGE_WIFI_STATE})
    public static void connectAP(WifiManager wifiManager, final String ssid, final String pass) throws Exception {
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

        setWifiAPEnable(wifiManager, null, false);
        setWifiAPEnable(wifiManager, wc, true);
    }

    @RequiresPermission(anyOf = {Manifest.permission.CHANGE_WIFI_STATE})
    public static void shutdownAP(WifiManager wifiManager) throws Exception {
        setWifiAPEnable(wifiManager, null, false);
    }
}