package org.example;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import org.example.handler.MyCustomHandler;


public class NettyServer {


    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1); // 接收连接的线程组
        EventLoopGroup workerGroup = new NioEventLoopGroup(); // 处理连接的线程组
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch. pipeline().addLast("codec", new HttpServerCodec());
                        ch.pipeline().addLast("aggregator", new HttpObjectAggregator(512 * 1024));
                        // 在这里添加自定义的ChannelHandler
                        ch.pipeline().addLast("myHandler",new MyCustomHandler());
                    }
                });

        try {
            System.out.println("启动");
            b.bind(8080).sync().channel().closeFuture().await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
