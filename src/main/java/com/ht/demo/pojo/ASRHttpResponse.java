package com.ht.demo.pojo;

import lombok.Data;

@Data
public class ASRHttpResponse {

    private Boolean success;

    private String requestId;

    private Integer code;

    private String msg;

    private Data data;

    @lombok.Data
    public static class Data {

        private String btId;

        private String audioText;

        private Long audioTime;
    }
}
