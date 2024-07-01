package com.gali.rpc.consumer;

import com.gali.rpc.proxy.ServiceProxyFactory;
import com.rpc.common.model.User;
import com.rpc.common.service.UserService;

import java.util.Objects;

/**
 * EasyConsumerMain:
 *
 * @author gali
 * @date 2024/06/20
 */
public class EasyConsumerMain {
    public static void main(String[] args) {
        UserService userService = ServiceProxyFactory.getServiceProxy(UserService.class);

        User gali = userService.getUserName("gali");
        if (Objects.isNull(gali)) {
            System.out.println("userName is null");
        } else {
            System.out.println(gali.getName());
        }

        UserService userMockService = ServiceProxyFactory.getMockServiceProxy(UserService.class);

        System.out.println(userMockService.getDefaultValue());
    }
}
