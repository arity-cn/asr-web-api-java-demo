package com.ht.demo.pojo;

import lombok.Data;

@Data
public class HttpApplicationJsonRequest {

    private String btId;

    private String accessKey;

    private String appCode;

    private String channelCode;

    private String contentType;

    private String formatInfo;

    private String content;

    private Long timestamp;

    private String sign;

    private String sampleRateEnum;
}
