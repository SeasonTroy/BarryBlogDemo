package com.bootdang.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

public class MyUserNamePassWord extends UsernamePasswordToken {
    private static final long serialVersionUID = 2362997079640938029L;

    private static Integer type;

    public static Integer getType () {
        return type;
    }

    public static void setType (Integer type) {
        MyUserNamePassWord.type = type;
    }

    public MyUserNamePassWord (String username, String password, Integer type) {
        super(username, password);
        this.type=type;
    }
}
