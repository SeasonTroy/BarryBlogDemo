package com.bootdang.util;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

public class DateTimeFormatUtils {
    //yyyy年MM月dd日hh:mm a "1999年07月11日10:23 上午
    private static DateTimeFormatter formatter;
    public DateTimeFormatUtils(String data){
        formatter=DateTimeFormatter.ofPattern(data);
    }
    public LocalDateTime parse(String data) {

        LocalDateTime parse = LocalDateTime.parse(data, formatter);
        return  parse;

    }

    public String format(LocalDateTime date){
        String format = formatter.format(date);
        return format;
    }
}
