package br.com.grupocriar.swapandroid.gps;

/**
 * Created by Guilherme Biff Zarelli on 15/03/16.
 */
public final class GPSUtils {
    private GPSUtils() {
        throw new RuntimeException("No GPSUtils!");
    }

    /**
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return value in meters
     */
    public static double calculateDistanceMeters(double lat1, double lng1, double lat2, double lng2) {
        return calculateDistance(lat1, lng1, lat2, lng2) * 1000;
    }

    /**
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return value in kilometers
     */
    public static double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        //double earthRadius = 3958.75;//miles
        double earthRadius = 6371;//kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c;
    }
}
