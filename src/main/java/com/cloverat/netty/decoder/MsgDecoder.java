package com.cloverat.netty.decoder;

import com.cloverat.netty.Msg;
import com.cloverat.util.MsgCheckUtil;
import com.cloverat.util.MsgDataUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * 消息解码器，切记不可使用单例模式
 *
 * @author chenyujun
 * @date 18-4-3
 */
public class MsgDecoder extends LengthFieldBasedFrameDecoder {

    private static final int MAX_FRAME_LENGTH = 1024 * 1024;//1G
    private static final int LENGTH_FIELD_OFFSET = 3;
    private static final int LENGTH_FIELD_LENGTH = 4;

    public MsgDecoder() {
        super(MAX_FRAME_LENGTH, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH);
    }

    @Override
    protected Msg decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        //数据还不可读
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame == null) {
            return null;
        }
        //全部数据已准备完毕,长度正确性已校验

        //解析数据头
        byte[] readHeader = new byte[LENGTH_FIELD_OFFSET];
        frame.readBytes(LENGTH_FIELD_OFFSET).readBytes(readHeader);
        //数据头有误
        if (!Arrays.equals(readHeader, Msg.getHEADER())) {
            System.out.println("数据头有误");
            throw new IllegalArgumentException("数据头有误");
        }
        //数据长度部分
        int len = frame.readInt();
        //待校验信息
        byte[] data2check = new byte[len - 1];
        frame.getBytes(LENGTH_FIELD_OFFSET + LENGTH_FIELD_LENGTH,
                data2check, 0, len - 1);
        //解析数据体
        //消息类型
        byte type = frame.readByte();
        //操作类型
        byte opt = frame.readByte();
        //文本消息长度
        int msgLen = frame.readInt();
        //文本消息
        byte[] msg = new byte[msgLen];
        frame.readBytes(msgLen).readBytes(msg);
        //二进制数据
        int binLen = len - msgLen - 7;
        byte[] bin = null;
        if (binLen != 0) {
            bin = new byte[binLen];
            frame.readBytes(binLen).readBytes(bin);
        }
        //校验位
        byte check = frame.readByte();

        //冗余校验
        byte calValue = MsgCheckUtil.calculate(data2check);
        if (calValue != check) {
            System.out.println("校验值不匹配");
            throw new IllegalArgumentException("校验值不匹配");
        }

        //对msg解码
        byte[] decodedMsg = MsgDataUtil.decrypt(msg);
        if (decodedMsg == null) {
            System.out.println("解码出错");
            throw new IllegalArgumentException("解码出错");
        }
        String msgStr = new String(decodedMsg, Charset.forName("utf-8"));
        return new Msg(type, opt, msgStr, bin);
    }
}
