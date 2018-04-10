package br.com.grupocriar.swapandroid.telephony;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;

import br.com.grupocriar.swapandroid.io.network.NetworkUtils;

/**
 * Created by Guilherme Biff Zarelli on 26/08/16.
 */
public class SIMUtils {
    private SIMUtils() {
        throw new RuntimeException("No Simutils");
    }


    @SuppressLint("HardwareIds")
    @RequiresPermission(anyOf = {android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.READ_SMS,
            android.Manifest.permission.READ_PHONE_NUMBERS,
            android.Manifest.permission.ACCESS_NETWORK_STATE})
    public static ObSIM getSimInfo(Context context) {
        ObSIM obSIM = new ObSIM();
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (null == tm) throw new RuntimeException("No TelephonyManager service");
        obSIM.setLineNumber(tm.getLine1Number());//numero da linha
        obSIM.setSerialNumber(tm.getSimSerialNumber());//serial do SIM
        obSIM.setImei(tm.getDeviceId());//IMEI para GSM e MEID ou ESN para CDMA phones
        obSIM.setVersaoIMEI(tm.getDeviceSoftwareVersion()); //versao do IMEI
        obSIM.setNetworkOperator(tm.getNetworkOperatorName()); //Nome do operador de rede
        obSIM.setSimOperator(tm.getSimOperatorName()); //Nome do operador do SIM
        obSIM.setMobileNetworkActive(NetworkUtils.hasMobileNetworkAvailable(context));//diz se os dados moveis estao ativos
        return obSIM;
    }
}
