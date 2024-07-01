package com.gali.rpc.proxy;

import java.lang.reflect.Proxy;

/**
 * ServiceProxyFactory:
 *
 * @author gali
 * @date 2024/06/20
 */
public class ServiceProxyFactory {

    public static <T> T getServiceProxy(Class<T> tClass) {
        return (T) Proxy.newProxyInstance(tClass.getClassLoader(),
                new Class[]{tClass},
                new ServiceProxy());
    }


    public static <T> T getMockServiceProxy(Class<T> tClass) {
        return (T) Proxy.newProxyInstance(tClass.getClassLoader(),
                new Class[]{tClass},
                new MockServiceProxy());
    }
}
