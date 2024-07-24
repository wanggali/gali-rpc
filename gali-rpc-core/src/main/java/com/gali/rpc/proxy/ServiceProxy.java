package com.gali.rpc.proxy;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.gali.rpc.GaliRpcApplication;
import com.gali.rpc.config.RegistryConfig;
import com.gali.rpc.config.RpcConfig;
import com.gali.rpc.constant.RpcConstant;
import com.gali.rpc.fault.retry.RetryStrategy;
import com.gali.rpc.fault.retry.RetryStrategyFactory;
import com.gali.rpc.fault.tolerant.TolerantStrategy;
import com.gali.rpc.fault.tolerant.TolerantStrategyFactory;
import com.gali.rpc.loadbalancer.LoadBalancer;
import com.gali.rpc.loadbalancer.LoadBalancerFactory;
import com.gali.rpc.model.GaliRpcRequest;
import com.gali.rpc.model.GaliRpcResponse;
import com.gali.rpc.model.ServiceMetaInfo;
import com.gali.rpc.register.Registry;
import com.gali.rpc.register.RegistryFactory;
import com.gali.rpc.serializer.JdkSerializer;
import com.gali.rpc.serializer.Serializer;
import com.gali.rpc.serializer.SerializerFactory;
import com.gali.rpc.service.tcp.VertxTcpClient;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetClient;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String serviceName = method.getDeclaringClass().getName();
        RpcConfig rpcConfig = GaliRpcApplication.getRpcConfig();
        Serializer jdkSerializer = SerializerFactory.getInstance(rpcConfig.getSerializer());

        //构造请求
        GaliRpcRequest request = GaliRpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        //注册中心获取服务提供者地址
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);

        List<ServiceMetaInfo> serviceMetaInfos = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
        if (CollectionUtil.isEmpty(serviceMetaInfos)) {
            throw new RuntimeException("No service discovery");
        }

        // 负载均衡
        LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
        // 将调用方法名（请求路径）作为负载均衡参数
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("methodName", request.getMethodName());
        ServiceMetaInfo discoveryServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfos);

        GaliRpcResponse galiRpcResponse = null;
        try {
            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
            galiRpcResponse = retryStrategy.doRetry(() -> VertxTcpClient.doRequest(request, discoveryServiceMetaInfo));
        } catch (Exception e) {
            //容错机制
            TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getInstance(rpcConfig.getTolerantStrategy());
            galiRpcResponse = tolerantStrategy.doTolerant(null, e);
        }
        return galiRpcResponse.getData();

//        try {
//            byte[] serialize = jdkSerializer.serialize(request);
//            //发送请求
//            HttpResponse execute = HttpRequest.post(discoveryServiceMetaInfo.getServiceAddress())
//                    .body(serialize)
//                    .execute();
//            byte[] bytes = execute.bodyBytes();
//            GaliRpcResponse deserialize = jdkSerializer.deserialize(bytes, GaliRpcResponse.class);
//            execute.close();
//            return deserialize.getData();
//        } catch (Exception e) {
//            log.error("proxy error", e);
//        }
    }
}
