package com.cloverat.netty.handler;

import com.alibaba.fastjson.JSON;
import com.cloverat.netty.Msg;
import com.cloverat.netty.processCenter.ProcessCenter;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 消息处理器，使用单例模式
 *
 * @author chenyujun
 * @date 18-4-3
 */
@Component
@ChannelHandler.Sharable
public class MsgHandler extends SimpleChannelInboundHandler<Msg> {

    private static final Msg serverErrorMsg =
            new Msg((byte) 0x00, (byte) 0x66, JSON.toJSONString(new ErrorMsg("服务端未知异常")));

    @Autowired
    private ProcessCenter processCenter;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Msg msg) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println("入站消息:" + msg.toString());

        //处理Msg返回结果
        Msg result = processCenter.process(msg);
        System.out.println("出站消息:" + result.toString());

        //输出结果
        incoming.write(result);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // ctx.flush();
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println("发生异常" + cause);
        if (cause instanceof IllegalArgumentException) {
            incoming.write(new Msg((byte) 0x00, (byte) 0x65, JSON.toJSONString(new ErrorMsg(cause.getMessage()))));
        } else {
            incoming.write(serverErrorMsg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                System.out.println("read idle");
            } else if (event.state() == IdleState.WRITER_IDLE) {
                System.out.println("write idle");
            } else if (event.state() == IdleState.ALL_IDLE) {
                System.out.println("all idle");
            }
        }
    }
}
