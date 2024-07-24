package com.gali.rpc.starter.annotation;

import com.gali.rpc.starter.bootstrap.RpcConsumerBootstrap;
import com.gali.rpc.starter.bootstrap.RpcInitBootstrap;
import com.gali.rpc.starter.bootstrap.RpcProviderBootstrap;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * EnableGaliRpc:
 *
 * @author gali
 * @date 2024/07/24
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcConsumerBootstrap.class, RpcInitBootstrap.class, RpcProviderBootstrap.class})
public @interface EnableGaliRpc {

    /**
     * 需要启动server
     */
    boolean needServer() default true;
}
