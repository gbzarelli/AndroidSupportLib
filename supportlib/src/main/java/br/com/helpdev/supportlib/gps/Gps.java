package br.com.helpdev.supportlib.gps;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;

import java.util.HashMap;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Permissions <uses-permission
 * android:name="android.permission.ACCESS_FINE_LOCATION"/>
 *
 * @author Guilherme Biff Zarelli
 */
public class Gps implements LocationListener {

    private int views;
    private Location locationGPS, locationNetwork;
    private HashMap<String, GPSProvider> providers;
    private LocationManager locationManager;

    @RequiresPermission(anyOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    public Gps(Context context) {
        views = -1;
        locationGPS = null;
        providers = new HashMap<>();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) {
            throw new RuntimeException("No LocationManager");
        }
        if (isGpsProviderEnable()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                    0, this);
        }
        if (isNetworkProviderEnable()) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                    0, this);
        }
    }

    @RequiresPermission(anyOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    public void close() {
        locationManager.removeUpdates(this);
    }

    public Location getLocationNetwork() {
        return locationNetwork;
    }

    public boolean isGpsProviderEnable() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public boolean isNetworkProviderEnable() {
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    @Override
    public void onLocationChanged(Location location) {
        if (LocationManager.GPS_PROVIDER.equals(location.getProvider())) {
            this.locationGPS = location;
        } else if (LocationManager.NETWORK_PROVIDER.equals(location.getProvider())) {
            this.locationNetwork = location;
        }
        views = 0;
    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        GPSProvider provider = providers.get(arg0);

        if (provider == null) {
            provider = new GPSProvider(arg0);
        }

        provider.setStatus(arg1);
        provider.setBundle(arg2);

        providers.put(arg0, provider);
    }

    @Override
    public void onProviderEnabled(String arg0) {
        GPSProvider provider = new GPSProvider(arg0, GPSProvider.STATUS_NEW);
        providers.put(arg0, provider);
    }

    @Override
    public void onProviderDisabled(String arg0) {
        providers.remove(arg0);
    }

    public int getViews() {
        return views;
    }

    public Location getLocationGPS() {
        return getLocationGPS(10_000, 10_000);
    }

    public Location getLocationGPS(long outdateGPSMillis, long outdateNetworkMillis) {
        views++;

        //Network esta desatualizado!.
        if (null != locationNetwork && System.currentTimeMillis() - locationNetwork.getTime() > outdateNetworkMillis) {
            if (null != locationGPS)//Tem localizacao, define null, se nao fica sem nenhum
                locationNetwork = null;
        }

        //Gps esta desatualizado!.
        if (null != locationGPS && System.currentTimeMillis() - locationGPS.getTime() > outdateGPSMillis) {
            if (null != locationNetwork)//Tem localizacao de rede, define null, se nao fica sem nenhum
                locationGPS = null;
        }

        if (locationGPS == null || locationNetwork == null) {
            return null != locationGPS ? locationGPS : locationNetwork;
        }

        //Se network estiver com o registro mais atualizado e melhor precisao
        if (locationNetwork.getTime() - locationGPS.getTime() > 0) {
            if (locationNetwork.getAccuracy() < locationGPS.getAccuracy()) {
                return locationNetwork;
            }
            //se o GPS estiver com o registro mais atualizado mas sem precisao
        } else if (locationNetwork.getAccuracy() < locationGPS.getAccuracy()) {
            return locationNetwork;
        }
        return locationGPS;
    }

    public HashMap<String, GPSProvider> getProviders() {
        return providers;
    }
}
