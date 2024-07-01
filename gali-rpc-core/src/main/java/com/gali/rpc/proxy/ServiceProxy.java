package com.gali.rpc.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.gali.rpc.GaliRpcApplication;
import com.gali.rpc.config.RpcConfig;
import com.gali.rpc.model.GaliRpcRequest;
import com.gali.rpc.model.GaliRpcResponse;
import com.gali.rpc.serializer.JdkSerializer;
import com.gali.rpc.serializer.Serializer;
import com.gali.rpc.serializer.SerializerFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * ServiceProxy: JDK代理
 *
 * @author gali
 * @date 2024/06/20
 */
@Slf4j
public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //指定序列化
        RpcConfig rpcConfig = GaliRpcApplication.getRpcConfig();
        Serializer jdkSerializer = SerializerFactory.getInstance(rpcConfig.getSerializer());

        //构造请求
        GaliRpcRequest request = GaliRpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        try {
            byte[] serialize = jdkSerializer.serialize(request);
            //发送请求
            HttpResponse execute = HttpRequest.post("http://localhost:8080")
                    .body(serialize)
                    .execute();
            byte[] bytes = execute.bodyBytes();
            GaliRpcResponse deserialize = jdkSerializer.deserialize(bytes, GaliRpcResponse.class);
            execute.close();
            return deserialize.getData();
        } catch (Exception e) {
            log.error("proxy error", e);
        }
        return null;
    }
}
