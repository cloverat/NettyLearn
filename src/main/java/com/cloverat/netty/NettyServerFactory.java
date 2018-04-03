package com.cloverat.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Netty启动工厂
 *
 * @author chenyujun
 * @date 18-4-3
 */
public class NettyServerFactory {

    public static void create(EventLoopGroup bossGroup, EventLoopGroup workerGroup,
                              ChannelInitializer<SocketChannel> channelInitializer, int port) throws
            InterruptedException {
        // netty的初始化配置
        ServerBootstrap bootstrap = new ServerBootstrap();
        // 主线程组,工作线程组
        bootstrap.group(bossGroup, workerGroup)
                // nio异步io
                .channel(NioServerSocketChannel.class)
                // 通道初始化配置
                .childHandler(channelInitializer)
                // 最大连接数
                .option(ChannelOption.SO_BACKLOG, 1024)
                // option和childOption
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        
        bootstrap.bind(port).sync();
    }
}
