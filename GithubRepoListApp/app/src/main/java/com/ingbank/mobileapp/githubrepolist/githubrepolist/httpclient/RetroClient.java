package com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient;

import android.content.Context;
import android.content.ContextWrapper;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {

    private static Retrofit retrofit = null;

    private RetroClient() {
    }


    public static Retrofit getClient(Context applicationContext) {
        OkHttpClient okHttpClient = null;
        try {
            // loading CAs from an InputStream
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            ContextWrapper cs = new ContextWrapper(applicationContext);

            InputStream cert = cs.getAssets().open("api_github_cert.crt");
            Certificate ca;
            try {
                ca = cf.generateCertificate(cert);
            } finally {
                cert.close();
            }

            // creating a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // creating a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // creating an SSLSocketFactory that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
            okHttpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory())
                    .build();
        } catch (Exception exc) {
        }

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()

                    .baseUrl(HttpConstants.API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient)
                    .build();
        }
        return retrofit;
    }
}
