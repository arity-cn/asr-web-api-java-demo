package com.ht.demo.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * websocket 服务端返回报文状态枚举
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {

    SUCCESS("ok", "成功"),

    FAIL("failed", "失败"),

    ;

    // 编码
    private final String code;

    // 描述
    private final String name;
}
