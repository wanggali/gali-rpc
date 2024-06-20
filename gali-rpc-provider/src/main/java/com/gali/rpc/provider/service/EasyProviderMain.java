package com.gali.rpc.provider.service;

import com.gali.rpc.GaliRpcApplication;
import com.gali.rpc.config.RpcConfig;
import com.gali.rpc.register.LocalRegister;
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

        VertxHttpServer vertxHttpServer = new VertxHttpServer();
        System.out.println(GaliRpcApplication.getRpcConfig());
        vertxHttpServer.doStart(GaliRpcApplication.getRpcConfig().getPort());
    }
}
