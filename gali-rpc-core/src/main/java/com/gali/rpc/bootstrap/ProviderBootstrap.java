package com.gali.rpc.bootstrap;

import com.gali.rpc.GaliRpcApplication;
import com.gali.rpc.config.RegistryConfig;
import com.gali.rpc.config.RpcConfig;
import com.gali.rpc.model.ServiceMetaInfo;
import com.gali.rpc.model.ServiceRegisterInfo;
import com.gali.rpc.register.LocalRegister;
import com.gali.rpc.register.Registry;
import com.gali.rpc.register.RegistryFactory;
import com.gali.rpc.service.tcp.VertxTcpServer;

import java.util.List;

/**
 * ProviderBootstrap:
 *
 * @author gali
 * @date 2024/07/24
 */
public class ProviderBootstrap {
    /**
     * 初始化
     */
    public static void init(List<ServiceRegisterInfo<?>> serviceRegisterInfoList) {
        // RPC 框架初始化（配置和注册中心）
        GaliRpcApplication.init();
        // 全局配置
        final RpcConfig rpcConfig = GaliRpcApplication.getRpcConfig();

        // 注册服务
        for (ServiceRegisterInfo<?> serviceRegisterInfo : serviceRegisterInfoList) {
            String serviceName = serviceRegisterInfo.getServiceName();
            // 本地注册
            LocalRegister.register(serviceName, serviceRegisterInfo.getImplClass());

            // 注册服务到注册中心
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getPort());
            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException(serviceName + " 服务注册失败", e);
            }
        }

        // 启动服务器
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(rpcConfig.getPort());
    }
}
