package com.bootdang.common;

import com.bootdang.util.MyPage;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


public class PageConverter implements Converter<String, MyPage> {
    @Override
    public MyPage convert (String s) {
        String[] split = s.split("&");//按照这个符号，分割字符串
        MyPage myPage = new MyPage();
        myPage.setPage(Integer.parseInt(split[0]));
        myPage.setLimit(Integer.parseInt(split[1]));
        return myPage;
    }
}
