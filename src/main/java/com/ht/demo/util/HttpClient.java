package com.ht.demo.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.util.Map;

public class HttpClient {

    private final CloseableHttpClient httpClient;

    public HttpClient() {
        httpClient = HttpClients.createDefault();
    }

    public CloseableHttpResponse post(String url, String jsonParam) throws IOException {
        final HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(new BasicHeader("Content-Type", "application/json"));
        StringEntity entity = new StringEntity(jsonParam, ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);
        return httpClient.execute(httpPost);
    }

    public CloseableHttpResponse post(String url, Map<String, String> formData, byte[] file, String fileName) throws IOException {
        final HttpPost httpPost = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.RFC6532);
        for (Map.Entry<String, String> entry : formData.entrySet()) {
            builder.addTextBody(entry.getKey(), entry.getValue());
        }
        builder.addBinaryBody("file", file, ContentType.APPLICATION_OCTET_STREAM, fileName);
        HttpEntity httpEntity = builder.build();
        httpPost.setEntity(httpEntity);
        return httpClient.execute(httpPost);
    }
}
