package com.ht.demo.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FrameTypeEnum {

    VERIFY("verify", "验证结果报文"),

    SERVER_READY("server_ready", "准备好进行语音识别报文"),

    PARTIAL_RESULT("partial_result", "中间识别结果报文"),

    FINAL_RESULT("final_result", "最终识别结果报文"),

    SPEECH_END("speech_end", "识别结束报文"),

    ;

    // 编码
    private final String code;

    // 名称
    private final String name;
}
