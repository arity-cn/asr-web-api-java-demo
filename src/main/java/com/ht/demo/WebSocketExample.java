package com.ht.demo;

import com.alibaba.fastjson2.JSON;
import com.ht.demo.enumeration.StatusEnum;
import com.ht.demo.listener.WebSocketListener;
import com.ht.demo.pojo.WebsocketServerFrame;
import com.ht.demo.pojo.WebsocketServerNbest;
import com.ht.demo.util.FileUtil;
import com.ht.demo.util.FrameUtil;
import com.ht.demo.util.SignatureUtil;
import com.ht.demo.util.WebSocketClient;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * ASR websocket api 示例代码
 */
@Slf4j
public class WebSocketExample {

    public static void main(String[] args) {
        String url = "wss://k8s.arity.cn/asr/ws";
        // 业务方唯一标识id，最高128位，建议不能重复，这里只是模拟
        String btId = "123";
        String accessKey = "accessKey(请替换为正确的accessKey)";
        String accessKeySecret = "accessKey(请替换为正确的accessKeySecret)";
        String appCode = "appCode(请替换为正确的appCode)";
        String channelCode = "channelCode(请替换为正确的channelCode)";
        // 语音识别结果
        StringBuffer result = new StringBuffer();
        String completeUrl = getWebSocketUrl(url, btId, accessKey, accessKeySecret, appCode, channelCode);
        log.info("构建参数后的url: {}", completeUrl);
        final WebSocketClient client = new WebSocketClient(completeUrl);
        WebSocketListener listener = new WebSocketListener() {
            @Override
            public void afterProcessVerify(WebsocketServerFrame serverFrame) {
                // 处理验证结果逻辑：验证成功发送开始报文，验证失败打印错误信息
                if (StatusEnum.SUCCESS.getCode().equals(serverFrame.getStatus())) {
                    log.error("校验通过，requestId: {}, code: {}", serverFrame.getRequestId(),
                            serverFrame.getCode());
                    client.sendMessage(FrameUtil.buildStartFrame());
                } else {
                    log.error("校验失败，requestId: {}, code: {}, message: {}", serverFrame.getRequestId(),
                            serverFrame.getCode(), serverFrame.getMessage());
                }
            }

            @Override
            public void afterProcessServerReady(WebsocketServerFrame serverFrame) {
                // 处理服务端准备好进行语音识别报文，发送二进制报文，发送完二进制报文后发送结束识别报文
                log.info("处理服务端准备好进行语音识别报文");
                if (StatusEnum.SUCCESS.getCode().equals(serverFrame.getStatus())) {
                    try {
                        List<ByteBuf> byteBufList = FileUtil.readFileToByteBuf("audio/BAC009S0002W0164.wav", 1024 * 10);
                        for (ByteBuf byteBuf : byteBufList) {
                            client.sendMessage(byteBuf);
                        }
                        // 发送结束识别报文
                        client.sendMessage(FrameUtil.buildEndFrame());
                    } catch (Exception e) {
                        log.error("发生异常", e);
                    }
                } else {
                    log.error("服务器准备失败, 报文{}", serverFrame);
                }
            }

            @Override
            public void afterProcessPartialResult(WebsocketServerFrame serverFrame) {
                log.info("处理中间结果报文");
                List bestList = JSON.parseObject(serverFrame.getNbest(), List.class);
                String sentence = JSON.parseObject(String.valueOf(bestList.get(0)), WebsocketServerNbest.class).getSentence();
                if (sentence.length() == 0) {
                    log.info("没有识别出结果，跳过此次中间结果报文处理");
                    return;
                }
                if (result.length() > 0) {
                    log.info("当前语音识别结果：{}", result + "，" + sentence);
                } else {
                    log.info("当前语音识别结果：{}", sentence);
                }
            }

            @Override
            public void afterProcessFinalResult(WebsocketServerFrame serverFrame) {
                log.info("处理最终结果报文");
                List bestList = JSON.parseObject(serverFrame.getNbest(), List.class);
                String sentence = JSON.parseObject(String.valueOf(bestList.get(0)), WebsocketServerNbest.class).getSentence();
                if (sentence.length() == 0) {
                    log.info("没有识别出结果，跳过此次最终结果报文处理");
                    return;
                }
                if (result.length() > 0) {
                    result.append("，");
                }
                result.append(sentence);
                log.info("当前语音识别结果：{}", result);
            }

            @Override
            public void afterProcessSpeechEnd(WebsocketServerFrame serverFrame) {
                log.info("收到识别结束：{}", serverFrame);
                try {
                    client.close();
                    // 因为时示例代码，所以结束后退出程序
                    System.exit(0);
                } catch (InterruptedException e) {
                    log.error("", e);
                }
            }
        };
        client.setListener(listener);
        client.connect();
    }

    /**
     * 构建请求参数
     *
     * @param websocketUrl websocketUrl
     * @param btId 业务方唯一标识id
     * @param accessKey 密钥accessKey
     * @param accessKeySecret 密钥accessKeySecret
     * @param appCode 应用标识
     * @param channelCode 渠道标识
     * @return 构建参数后的完整url
     */
    private static String getWebSocketUrl(String websocketUrl, String btId, String accessKey, String accessKeySecret, String appCode, String channelCode) {
        StringBuilder url = new StringBuilder(websocketUrl);
        SignatureUtil.SignResult signResult = SignatureUtil.generateSignature(accessKey, accessKeySecret, appCode);
        url.append("?accessKey=");
        url.append(accessKey);
        url.append("&appCode=");
        url.append(appCode);
        url.append("&channelCode=");
        url.append(channelCode);
        url.append("&btId=");
        url.append(btId);
        url.append("&sign=");
        url.append(signResult.getSignature());
        url.append("&timestamp=");
        url.append(signResult.getTimestamp());
        return url.toString();
    }
}
