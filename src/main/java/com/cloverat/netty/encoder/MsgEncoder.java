package com.cloverat.netty.encoder;

import com.cloverat.netty.Msg;
import com.cloverat.util.MsgCheckUtil;
import com.cloverat.util.MsgDataUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

/**
 * 消息编码器，使用单例模式
 *
 * @author chenyujun
 * @date 18-4-3
 */
@Component
@ChannelHandler.Sharable
public class MsgEncoder extends MessageToByteEncoder<Msg> {

    //  1  + 1  +  4    +  1  =   7
    // type opt  msgLen check 一共占据的字节长度
    private static final int OTHER_INFO_LENGTH = 7;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Msg msg, ByteBuf out) throws Exception {
        byte type = msg.getType();
        byte opt = msg.getOpt();
        String msgStr = msg.getMsgStr();
        byte[] bin = msg.getBin();

        byte[] msgBytes = msgStr.getBytes(Charset.forName("utf-8"));
        byte[] encodedMsgBytes = MsgDataUtil.encrypt(msgBytes);
        if (encodedMsgBytes == null) {
            throw new Exception("字符串数据加密异常");
        }
        // 文本消息长度
        int msgLength = encodedMsgBytes.length;
        // 总消息长度
        int allDataLength = msgLength + (bin == null ? 0 : bin.length) + OTHER_INFO_LENGTH;

        //输出数据
        out.writeBytes(Msg.getHEADER());//输出头部标识
        out.writeInt(allDataLength);//其后所有的数据长度(字节)
        out.writeByte(type);//数据类型
        out.writeByte(opt);//操作类型
        out.writeInt(encodedMsgBytes.length);//文本消息长度
        out.writeBytes(encodedMsgBytes);//信息字符串内容(已加密,字节)
        //二进制数据
        if (bin != null) {
            out.writeBytes(bin);
        }
        //输出校验值
        byte[] bytesToCheck = new byte[allDataLength - 1];
        out.getBytes(7, bytesToCheck);
        byte check = MsgCheckUtil.calculate(bytesToCheck);
        out.writeByte(check);//冗余校验值
    }
}
