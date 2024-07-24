package com.gali.rpc.starter.bootstrap;

import com.gali.rpc.GaliRpcApplication;
import com.gali.rpc.config.RpcConfig;
import com.gali.rpc.service.tcp.VertxTcpServer;
import com.gali.rpc.starter.annotation.EnableGaliRpc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Rpc 框架启动
 */
@Slf4j
public class RpcInitBootstrap implements ImportBeanDefinitionRegistrar {

    /**
     * Spring 初始化时执行，初始化 RPC 框架
     *
     * @param importingClassMetadata
     * @param registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 获取 EnableRpc 注解的属性值
        boolean needServer = (boolean) importingClassMetadata.getAnnotationAttributes(EnableGaliRpc.class.getName())
                .get("needServer");

        // RPC 框架初始化（配置和注册中心）
        GaliRpcApplication.init();

        // 全局配置
        final RpcConfig rpcConfig = GaliRpcApplication.getRpcConfig();

        // 启动服务器
        if (needServer) {
            VertxTcpServer vertxTcpServer = new VertxTcpServer();
            vertxTcpServer.doStart(rpcConfig.getPort());
        } else {
            log.info("不启动 server");
        }

    }
}
