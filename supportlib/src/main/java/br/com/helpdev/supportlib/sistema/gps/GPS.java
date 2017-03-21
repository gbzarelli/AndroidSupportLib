package br.com.helpdev.supportlib.sistema.gps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;

import java.security.Permission;
import java.security.SecurityPermission;
import java.util.HashMap;

import br.com.helpdev.supportlib.utils.GPSUtils;

/**
 * Permissions <uses-permission
 * android:name="android.permission.ACCESS_FINE_LOCATION"/>
 *
 * @author Guilherme Biff Zarelli
 */
public class GPS implements LocationListener {

    private int views;
    private Location location;
    private HashMap<String, GPSProvider> providers;
    private Context context;
    private LocationManager locationManager;

    /**
     * Para iniciar utilize o método loadGPS();
     * <p/>
     *
     * @param context
     */
    public GPS(Context context) throws RuntimeException {
        this.context = context;
        views = -1;
        providers = new HashMap<String, GPSProvider>();
        location = null;
    }

    @RequiresPermission(
            anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}
    )
    public void loadGPS() throws SecurityException {
        loadGPS(0, 0);
    }

    @RequiresPermission(
            anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}
    )
    public void loadGPS(int minTime, int minDistance) throws SecurityException {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, this);
    }

    public void close() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(this);
    }

    public boolean isGpsEnable() {
        return GPSUtils.isGpsEnable(locationManager);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        views = 0;
    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle extras) {
        GPSProvider provider = providers.get(arg0);

        if (provider == null) {
            provider = new GPSProvider(arg0);
        }

        provider.setStatus(arg1);
        provider.setBundle(extras);

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

    public void setContext(Context context) {
        this.context = context;
    }

    public int getViews() {
        return views;
    }

    public Location getLocation() {
        views++;
        return location;
    }

    public HashMap<String, GPSProvider> getProviders() {
        return providers;
    }

}
