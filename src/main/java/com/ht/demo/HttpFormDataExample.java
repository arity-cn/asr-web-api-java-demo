package com.ht.demo;

import com.alibaba.fastjson2.JSON;
import com.ht.demo.pojo.ASRHttpResponse;
import com.ht.demo.util.FileUtil;
import com.ht.demo.util.HttpClient;
import com.ht.demo.util.SignatureUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * http form-data webapi 示例代码
 */
@Slf4j
public class HttpFormDataExample {

    private static final HttpClient HTTP_CLIENT = new HttpClient();

    public static void main(String[] args) throws IOException {
        String url = "https://k8s.arity.cn/asr/http/asr/toTextBinary";
        // 业务方唯一标识id，最高128位，建议不要重复，这里只是模拟
        String btId = "123";
        String accessKey = "accessKey(请替换为正确的accessKey)";
        String accessKeySecret = "accessKey(请替换为正确的accessKeySecret)";
        String appCode = "appCode(请替换为正确的appCode)";
        String channelCode = "channelCode(请替换为正确的channelCode)";
        Map<String, String> formData = buildFormData(btId, accessKey, accessKeySecret, appCode, channelCode);
        byte[] file = FileUtil.readFileToBytes("audio/BAC009S0002W0164.wav");
        CloseableHttpResponse httpResponse = HTTP_CLIENT.post(url, formData, file, "BAC009S0002W0164.wav");
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            String responseBody = EntityUtils.toString(httpResponse.getEntity());
            if (responseBody == null || responseBody.length() == 0) {
                log.error("返回内容为空");
                return;
            }
            log.info("http application/json 返回内容：{}", responseBody);
            ASRHttpResponse response = JSON.parseObject(responseBody, ASRHttpResponse.class);
            if (response.getSuccess()) {
                log.info("语音识别结果: {}", response.getData().getAudioText());
            } else {
                log.error("请求异常: {}", response);
            }
        } else {
            log.error("请求异常, httpCode: {}", httpResponse.getStatusLine().getStatusCode());
        }
    }

    private static Map<String, String> buildFormData(String btId, String accessKey, String accessKeySecret, String appCode, String channelCode) {
        Map<String, String> formData = new HashMap<>();
        SignatureUtil.SignResult signResult = SignatureUtil.generateSignature(accessKey, accessKeySecret, appCode);
        formData.put("btId", btId);
        formData.put("accessKey", accessKey);
        formData.put("appCode", appCode);
        formData.put("channelCode", channelCode);
        formData.put("timestamp", signResult.getTimestamp().toString());
        formData.put("sign", signResult.getSignature());
        formData.put("sampleRateEnum", "SAMPLE_RATE_16K");
        return formData;
    }
}
