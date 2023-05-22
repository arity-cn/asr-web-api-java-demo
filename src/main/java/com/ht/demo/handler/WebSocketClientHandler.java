package com.ht.demo.handler;

import com.alibaba.fastjson2.JSON;
import com.ht.demo.enumeration.FrameTypeEnum;
import com.ht.demo.listener.WebSocketListener;
import com.ht.demo.pojo.WebsocketServerFrame;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {
    private WebSocketClientHandshaker handshaker;
    private ChannelPromise handshakeFuture;
    private final StringBuilder messageBuffer;
    private WebSocketListener listener = null;

    public WebSocketClientHandler() {
        this.messageBuffer = new StringBuilder();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        handlerAdded(ctx);
        log.info("websocket建立连接完成");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.info("连接断开");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 握手协议返回，设置结束握手
        if (!this.handshaker.isHandshakeComplete()) {
            FullHttpResponse response = (FullHttpResponse) msg;
            this.handshaker.finishHandshake(ctx.channel(), response);
            this.handshakeFuture.setSuccess();
            log.info("握手成功");
            return;
        }

        WebSocketFrame webSocketFrame = (WebSocketFrame) msg;
        if (webSocketFrame instanceof TextWebSocketFrame || webSocketFrame instanceof ContinuationWebSocketFrame) {
            String message = ((TextWebSocketFrame) webSocketFrame).text();
            messageBuffer.append(message);

            if (webSocketFrame.isFinalFragment()) {
                // 处理完整的消息
                String completeMessage = messageBuffer.toString();
                log.info("收到消息: {}", completeMessage);
                messageBuffer.setLength(0); // 清空消息缓冲区

                // 消息分发
                WebsocketServerFrame websocketServerFrame = JSON.parseObject(completeMessage, WebsocketServerFrame.class);
                if (FrameTypeEnum.VERIFY.getCode().equals(websocketServerFrame.getType())) {
                    if (listener != null) {
                        listener.afterProcessVerify(websocketServerFrame);
                    }
                } else if (FrameTypeEnum.SERVER_READY.getCode().equals(websocketServerFrame.getType())) {
                    if (listener != null) {
                        listener.afterProcessServerReady(websocketServerFrame);
                    }
                } else if (FrameTypeEnum.PARTIAL_RESULT.getCode().equals(websocketServerFrame.getType())) {
                    if (listener != null) {
                        listener.afterProcessPartialResult(websocketServerFrame);
                    }
                } else if (FrameTypeEnum.FINAL_RESULT.getCode().equals(websocketServerFrame.getType())) {
                    if (listener != null) {
                        listener.afterProcessFinalResult(websocketServerFrame);
                    }
                } else if (FrameTypeEnum.SPEECH_END.getCode().equals(websocketServerFrame.getType())) {
                    if (listener != null) {
                        listener.afterProcessSpeechEnd(websocketServerFrame);
                    }
                }
            }
        } else if (webSocketFrame instanceof BinaryWebSocketFrame) {
            BinaryWebSocketFrame binaryFrame = (BinaryWebSocketFrame) msg;
            // 处理二进制消息逻辑
        } else if (webSocketFrame instanceof PongWebSocketFrame) {
            // 处理心跳消息逻辑
        } else if (webSocketFrame instanceof CloseWebSocketFrame) {
            // 处理WebSocket连接关闭消息逻辑
            log.info("收到关闭消息");
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("客户端抛异常：", cause);
        ctx.close();
    }

    public WebSocketClientHandshaker getHandshaker() {
        return handshaker;
    }

    public void setListener(WebSocketListener listener) {
        this.listener = listener;
    }

    public void setHandshaker(WebSocketClientHandshaker handshaker) {
        this.handshaker = handshaker;
    }

    public void handlerAdded(ChannelHandlerContext ctx) {
        this.handshakeFuture = ctx.newPromise();
    }

    public ChannelFuture handshakeFuture() {
        return this.handshakeFuture;
    }
}
