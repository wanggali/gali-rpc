package com.gali.rpc.serializer;

import com.gali.rpc.spi.SpiLoader;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

/**
 * SerializerFactory:
 *
 * @author gali
 * @date 2024/07/01
 */
public class SerializerFactory {

    static {
        SpiLoader.load(Serializer.class);
    }

    /**
     * 默认序列化器
     */
    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    /**
     * 获取实例
     *
     * @param key
     * @return
     */
    public static Serializer getInstance(String key) {
        return SpiLoader.getInstance(Serializer.class, key);
    }
}
