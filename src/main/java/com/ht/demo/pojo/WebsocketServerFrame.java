package com.ht.demo.pojo;

import lombok.Data;

/**
 * websocket 服务端返回报文
 */
@Data
public class WebsocketServerFrame {

    /**
     * 参数说明：状态
     * 是否必返：是
     * 规范：ok: 成功 failed: 失败
     */
    private String status;

    /**
     * 参数说明：报文类型
     * 是否必返：是
     * 规范：
     * verify:验证结果报文
     * server_ready:准备好进行语音识别报文
     * partial_result:中间识别结果报文
     * final_result:最终识别结果报文
     * speech_end:识别结束报文
     */
    private String type;

    /**
     * 参数说明：请求标识
     * 是否必返：否
     * 规范：verify报文会返回，此次语音识别请求唯一标识
     */
    private String requestId;

    /**
     * 参数说明：请求返回码
     * 是否必返：否
     * 规范：verify报文会返回，20000为成功
     */
    private Integer code;

    /**
     * 参数说明：请求返回描述
     * 是否必返：否
     * 规范：verify报文，验证结果为失败时会返回，和请求返回码对应
     */
    private String message;

    /**
     * 参数说明：识别结果
     * 是否必返：否
     * 规范：partial_result和final_result报文会返回，注意里面是个json字符串，读取请转换为对象
     */
    private String nbest;
}
