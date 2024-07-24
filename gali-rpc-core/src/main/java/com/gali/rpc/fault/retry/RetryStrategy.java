package com.gali.rpc.fault.retry;


import com.gali.rpc.model.GaliRpcResponse;

import java.util.concurrent.Callable;

/**
 * 重试策略
 *
 */
public interface RetryStrategy {

    /**
     * 重试
     *
     * @param callable
     * @return
     * @throws Exception
     */
    GaliRpcResponse doRetry(Callable<GaliRpcResponse> callable) throws Exception;
}
