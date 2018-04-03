package com.cloverat.netty.processCenter;

import com.cloverat.netty.Msg;
import org.springframework.stereotype.Component;

/**
 * iomb处理器
 *
 * @author chenyujun
 * @date 18-4-3
 */
@Component
public class IombProcess implements Process {

    @Override
    public Msg process(Msg inboundMsg) {
        byte opt = inboundMsg.getOpt();
        switch (opt) {
            case 0x10:
                // 业务逻辑
                // SystemSocketInfo systemInfo = systemService.getSystemSocketInfo();
                // String jsonString = JSON.toJSONString(systemInfo);
                // return new Msg(inboundMsg.getType(), inboundMsg.getOpt(), jsonString);
                return new Msg(inboundMsg.getType(), inboundMsg.getOpt(), "{}");
            case 0x20:
                return new Msg(inboundMsg.getType(), inboundMsg.getOpt(), "{}");
            default:
                throw new UnsupportedOperationException("找不到对应的处理方法");
        }
    }
}
