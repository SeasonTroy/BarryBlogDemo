package com.bootdang.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class HttpContextRequestUtil {

    public static HttpServletRequest getHttpServletRequest(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }

    public static String getUrl(){
        HttpServletRequest httpServletRequest = getHttpServletRequest();
        String requestURI = httpServletRequest.getRequestURI();
        return requestURI;
    }
}
