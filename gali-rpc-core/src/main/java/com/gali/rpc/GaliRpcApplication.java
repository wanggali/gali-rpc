package com.gali.rpc;

import com.gali.rpc.config.RpcConfig;
import com.gali.rpc.constant.RpcConstant;
import com.gali.rpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * GaliRpcApplication: 经典单例模式-双重检锁
 *
 * @author gali
 * @date 2024/06/20
 */
@Slf4j
public class GaliRpcApplication {
    private static volatile RpcConfig rpcConfig;

    private static void init(RpcConfig newRpcConfig) {
        rpcConfig = newRpcConfig;
        log.info("rpc init,config: {}", newRpcConfig);
    }

    public static void init() {
        RpcConfig newRpcConfig;

        try {
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    /**
     * 获取配置
     * @return
     */
    public static RpcConfig getRpcConfig() {
        if (Objects.isNull(rpcConfig)) {
            synchronized (GaliRpcApplication.class) {
                if (Objects.isNull(rpcConfig)) {
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
