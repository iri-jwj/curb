package jxpl.scnu.curb.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * created on ${date}
 *
 * @author iri-jwj
 * @version 1 init
 */
public class SSLFactory {
    private final static String CLIENT_PRI_KEY = "client";
    private final static String TRUSTSTORE_PUB_KEY = "turststore";
    private final static String CLIENT_BKS_PASSWORD = "c*9cZn$MA";
    private final static String TRUSTSTORE_BKS_PASSWORD = "c*9cZn$MA";
    private final static String KEYSTORE_TYPE = "BKS";
    private final static String PROTOCOL_TYPE = "TLS";
    private final static String CERTIFICATE_FORMAT = "X509";

    public static Map getSSLCertifcation(Context context) {
        SSLSocketFactory sslSocketFactory;
        SSLContext sslContext = null;
        Map<String, Object> map = new HashMap<>();
        try {
            // 服务器端需要验证的客户端证书，其实就是客户端的keystore
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);// 客户端信任的服务器端证书
            KeyStore trustStore = KeyStore.getInstance(KEYSTORE_TYPE);//读取证书
            InputStream ksIn = context.getAssets().open(CLIENT_PRI_KEY);
            InputStream tsIn = context.getAssets().open(TRUSTSTORE_PUB_KEY);//加载证书
            keyStore.load(ksIn, CLIENT_BKS_PASSWORD.toCharArray());
            trustStore.load(tsIn, TRUSTSTORE_BKS_PASSWORD.toCharArray());
            ksIn.close();
            tsIn.close();
            //初始化SSLContext
            sslContext = SSLContext.getInstance(PROTOCOL_TYPE);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(CERTIFICATE_FORMAT);
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(CERTIFICATE_FORMAT);
            trustManagerFactory.init(trustStore);

            X509TrustManager lc_x509TrustManager = (X509TrustManager) trustManagerFactory.getTrustManagers()[0];

            map.put("x509", lc_x509TrustManager);
            keyManagerFactory.init(keyStore, CLIENT_BKS_PASSWORD.toCharArray());
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
            map.put("ssl", sslContext.getSocketFactory());
        } catch (KeyStoreException para_e) {
            para_e.printStackTrace();
        } catch (IOException para_e) {
            para_e.printStackTrace();
        } catch (NoSuchAlgorithmException para_e) {
            para_e.printStackTrace();
        } catch (CertificateException para_e) {
            para_e.printStackTrace();
        } catch (UnrecoverableKeyException para_e) {
            para_e.printStackTrace();
        } catch (KeyManagementException para_e) {
            para_e.printStackTrace();
        }
        return map;
    }
}
