package com.cloverat.util;

/**
 * @author chenyujun
 * @date 18-4-3
 */
public class MsgCheckUtil {

    /**
     * 冗余校验
     *
     * @param data 待校验数据
     * @return 冗余校验值
     */
    public static byte calculate(byte[] data) {
        byte result = (byte) 0x00;
        for (byte aData : data) {
            result = (byte) (result ^ aData);
        }
        return result;
    }
}
