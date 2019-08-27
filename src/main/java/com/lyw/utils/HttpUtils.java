package com.lyw.utils;

import com.sobte.cqp.jcq.event.JcqApp;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpUtils {

    private static final CloseableHttpClient httpClient = HttpClientBuilder.create().build();

    public static String get(String url) {
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(new HttpGet(url));
            return EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException e) {
            JcqApp.CQ.logError("hentai-bot", e.getMessage());
            return null;
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    JcqApp.CQ.logError("hentai-bot", e.getMessage());
                }
            }
        }
    }

}
