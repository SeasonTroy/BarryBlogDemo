package com.bootdang.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineUser {

    private Integer user_id;
    //用户sessionid
    private String xhid;
    //用户名称
    private String username;
    //是否是超级管理员
    private Integer isadmin;
    //登陆时间
    @JsonFormat(pattern = "yyyy年MM月dd日hh:mm:ss秒 a")
    private Date logindatetime;
    //最近访问时间

    @JsonFormat(pattern = "yyyy年MM月dd日hh:mm:ss秒 a")
    private Date lastdatetime;
    //过期时间
    private Long logoutdatetime;
    /**
     * 用户主机地址
     */
    private String host;

    /**
     * 用户登录时系统IP
     */
    private String systemHost;

    /**
     * 用户浏览器类型
     */
    private String userAgent;

    /**
     * 在线状态
     */
    private Integer status = 1;
}
