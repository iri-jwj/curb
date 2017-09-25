package jxpl.scnu.curb.utils;

import android.content.SyncRequest;
import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.ContentCodingType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by irijwj on 2017/9/5.
 * all great things are simple,
 * and many can be expressed in single words:
 * freedom, justice, honor, duty, mercy, hope.
 * --Winsdon Churchill
 */

public class RestTemplateManager <T> extends AsyncTask<Void,Void,Class<T>> {
    private static final int CONNECT_TIMEOUT = 15000;// 30秒
    private static final int READ_TIMEOUT = 10000;// 15秒
    private static final String TAG = "RestTemplateManager";
    private static SimpleClientHttpRequestFactory simpleFactory;

    private static HttpHeaders httpHeaders = new HttpHeaders();
    static {
        simpleFactory = new SimpleClientHttpRequestFactory();
        simpleFactory.setConnectTimeout(CONNECT_TIMEOUT);
        simpleFactory.setReadTimeout(READ_TIMEOUT);
    }

    /**
     * 请求头里面加入验证字符串
     *
     * @param extra ??
     */
    public static void setHttpHeaders(String extra) {
        httpHeaders.clear();
        httpHeaders.add("verify", extra);
        httpHeaders.setAcceptEncoding(ContentCodingType.GZIP);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    }

    public static void setHeadersAcceptImage(){
        httpHeaders.clear();
        httpHeaders.setAcceptEncoding(ContentCodingType.GZIP);
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
    }

    private static ClientHttpRequestFactory getRequestFactory() {
        return simpleFactory;
    }

    /**
     * POST请求
     *
     * @param url 目的地址
     * @param msg 待发送信息
     * @param clazz 类
     * @return 返回
     */

    public static <R, M> R postWithJSON(String url, M msg, Class<R> clazz) {
        try {
            Log.v(TAG, url);

            HttpEntity<M> requestEntity = new HttpEntity<>(msg, httpHeaders);
            RestTemplate restTemplate = new RestTemplate(getRequestFactory());
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<R> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, clazz);
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <R> R getWithJson(String url, Class<R> clazz, Object... uriVariables) {
        try {
            url = spliceRestURL(url, uriVariables);
            Log.v(TAG, url);
            HttpEntity<?> requestEntity = new HttpEntity<Object>(httpHeaders);
            RestTemplate restTemplate = new RestTemplate(getRequestFactory());
            ResponseEntity<R> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, clazz,
                    uriVariables);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 拼接rest风格的URL，注意参数的顺序
     */
    private static String spliceRestURL(String prefix, Object... params) {
        StringBuilder builder = new StringBuilder(prefix);
        for (Object p : params) {
            builder.append("/").append(p);
        }
        return builder.toString();
    }


    @Override
    public Class<T> doInBackground(Void...Params){

    }

}
