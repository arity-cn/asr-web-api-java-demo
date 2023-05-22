package com.ht.demo.pojo;

import lombok.Data;

/**
 * websocket 客户端上传报文
 */
@Data
public class WebsocketClientFrame {

    /**
     * 参数说明：信号类型
     * 传入说明：必传
     * 规范：开始报文固定传start, 结束报文固定传end
     */
    private String signal;

    /**
     * 参数说明：业务参数
     * 传入说明：非必传
     * 规范：开始报文独有参数，结束报文勿传
     */
    private Business business;

    /**
     * 参数说明：业务数据流参数
     * 传入说明：非必传
     * 规范：开始报文独有参数，结束报文勿传
     */
    private AudioData data;

    @Data
    public static class Business {

        /**
         * 参数说明：静默检测阈值
         * 传入说明：非必传
         * 规范：用于设置端点检测的静默时间，单位是毫秒。即静默多长时间后引擎认为音频结束。默认5000
         */
        private Integer vadEos;
    }

    @Data
    public static class AudioData {

        /**
         * 参数说明：音频格式
         * 传入说明：非必传
         * 规范：可选值：PCM、WAV，默认PCM
         */
        private String audioFormatInfo;

        /**
         * 参数说明：采样率
         * 传入说明：非必传
         * 规范：可选值：SAMPLE_RATE_16K: 16k。默认值：SAMPLE_RATE_16K
         */
        private String sampleRate;
    }
}
