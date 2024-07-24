package com.gali.rpc.fault.tolerant;


import com.gali.rpc.model.GaliRpcResponse;

import java.util.Map;

/**
 * 快速失败 - 容错策略（立刻通知外层调用方）
 *
 */
public class FailFastTolerantStrategy implements TolerantStrategy {

    @Override
    public GaliRpcResponse doTolerant(Map<String, Object> context, Exception e) {
        throw new RuntimeException("服务报错", e);
    }
}
