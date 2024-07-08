package com.gali.rpc.config;

import lombok.Data;
import com.gali.rpc.register.RegistryKeys;


/**
 * RegistryConfig:
 *
 * @author gali
 * @date 2024/07/08
 */
@Data
public class RegistryConfig {
    /**
     * 注册中心类别
     */
    private String registry = RegistryKeys.ETCD;

    /**
     * 注册中心地址
     */
    private String address = "http://localhost:2380";

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 超时时间（单位毫秒）
     */
    private Long timeout = 10000L;
}
