package com.gali.rpc.register;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * LocalRegister:
 *
 * @author gali
 * @date 2024/06/17
 */
public class LocalRegister {
    /**
     * 注册信息存储
     */
    private static final Map<String, Class> REGISTER_MAP = Maps.newConcurrentMap();

    /**
     * 注册服务
     *
     * @param serviceName
     * @param implClass
     */
    public static void register(String serviceName, Class<?> implClass) {
        REGISTER_MAP.put(serviceName, implClass);
    }

    /**
     * 获取服务
     *
     * @param serviceName
     * @return
     */
    public static Class<?> get(String serviceName) {
        return REGISTER_MAP.get(serviceName);
    }

    /**
     * 删除服务
     *
     * @param serviceName
     */
    public static void remove(String serviceName) {
        REGISTER_MAP.remove(serviceName);
    }
}
