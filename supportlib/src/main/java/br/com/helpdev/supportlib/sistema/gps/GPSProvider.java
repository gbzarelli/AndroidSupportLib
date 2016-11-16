package br.com.helpdev.supportlib.sistema.gps;

import android.os.Bundle;

/**
 * @author Guilherme Biff Zarelli
 */
public class GPSProvider {

    public static final int STATUS_NEW = -91;
    private String provider;
    private int status;
    private Bundle bundle;

    public GPSProvider() {
        this("");
    }

    public GPSProvider(String provider) {
        this(provider, -1);
    }

    public GPSProvider(String provider, int status) {
        this(provider, status, new Bundle());
    }

    public GPSProvider(String provider, int status, Bundle bundle) {
        this.provider = provider;
        this.status = status;
        this.bundle = bundle;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
