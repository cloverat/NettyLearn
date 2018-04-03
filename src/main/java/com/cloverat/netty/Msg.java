package com.cloverat.netty;

import java.util.Arrays;

/**
 * @author chenyujun
 * @date 18-4-3
 */
public class Msg {

    private static final byte[] HEADER = new byte[]{-1, 0, 0};
    private byte type;
    private byte opt;
    private String msgStr;
    private byte[] bin;

    public Msg() {
    }

    public Msg(byte type, byte opt, String msgStr) {
        this.type = type;
        this.opt = opt;
        this.msgStr = msgStr;
    }

    public Msg(byte type, byte opt, String msgStr, byte[] bin) {
        this(type, opt, msgStr);
        this.bin = bin;
    }

    public static byte[] getHEADER() {
        return HEADER;
    }

    public byte getType() {
        return this.type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getOpt() {
        return this.opt;
    }

    public void setOpt(byte opt) {
        this.opt = opt;
    }

    public String getMsgStr() {
        return this.msgStr;
    }

    public void setMsgStr(String msgStr) {
        this.msgStr = msgStr;
    }

    public byte[] getBin() {
        return this.bin;
    }

    public void setBin(byte[] bin) {
        this.bin = bin;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "type=" + type +
                ", opt=" + opt +
                ", msgStr='" + msgStr + '\'' +
                ", bin=" + Arrays.toString(bin) +
                '}';
    }
}
