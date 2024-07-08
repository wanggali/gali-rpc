package com.gali.rpc.provider.service;

import com.gali.rpc.GaliRpcApplication;
import com.gali.rpc.config.RegistryConfig;
import com.gali.rpc.config.RpcConfig;
import com.gali.rpc.model.ServiceMetaInfo;
import com.gali.rpc.register.LocalRegister;
import com.gali.rpc.register.Registry;
import com.gali.rpc.register.RegistryFactory;
import com.gali.rpc.service.VertxHttpServer;
import com.rpc.common.service.UserService;

/**
 * EasyProviderMain:
 *
 * @author gali
 * @date 2024/06/20
 */
public class EasyProviderMain {
    //提供服务
    public static void main(String[] args) {
        GaliRpcApplication.init();

        //注册服务
        LocalRegister.register(UserService.class.getName(), UserServiceImpl.class);

        //注册服务到注册中心
        RpcConfig rpcConfig = GaliRpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(UserService.class.getName());
        serviceMetaInfo.setServiceVersion(rpcConfig.getVersion());
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getPort());
        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        VertxHttpServer vertxHttpServer = new VertxHttpServer();
        System.out.println(rpcConfig);
        vertxHttpServer.doStart(rpcConfig.getPort());
    }
}
