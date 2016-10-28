package br.com.helpdev.supportlib.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.text.TextUtils;

import java.text.Normalizer;

/**
 * Created by demantoide on 11/03/16.
 */
public class StringUtils {
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static String removeAccentuation(String texto) {
        if(TextUtils.isEmpty(texto)) {
           return "";
        }
        return Normalizer.normalize(new StringBuilder(texto), Normalizer.Form.NFKD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    public static String removeNonASCII(String texto) {
        return texto.replaceAll("[^\\x00-\\x7F]", "");
    }

    public static String removeUselessSpaces(String texto) {
        return texto.replaceAll("^ +| +$| (?= )", "");
    }

    public static String formatCPF(String cpf) {
        if (cpf.length() == 11) {
            cpf = String.format("%s.%s.%s-%s",
                    cpf.substring(0, 3),
                    cpf.substring(3, 6),
                    cpf.substring(6, 9),
                    cpf.substring(9, 11)
            );
        }
        return cpf;
    }
}
