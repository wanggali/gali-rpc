package com.gali.rpc;

import com.gali.rpc.config.RegistryConfig;
import com.gali.rpc.config.RpcConfig;
import com.gali.rpc.constant.RpcConstant;
import com.gali.rpc.register.Registry;
import com.gali.rpc.register.RegistryFactory;
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
        registerInit(newRpcConfig);
    }

    private static void registerInit(RpcConfig newRpcConfig) {
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info("registry init,config: {}", newRpcConfig);

        //jvm关闭前下线服务
        Runtime.getRuntime().addShutdownHook(new Thread(registry::destroy));

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
     *
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
