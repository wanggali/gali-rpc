package com.gali.rpc.fault.tolerant;

import com.gali.rpc.model.GaliRpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 转移到其他服务节点 - 容错策略
 *
 */
@Slf4j
public class FailOverTolerantStrategy implements TolerantStrategy {

    @Override
    public GaliRpcResponse doTolerant(Map<String, Object> context, Exception e) {
        // todo 可自行扩展，获取其他服务节点并调用
        return null;
    }
}
