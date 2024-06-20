package com.gali.rpc.provider.service;

import com.rpc.common.model.User;
import com.rpc.common.service.UserService;

/**
 * UserServiceImpl:
 *
 * @author gali
 * @date 2024/06/20
 */
public class UserServiceImpl implements UserService {
    @Override
    public User getUserName(String userName) {
        System.out.println("用户名" + userName);
        return new User(userName);
    }
}
