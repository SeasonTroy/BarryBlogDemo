package com.bootdang.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpDataTimeUtils {

    private static  SimpleDateFormat formats=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public SimpDataTimeUtils(){}

    public SimpDataTimeUtils(String data){
        formats=new SimpleDateFormat(data);
    }

    public Date parse(String data) {

        try {
            Date parse = formats.parse(data);
            return parse;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String format(Date date){
        String format = formats.format(date);
        return format;
    }
}
