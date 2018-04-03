package com.cloverat.netty.initializer;

import com.cloverat.netty.decoder.MsgDecoder;
import com.cloverat.netty.encoder.MsgEncoder;
import com.cloverat.netty.handler.MsgHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 通道初始化信息，单例
 *
 * @author chenyujun
 * @date 18-4-3
 */
@Component
public class MyInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private MsgEncoder msgEncoder;
    @Autowired
    private MsgHandler msgHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        System.out.println("来自" + ch.remoteAddress().getAddress() + "的连接");

        ch.pipeline()
                // 设置空闲时间超时监控
                .addLast("idleStateHandler",
                        new IdleStateHandler(120, 120, 240))
                // 解码
                .addLast("decoder", new MsgDecoder())
                // 编码
                .addLast("encoder", msgEncoder)
                // 消息处理中心
                .addLast(msgHandler);
    }
}
