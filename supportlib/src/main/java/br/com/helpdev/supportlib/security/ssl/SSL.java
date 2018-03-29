package br.com.helpdev.supportlib.security.ssl;

import android.util.Log;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;


/**
 * Created by Guilherme Biff Zarelli on 15/01/16.
 */
public class SSL {
    private SSL() {
        throw new RuntimeException("No SSL!");
    }

    public static void allowAllSSL() {
        javax.net.ssl.HttpsURLConnection
                .setDefaultHostnameVerifier(new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });

        javax.net.ssl.SSLContext context = null;

        TrustManager[] trustManagers = new TrustManager[]{new FakeX509TrustManager()};

        try {
            context = javax.net.ssl.SSLContext.getInstance("TLS");
            context.init(null, trustManagers, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            Log.e("allowAllSSL", e.toString());
        } catch (KeyManagementException e) {
            Log.e("allowAllSSL", e.toString());
        }
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(context
                .getSocketFactory());
    }
}
