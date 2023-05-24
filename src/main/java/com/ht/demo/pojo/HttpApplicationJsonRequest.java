package com.ht.demo.pojo;

import lombok.Data;

@Data
public class HttpApplicationJsonRequest {

    /**
     * 参数说明：业务方的唯一标识id
     * 传入说明：必传
     * 规范：对应语音识别时传的的btId
     */
    private String btId;

    /**
     * 参数说明：密钥accessKey
     * 传入说明：必传
     */
    private String accessKey;

    /**
     * 参数说明：应用标识
     * 传入说明：必传
     * 规范：用于区分应用，可在管理后台的应用管理查看
     */
    private String appCode;

    /**
     * 参数说明：渠道标识
     * 传入说明：必传
     * 规范：用于区分渠道，可在管理后台的渠道管理查看
     */
    private String channelCode;

    /**
     * 参数说明：待识别音频内容的格式
     * 传入说明：必传
     * 规范：可选值：URL：识别内容为音频url地址；RAW：识别内容为音频的base64编码数据
     */
    private String contentType;

    /**
     * 参数说明：音频数据格式
     * 传入说明：特定条件必传
     * 规范：当音频内容格式为RAW时必传，
     * 可选值：PCM、WAV、MP3、SPEEX_WB(代表16K采样率的SPX格式)
     */
    private String formatInfo;

    /**
     * 参数说明：待识别的音频内容
     * 传入说明：必传
     * 规范：可以为url地址或者base64编码数据。
     */
    private String content;

    /**
     * 参数说明：当前时间戳
     * 传入说明：必传
     * 规范：毫秒级
     */
    private Long timestamp;

    /**
     * 参数说明：签名
     * 传入说明：必传
     * 规范：通过特定算法得到的签名，具体请看签名规则
     */
    private String sign;

    /**
     * 参数说明：采样率
     * 传入说明：非必传
     * 规范：可选值：SAMPLE_RATE_16K: 16k。默认值：SAMPLE_RATE_16K
     */
    private String sampleRateEnum;
}
