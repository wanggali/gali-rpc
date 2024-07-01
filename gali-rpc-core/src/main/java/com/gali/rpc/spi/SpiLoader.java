package com.gali.rpc.spi;

import cn.hutool.core.io.resource.ResourceUtil;
import com.gali.rpc.serializer.Serializer;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SpiLoader:
 *
 * @author gali
 * @date 2024/07/01
 */
@Slf4j
public class SpiLoader {
    /**
     * 已加载的类 接口名-key（实现类）
     */
    private static Map<String/*接口名*/, Map<String/*key*/, Class/*实现类字节码*/>> loadMap = new ConcurrentHashMap<>();

    /**
     * 对象实例缓存,避免重复初始化，类路径-》对象实例 单例模式
     */
    private static Map<String, Object> instanceMap = new ConcurrentHashMap<>();

    /**
     * 系统SPI目录
     */
    private static final String SYSTEM_SPI_DIR = "META-INF/gali/system/";

    /**
     * 用户自定义SPI目录
     */
    private static final String CUSTOM_SPI_DIR = "META-INF/gali/custom/";

    /**
     * 扫描路径
     */
    private static final String[] SCAN_DIRS = new String[]{SYSTEM_SPI_DIR, CUSTOM_SPI_DIR};

    /**
     * 动态加载类列表
     */
    private static final List<Class> LOAD_CLASS_LIST = Lists.newArrayList(Serializer.class);


    /**
     * 加载所有类型
     */
    public static void loadAll() {
        log.info("load all spi info");
        for (Class aClass : LOAD_CLASS_LIST) {
            load(aClass);
        }
    }

    public static Map<String, Class> load(Class loadClass) {
        log.info("load class spi info,name:{}", loadClass.getName());

        //扫描路径，用户自定义SPI优先级高于系统SPI
        Map<String, Class> keyClassMap = new HashMap<>();
        for (String scanDir : SCAN_DIRS) {
            List<URL> resources = ResourceUtil.getResources(scanDir + loadClass.getName());
            // 读取每个资源文件
            for (URL resource : resources) {
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] strArray = line.split("=");
                        if (strArray.length > 1) {
                            String key = strArray[0];
                            String className = strArray[1];
                            keyClassMap.put(key, Class.forName(className));
                        }
                    }
                } catch (Exception e) {
                    log.error("spi resource load error", e);
                }
            }
        }
        loadMap.put(loadClass.getName(), keyClassMap);
        return keyClassMap;

    }

    /**
     * 获取某个接口的实例
     *
     * @param tClass
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getInstance(Class<?> tClass, String key) {
        String tClassName = tClass.getName();
        Map<String, Class> keyClassMap = loadMap.get(tClassName);
        if (keyClassMap == null) {
            throw new RuntimeException(String.format("SpiLoader 未加载 %s 类型", tClassName));
        }
        if (!keyClassMap.containsKey(key)) {
            throw new RuntimeException(String.format("SpiLoader 的 %s 不存在 key=%s 的类型", tClassName, key));
        }
        // 获取到要加载的实现类型
        Class<?> implClass = keyClassMap.get(key);
        // 从实例缓存中加载指定类型的实例
        String implClassName = implClass.getName();
        if (!instanceMap.containsKey(implClassName)) {
            try {
                instanceMap.put(implClassName, implClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                String errorMsg = String.format("%s 类实例化失败", implClassName);
                throw new RuntimeException(errorMsg, e);
            }
        }
        return (T) instanceMap.get(implClassName);
    }

}
