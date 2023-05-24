package com.ht.demo;

import com.alibaba.fastjson2.JSON;
import com.ht.demo.pojo.ASRHttpResponse;
import com.ht.demo.pojo.FeedbackRequest;
import com.ht.demo.util.HttpClient;
import com.ht.demo.util.SignatureUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * 语音识别反馈 webapi 示例代码
 */
@Slf4j
public class FeedbackExample {

    private static final HttpClient HTTP_CLIENT = new HttpClient();

    public static void main(String[] args) throws IOException {
        String url = "https://k8s.arity.cn/asr/http/asr/V1/feedback";
        // 对应语音识别时传的的btId
        String btId = "btId(请替换为正确的btId)";
        // 对应语音识别返回的requestId
        String requestId = "requestId(请替换为正确的requestId)";
        // 是否识别准确 0: 准确 1: 不准确
        Integer exactType = 1;
        String accessKey = "accessKey(请替换为正确的accessKey)";
        String accessKeySecret = "accessKey(请替换为正确的accessKeySecret)";
        String appCode = "appCode(请替换为正确的appCode)";
        String channelCode = "channelCode(请替换为正确的channelCode)";
        CloseableHttpResponse httpResponse = HTTP_CLIENT.post(url, buildJsonParam(btId, requestId, exactType, accessKey,
                accessKeySecret, appCode, channelCode));
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            String responseBody = EntityUtils.toString(httpResponse.getEntity());
            if (responseBody == null || responseBody.length() == 0) {
                log.error("返回内容为空");
                return;
            }
            log.info("http application/json 返回内容：{}", responseBody);
            ASRHttpResponse response = JSON.parseObject(responseBody, ASRHttpResponse.class);
            if (response.getSuccess()) {
                log.info("反馈成功");
            } else {
                log.error("反馈异常: {}", response);
            }
        } else {
            log.error("请求异常, httpCode: {}", httpResponse.getStatusLine().getStatusCode());
        }
    }

    /**
     * 构建请求参数
     *
     * @param btId 业务方唯一标识id
     * @param requestId 请求标识
     * @param exactType 是否识别准确，0: 准确 1: 不准确
     * @param accessKey 密钥accessKey
     * @param accessKeySecret 密钥accessKeySecret
     * @param appCode 应用标识
     * @param channelCode 渠道标识
     * @return 请求参数
     */
    private static String buildJsonParam(String btId, String requestId, Integer exactType, String accessKey, String accessKeySecret, String appCode, String channelCode) {
        FeedbackRequest request = new FeedbackRequest();
        SignatureUtil.SignResult signResult = SignatureUtil.generateSignature(accessKey, accessKeySecret, appCode);
        request.setBtId(btId);
        request.setRequestId(requestId);
        request.setAccessKey(accessKey);
        request.setAppCode(appCode);
        request.setChannelCode(channelCode);
        request.setTimestamp(signResult.getTimestamp());
        request.setSign(signResult.getSignature());
        request.setExactType(exactType);
        return JSON.toJSONString(request);
    }
}
