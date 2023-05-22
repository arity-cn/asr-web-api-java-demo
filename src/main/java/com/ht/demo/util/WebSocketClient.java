package com.ht.demo.util;

import com.ht.demo.handler.WebSocketClientHandler;
import com.ht.demo.listener.WebSocketListener;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

/**
 * websocket 客户端
 */
@Slf4j
public class WebSocketClient {
    private final String url;
    private final WebSocketClientHandler handler;
    private Channel channel;
    private static final EventLoopGroup GROUP = new NioEventLoopGroup();

    public WebSocketClient(String url) {
        this.url = url;
        this.handler = new WebSocketClientHandler();
    }

    public void setListener(WebSocketListener listener) {
        this.handler.setListener(listener);
    }

    public void connect() {
        try {
            SslContext sslContext = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(GROUP)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(sslContext.newHandler(ch.alloc()));
                            // 添加一个http的编解码器
                            pipeline.addLast(new HttpClientCodec());
//                            // 添加一个用于支持大数据流的支持
                            pipeline.addLast(new ChunkedWriteHandler());
                            // 添加一个聚合器，这个聚合器主要是将HttpMessage聚合成FullHttpRequest/Response
                            pipeline.addLast(new HttpObjectAggregator(1024 * 64));
                            pipeline.addLast(handler);
                        }
                    });
            HttpHeaders headers = new DefaultHttpHeaders();
            headers.set("Origin", "null");  // 将Origin字段设置为null
            WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(new URI(this.url),
                    WebSocketVersion.V13, (String) null, true, headers);
            ChannelFuture future = bootstrap.connect(URI.create(url).getHost(), getPort(URI.create(url))).sync();
            this.channel = future.channel();
            handler.setHandshaker(handshaker);
            handshaker.handshake(channel);
            //阻塞等待是否握手成功
            handler.handshakeFuture().sync();
            log.info("握手成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        channel.writeAndFlush(new TextWebSocketFrame(message));
    }

    public void sendMessage(ByteBuf binaryData) {
        channel.writeAndFlush(new BinaryWebSocketFrame(binaryData));
    }

    public void close() throws InterruptedException {
        channel.close().sync();
    }

    private int getPort(URI uri) {
        int port = uri.getPort();
        if (port == -1) {
            if ("wss".equalsIgnoreCase(uri.getScheme())) {
                return 443;
            } else if ("ws".equalsIgnoreCase(uri.getScheme())) {
                return 80;
            }
        }
        return port;
    }
}

