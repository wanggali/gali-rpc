package com.gali.rpc.service;

import com.gali.rpc.GaliRpcApplication;
import com.gali.rpc.config.RpcConfig;
import com.gali.rpc.model.GaliRpcRequest;
import com.gali.rpc.model.GaliRpcResponse;
import com.gali.rpc.register.LocalRegister;
import com.gali.rpc.serializer.JdkSerializer;
import com.gali.rpc.serializer.Serializer;
import com.gali.rpc.serializer.SerializerFactory;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * HttpServerHandler:
 *
 * @author gali
 * @date 2024/06/20
 */
@Slf4j
public class HttpServerHandler implements Handler<HttpServerRequest> {
    @Override
    public void handle(HttpServerRequest httpServerRequest) {
        //指定序列化
        RpcConfig rpcConfig = GaliRpcApplication.getRpcConfig();
        Serializer jdkSerializer = SerializerFactory.getInstance(rpcConfig.getSerializer());

        //记录日志
        System.out.println("received request " + httpServerRequest.method() + "" + httpServerRequest.uri());

        //异步处理HTTP请求
        httpServerRequest.bodyHandler(body -> {
            byte[] bytes = body.getBytes();
            GaliRpcRequest rpcRequest = null;

            try {
                rpcRequest = jdkSerializer.deserialize(bytes, GaliRpcRequest.class);
            } catch (IOException e) {
                log.error("Failed to deserialize");
            }

            GaliRpcResponse galiRpcResponse = new GaliRpcResponse();
            if (Objects.isNull(rpcRequest)) {
                galiRpcResponse.setMessage("rpcRequest is null");
                doResponse(httpServerRequest, galiRpcResponse, jdkSerializer);
                return;
            }

            try {
                Class<?> aClass = LocalRegister.get(rpcRequest.getServiceName());
                Method method = aClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object invoke = method.invoke(aClass.newInstance(), rpcRequest.getArgs());
                galiRpcResponse.setData(invoke);
                galiRpcResponse.setMessage("success");
                galiRpcResponse.setDataType(method.getReturnType());
            } catch (Exception e) {
                log.error("Failed to get method ,request:{}", rpcRequest);
            }

            doResponse(httpServerRequest, galiRpcResponse, jdkSerializer);
        });
    }

    private void doResponse(HttpServerRequest request, GaliRpcResponse galiRpcResponse, Serializer jdkSerializer) {
        HttpServerResponse response = request.response().putHeader("content-type", "application/json");
        try {
            byte[] serialize = jdkSerializer.serialize(galiRpcResponse);
            response.end(Buffer.buffer(serialize));
        } catch (Exception e) {
            log.error("Failed to serialize");
        }
    }
}
