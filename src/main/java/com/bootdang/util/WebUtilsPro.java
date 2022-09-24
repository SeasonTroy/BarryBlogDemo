package com.bootdang.util;

import javax.servlet.http.HttpServletRequest;

public class WebUtilsPro {

    /**
     * 是否是Ajax请求
     *
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("x-requested-with");
        if (requestedWith != null && requestedWith.equalsIgnoreCase("XMLHttpRequest")) {
            return true;
        } else {
            return false;
        }
    }
}
