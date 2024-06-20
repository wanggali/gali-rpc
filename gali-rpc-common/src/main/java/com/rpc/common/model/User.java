package com.rpc.common.model;

import java.io.Serializable;

/**
 * User:
 *
 * @author gali
 * @date 2024/06/20
 */

public class User implements Serializable {

    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User(String name) {
        this.name = name;
    }
}
