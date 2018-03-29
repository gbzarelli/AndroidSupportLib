package br.com.helpdev.supportlib.telephony;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;

import br.com.helpdev.supportlib.io.network.NetworkUtils;

/**
 * Created by Guilherme Biff Zarelli on 26/08/16.
 */
public class SIMUtils {
    private SIMUtils() {
        throw new RuntimeException("No Simutils");
    }

    @SuppressLint("MissingPermission")
    public static ObSIM getSimInfo(Context context) {
        ObSIM obSIM = new ObSIM();
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
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
