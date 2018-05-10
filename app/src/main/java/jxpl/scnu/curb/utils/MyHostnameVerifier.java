package jxpl.scnu.curb.utils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * created on ${date}
 *
 * @author iri-jwj
 * @version 1 init
 */
public class MyHostnameVerifier implements HostnameVerifier {
    @Override
    public boolean verify(String hostname, SSLSession session) {
        return session.isValid() && hostname.equals("39.108.105.150");
    }
}
