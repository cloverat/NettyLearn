package com.cloverat.netty.processCenter;

import com.cloverat.netty.Msg;
import org.springframework.stereotype.Component;

/**
 * 消息处理中心，处理入站信息，返回出站信息
 *
 * @author chenyujun
 * @date 18-4-3
 */
@Component
public class ProcessCenter implements Process {

    IombProcess iombProcess;

    /**
     * 对收到的报文进行处理,区分不同的报文类型,交给不同的报文处理器处理
     *
     * @param inboundMsg 入站报文
     * @return 出站报文
     */
    @Override
    public Msg process(Msg inboundMsg) {
        byte type = inboundMsg.getType();
        switch (type) {
            // 通用系统
            case 0x20:
                return iombProcess.process(inboundMsg);
            default:
                throw new UnsupportedOperationException("找不到对应的处理器");
        }
    }
}
