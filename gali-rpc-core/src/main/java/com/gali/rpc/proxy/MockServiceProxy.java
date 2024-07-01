package com.gali.rpc.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static cn.hutool.core.util.ClassUtil.getDefaultValue;

/**
 * MockServiceProxy:
 *
 * @author gali
 * @date 2024/07/01
 */
@Slf4j
public class MockServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> returnType = method.getReturnType();
        log.info("mock invoke:{}", method.getName());
        return getDefaultValue(returnType);
    }
}
