package br.com.helpdev.supportlib.sistema.gps;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.HashMap;

/**
 * Permissions <uses-permission
 * android:name="android.permission.ACCESS_FINE_LOCATION"/>
 *
 * @author Guilherme Biff Zarelli
 */
public class Gps implements LocationListener {

	private int views;
	private Location location;
	private HashMap<String, GPSProvider> providers;
	private Context context;
	private LocationManager locationManager;

	public Gps(Context c, boolean alerta) {
		this.context = c;
		views = -1;
		location = null;
		providers = new HashMap<String, GPSProvider>();
		locationManager = (LocationManager) c
				.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, this);
	}

	public void close() {
		locationManager.removeUpdates(this);
	}

	public boolean isGpsEnable() {
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}


	@Override
	public void onLocationChanged(Location location) {
		this.location = location;
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

	public Location getLocation() {
		views++;
		return location;
	}

	public HashMap<String, GPSProvider> getProviders() {
		return providers;
	}
}
