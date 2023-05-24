package com.ht.demo.pojo;

import lombok.Data;

@Data
public class ASRHttpResponse {

    /**
     * 参数说明: 请求标识
     * 是否必返: 是
     * 规范: true：识别成功 false：识别失败
     */
    private Boolean success;

    /**
     * 参数说明: 请求标识
     * 是否必返: 是
     */
    private String requestId;

    /**
     * 参数说明: 请求返回码
     * 是否必返: 是
     * 规范: 20000为成功返回码
     */
    private Integer code;

    /**
     * 参数说明: 请求返回描述，和请求返回码对应
     * 是否必返: 是
     * 规范: 失败会返回错误原因
     */
    private String msg;

    /**
     * 参数说明: 请求返回明细数据
     * 是否必返: 否
     */
    private Data data;

    @lombok.Data
    public static class Data {

        /**
         * 参数说明: 业务方唯一标识id
         * 是否必返: 是
         */
        private String btId;

        /**
         * 参数说明: 整段音频转译文本结果
         * 是否必返: 是
         */
        private String audioText;

        /**
         * 参数说明: 整段音频的音频时长
         * 是否必返: 是
         * 规范: 单位秒
         */
        private Long audioTime;
    }
}
