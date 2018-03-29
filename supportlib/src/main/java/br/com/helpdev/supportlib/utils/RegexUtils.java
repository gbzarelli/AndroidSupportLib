package br.com.grupocriar.swapandroid.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Guilherme Biff Zarelli on 06/12/16.
 */

public class RegexUtils {
    private RegexUtils() {
        throw new RuntimeException("No RegexUtils!");
    }

    public static final String REGEX_RENACH_TESTES = "^[a-zA-Z]{2}([1]{4}[0-9]{5})$";
    public static final String REGEX_NUMEROS_IGUAIS = "^(\\d)\\1*$";

    public static String extractValue(String regex, String text) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            text = matcher.group(0);
        }
        return text;
    }
}
