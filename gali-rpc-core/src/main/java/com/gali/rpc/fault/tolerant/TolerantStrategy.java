package com.gali.rpc.fault.tolerant;


import com.gali.rpc.model.GaliRpcResponse;

import java.util.Map;

/**
 * 容错策略
 *
 */
public interface TolerantStrategy {

    /**
     * 容错
     *
     * @param context 上下文，用于传递数据
     * @param e       异常
     * @return
     */
    GaliRpcResponse doTolerant(Map<String, Object> context, Exception e);
}
