package com.cloverat.netty.processCenter;

import com.cloverat.netty.Msg;

/**
 * 处理器接口
 *
 * @author chenyujun
 * @date 18-4-3
 */
public interface Process {
    Msg process(Msg inboundMsg);
}
