package jxpl.scnu.curb.utils;

import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * created on ${date}
 *
 * @author iri-jwj
 * @version 1 init
 */
public class MyTrustManage implements X509TrustManager {
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) {

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
