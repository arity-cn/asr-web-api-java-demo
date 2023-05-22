package com.ht.demo.listener;

import com.ht.demo.pojo.WebsocketServerFrame;

/**
 * websocket 服务端返回报文监听处理
 */
public interface WebSocketListener {

    /**
     * 处理验证结果报文
     *
     * @param serverFrame WebsocketServerFrame
     */
    void afterProcessVerify(WebsocketServerFrame serverFrame);

    /**
     * 处理准备好进行语音识别报文
     *
     * @param serverFrame WebsocketServerFrame
     */
    void afterProcessServerReady(WebsocketServerFrame serverFrame);

    /**
     * 处理中间识别结果报文
     *
     * @param serverFrame WebsocketServerFrame
     */
    void afterProcessPartialResult(WebsocketServerFrame serverFrame);

    /**
     * 处理最终识别结果报文
     *
     * @param serverFrame WebsocketServerFrame
     */
    void afterProcessFinalResult(WebsocketServerFrame serverFrame);

    /**
     * 处理识别结束报文
     *
     * @param serverFrame WebsocketServerFrame
     */
    void afterProcessSpeechEnd(WebsocketServerFrame serverFrame);
}
