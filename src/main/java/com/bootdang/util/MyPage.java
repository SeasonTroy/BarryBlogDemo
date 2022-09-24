package com.bootdang.util;

import lombok.Data;

@Data
public class MyPage {
    private Integer page;//当前页
    private Integer limit;//每页多少条
    private int total;//一共多少条
    private int pages;//一共多少页
}
