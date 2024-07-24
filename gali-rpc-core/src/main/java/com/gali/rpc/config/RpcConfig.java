package com.gali.rpc.config;

import com.gali.rpc.fault.retry.RetryStrategyKeys;
import com.gali.rpc.fault.tolerant.TolerantStrategyKeys;
import com.gali.rpc.loadbalancer.LoadBalancerKeys;
import com.gali.rpc.serializer.SerializeKeys;
import lombok.Data;

/**
 * RpcConfig:
 *
 * @author gali
 * @date 2024/06/20
 */
@Data
public class RpcConfig {
    /**
     * 名称
     */
    private String name = "gali-rpc";

    /**
     * 版本号
     */
    private String version = "1.0";

    /**
     * 服务器主机名
     */
    private String serverHost = "localhost";

    /**
     * 服务器端口号
     */
    private Integer port = 8080;

    /**
     * 模拟调用
     */
    private boolean mock = false;

    /**
     * 序列化器
     */
    private String serializer = SerializeKeys.JDK;

    /**
     * 注册中心
     */
    private RegistryConfig registryConfig = new RegistryConfig();

    /**
     * 负载均衡
     */
    private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;

    /**
     * 重试策略
     */
    private String retryStrategy = RetryStrategyKeys.NO;

    /**
     * 容错策略
     */
    private String tolerantStrategy = TolerantStrategyKeys.FAIL_FAST;
}
