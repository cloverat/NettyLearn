package com.cloverat.netty.server;

import com.cloverat.netty.NettyServerFactory;
import com.cloverat.netty.initializer.MyInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author chenyujun
 * @date 18-4-3
 */
@Component
public class NettyServer {

    @Autowired
    private MyInitializer myInitializer;

    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();
    private int port = 5151;

    @PostConstruct
    private void run() {
        try {
            NettyServerFactory.create(bossGroup, workerGroup, myInitializer, port);
            System.out.println("Netty启动成功,绑定端口:" + port);
        } catch (InterruptedException e) {
            System.out.println("Netty启动过程被中断");
        }
    }
}
