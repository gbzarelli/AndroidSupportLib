package br.com.helpdev.supportlib.security.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by Guilherme Biff Zarelli on 15/01/16.
 */
public class FakeX509TrustManager implements
        javax.net.ssl.X509TrustManager {
    private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[]{};

    FakeX509TrustManager() {
    }

    public void checkClientTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException {
    }

    public boolean isClientTrusted(X509Certificate[] chain) {
        return (true);
    }

    public boolean isServerTrusted(X509Certificate[] chain) {
        return (true);
    }

    public X509Certificate[] getAcceptedIssuers() {
        return (_AcceptedIssuers);
    }
}
