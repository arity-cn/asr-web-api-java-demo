package com.ht.demo.pojo;

import lombok.Data;

@Data
public class FeedbackRequest {

    /**
     * 参数说明：业务方的唯一标识id
     * 传入说明：必传
     * 规范：对应语音识别时传的的btId
     */
    private String btId;

    /**
     * 参数说明：请求标识
     * 传入说明：必传
     * 规范：对应语音识别返回的requestId
     */
    private String requestId;

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
     * 参数说明：是否识别准确
     * 传入说明：必传
     * 规范：0: 准确 1: 不准确
     */
    private Integer exactType;
}
