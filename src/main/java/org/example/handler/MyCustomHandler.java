package org.example.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

import javax.sound.midi.Soundbank;
import java.util.logging.Logger;


public class MyCustomHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = Logger.getLogger(MyCustomHandler.class.getName());


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println( msg instanceof HttpRequest);
        if (msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;

            // 获取 URL
            String uri = req.uri();
            logger.info("Received URL: " + uri);
            HttpMethod method = req.method();
            logger.info("Received HTTP method: " + method.name());
            // 获取 Headers
            HttpHeaders headers = req.headers();
//            System.out.println();
            headers.entries().forEach(System.out::println);
            // 例如，打印所有 headers
//            headers.forEach((headerName, headerValues) -> headerValues.forEach(value -> logger.info(headerName + ": " + value)));

            // 检查请求是否包含内容
            if (req instanceof HttpContent) {
                HttpContent httpContent = (HttpContent) msg;
                ByteBuf buf = httpContent.content();
                if (buf.isReadable()) {
                    // 获取请求体
                    String requestBody = buf.toString(io.netty.util.CharsetUtil.UTF_8);
                    logger.info("Received Request Body: " + requestBody);
                }
            }
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            response.content().writeBytes("API响应数据".getBytes());
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }
        // 处理接收到的消息
        // msg 是从 Channel 中读取的数据
        // 可以在这里实现你的业务逻辑
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 捕获并处理异常
        cause.printStackTrace();
        ctx.close(); // 关闭连接
    }
}