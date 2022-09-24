package com.bootdang.util;


import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Arrays;

public class MyXssHttpServleetRequest extends HttpServletRequestWrapper {
    private static HttpServletRequest httpServletRequest;
    private static boolean is_request=false;//是否调用过滤

    public MyXssHttpServleetRequest (HttpServletRequest request,boolean is_request) {
        super(request);
        this.httpServletRequest=request;
        this.is_request=is_request;
    }


    @Override
    public String getHeader (String name) {
        name = XssFileUtils.clean(name);
        String value = super.getHeader(name);
        if (StringUtils.isNotBlank(value)) {
            value = XssFileUtils.clean(value);
        }
        return value;
    }

    @Override
    public String getParameter (String name) {
        if (("context".equals(name) || name.equals("description")|| name.endsWith("WithHtml")) && !is_request) {
            return super.getParameter(name);
        }
        name = XssFileUtils.clean(name);
        String value = super.getParameter(name);
        if (StringUtils.isNotBlank(value)) {
            value = XssFileUtils.clean(value);
        }
        return value;
    }

    @Override
    public String[] getParameterValues (String name) {
        if(!is_request){
            return super.getParameterValues(name);
        }
        String[] parameterValues = super.getParameterValues(name);

        if(parameterValues!=null) {
           for(int i=0;i<parameterValues.length;i++){
              parameterValues[i]=XssFileUtils.clean(parameterValues[i]);
           }
        }
        return parameterValues;

    }
    public static HttpServletRequest getOrgRequest() {
        return httpServletRequest;
    }
}
