/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.helpdev.supportlib.sistema.chave;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.util.HashMap;

import br.com.helpdev.supportlib.sistema.seguranca.MD5;

/**
 * @author Guilherme Biff Zarelli
 */
public class ChaveSistema {

    private static HashMap<String, Integer> mapaLetras;

    public static String formataChave(String chave) {
        StringBuilder sb = new StringBuilder();
        int L = 5;
        for (int cont = 0; (cont + L) <= chave.length(); ) {
            sb.append(chave.substring(cont, cont + L));
            if ((cont + L) < chave.length()) {
                sb.append("-");
            }
            cont += L;
        }
        return sb.toString();
    }

    public static String getContraChave(String chave) throws Exception {
        ChaveSistema chaveSistema = new ChaveSistema();
        return chaveSistema.gerarContraChave(chave);
    }

    /**
     * Maneira mais precisa, até para dispositivos clonados,
     * porem ao formatar o dispositivo ou se houver uma atualização de
     * software a chave normalmente é alterada.
     *
     * @param activity
     * @return
     * @throws Throwable
     */
    public static String getChaveSegura(Context activity) throws Throwable {
        ChaveSistema chaveSistema = new ChaveSistema();

        String androidid = null;
        try {
            androidid = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Throwable t) {
        }

        StringBuilder preChave = new StringBuilder(chaveSistema.getInformacoesDispositivos(activity))
                .append(androidid == null ? "null" : androidid)
                .append(Build.TIME > 0 ? "null" : Build.TIME)
                .append(Build.DISPLAY == null ? "null" : Build.DISPLAY)
                .append(Build.HOST == null ? "null" : Build.HOST)
                .append(Build.FINGERPRINT == null ? "null" : Build.FINGERPRINT)
                .append(Build.BOOTLOADER == null ? "null" : Build.BOOTLOADER)
                .append(Build.VERSION.SDK_INT > 0 ? "null" : Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            preChave.append(Build.getRadioVersion() == null ? "null" : Build.getRadioVersion());
        }

        return chaveSistema.criptografarChave(preChave.toString());
    }

    /**
     * Maneira precisa, porem pega informações somente de hardware, não contempla
     * dispositivos clonados, porem garante a chave em caso de formatação.
     *
     * @param activity
     * @return
     * @throws Throwable
     */
    public static String getChave(Context activity) throws Throwable {
        ChaveSistema chaveSistema = new ChaveSistema();
        return chaveSistema.criptografarChave(chaveSistema.getInformacoesDispositivos(activity));
    }

    private ChaveSistema() {
        mapaLetras = new HashMap<String, Integer>();
        mapaLetras.put("A", 8);
        mapaLetras.put("B", 7);
        mapaLetras.put("C", 6);
        mapaLetras.put("D", 5);
        mapaLetras.put("E", 4);
        mapaLetras.put("F", 3);
        mapaLetras.put("G", 2);
        mapaLetras.put("H", 1);
        mapaLetras.put("I", 9);
        mapaLetras.put("J", 8);
        mapaLetras.put("K", 7);
        mapaLetras.put("L", 6);
        mapaLetras.put("M", 5);
        mapaLetras.put("N", 4);
        mapaLetras.put("O", 3);
        mapaLetras.put("P", 2);
        mapaLetras.put("Q", 1);
        mapaLetras.put("R", 9);
        mapaLetras.put("S", 8);
        mapaLetras.put("T", 7);
        mapaLetras.put("U", 6);
        mapaLetras.put("V", 5);
        mapaLetras.put("X", 4);
        mapaLetras.put("W", 3);
        mapaLetras.put("Y", 2);
        mapaLetras.put("Z", 1);
    }

    private String criptografarChave(String k) throws Throwable {
        StringBuilder chave = new StringBuilder();
        chave.append(MD5.toMD5(k));
        int tamanho = chave.length() / 2;
        chave.delete(0, 1);
        chave.delete(tamanho, tamanho + 1);
        return chave.toString().toUpperCase();
    }

    public String getInformacoesDispositivos(Context context) {
        StringBuilder preChave = new StringBuilder();
        TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        preChave.append("{\nGETDEVICEID:").append(tManager.getDeviceId() == null ? "null" : tManager.getDeviceId())
                .append(",\nBOARD:").append(Build.BOARD == null ? "null" : Build.BOARD)
                .append(",\nBRAND:").append(Build.BRAND == null ? "null" : Build.BRAND)
                .append(",\nDEVICE:").append(Build.DEVICE == null ? "null" : Build.DEVICE)
                .append(",\nID:").append(Build.ID == null ? "null" : Build.ID)
                .append(",\nMODEL:").append(Build.MODEL == null ? "null" : Build.MODEL)
                .append(",\nPRODUCT:").append(Build.PRODUCT == null ? "null" : Build.PRODUCT)
                .append(",\nTAGS:").append(Build.TAGS == null ? "null" : Build.TAGS)
                .append(",\nTYPE:").append(Build.TYPE == null ? "null" : Build.TYPE)
                .append(",\nUSER:").append(Build.USER == null ? "null" : Build.USER)
                .append(",\nHARDWARE:").append(Build.HARDWARE == null ? "null" : Build.HARDWARE)
                .append(",\nMANUFACTURER:").append(Build.MANUFACTURER == null ? "null" : Build.MANUFACTURER)
                .append(",\nSERIAL:").append(Build.SERIAL == null ? "null" : Build.SERIAL)
                .append("\n}");

        return preChave.toString();
    }

    private String gerarContraChave(String chave) throws Exception {
        return contraChaveProcesso(chave.substring(0, 5)) +
                contraChaveProcesso(chave.substring(5, 10)) +
                contraChaveProcesso(chave.substring(10, 15)) +
                contraChaveProcesso(chave.substring(15, 20)) +
                contraChaveProcesso(chave.substring(20, 25)) +
                contraChaveProcesso(chave.substring(25, 30));
    }

    private String contraChaveProcesso(String comp) {
        int v = 0;
        int res1 = 0;
        double t;
        double valor = 0;

        int op = 0; //0 = soma; 1 = multiplicacao
        int opOld = 0;//0 = soma; 1 = multiplicacao

        for (int i = 0; i < comp.length(); i++) {
            //Separa valor e operação
            if (Character.isDigit(comp.charAt(i))) {
                v = Integer.parseInt(comp.substring(i, i + 1));
                op = 0;
            } else if (Character.isLetter(comp.charAt(i))) {
                v = mapaLetras.get(comp.substring(i, i + 1));
                op = 1;
            }

            //Realiza Conta
            if (i == 0) {
                res1 = v;
            } else {
                if (opOld == 0) {
                    res1 += v;
                } else {
                    res1 = res1 * v;
                }
            }
            opOld = op;
        }

        comp = String.valueOf(res1);
        t = comp.length();

        for (int i = 0; i < t; i++) {
            valor += Integer.parseInt(comp.substring(i, i + 1));
        }

        double valor1 = valor / t;

        String str = Double.toString(valor1);

        return str.substring(0, str.indexOf("."));
    }

}
