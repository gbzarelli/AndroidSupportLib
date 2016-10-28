package br.com.helpdev.supportlib.sistema.telefonia;

import android.content.Context;
import android.telephony.TelephonyManager;

import br.com.grupocriar.swapandroid.sistema.rede.NetworkUtils;

/**
 * Created by demantoide on 26/08/16.
 */
public class SIMUtils {

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
