package br.com.grupocriar.swapandroid.time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by demantoide on 30/09/16.
 */

public class TimeZone {
    private TimeZone() {
        throw new RuntimeException("No TimeZone!");
    }

    /**
     * TimeZone formato : '-03:00'
     *
     * @return
     */
    public static String getTimeZoneDifGMT() {
        Calendar calendar = Calendar.getInstance(java.util.TimeZone.getTimeZone("GMT"), Locale.getDefault());
        String timeZone = new SimpleDateFormat("Z", Locale.getDefault()).format(calendar.getTime());
        return timeZone.substring(0, 3) + ":" + timeZone.substring(3, 5);
    }

    /**
     * TimeZone formato: BRT -03:00 America/Sao Paulo
     *
     * @return
     */
    public static String getTimeZone() {
        java.util.TimeZone tz = java.util.TimeZone.getDefault();
        return tz.getDisplayName(false, java.util.TimeZone.SHORT) + " " + getTimeZoneDifGMT() + " - " + tz.getID();
    }

    public static boolean isTimeZoneCorrectConfig(String sUf) {
        String tz = java.util.TimeZone.getDefault().getID().toLowerCase();
        switch (sUf) {
            case "SE":
            case "PB":
            case "PE":
            case "PI":
            case "RN":
            case "MA":
            case "TO":
            case "CE":
            case "PA":
            case "AL":
            case "BA":
            case "AP":
                return tz.contains("argentina") || tz.contains("buenos_aires");
            case "AC":
            case "AM":
            case "RO":
            case "RR":
            case "MT":
            case "MS":
                return tz.contains("amazonas") || tz.contains("manaus");
            case "SP":
            case "SC":
            case "RS":
            case "DF":
            case "GO":
            case "MG":
            case "RJ":
            case "ES":
            case "PR":
                return tz.contains("brasilia") || tz.contains("sao_paulo") || tz.contains("brasília") || tz.contains("são_paulo") || tz.contains("são paulo");
        }
        return false;
    }

}
