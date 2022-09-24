package com.bootdang.util;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/*
* 菜单树
*
* */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreeMenu {
    @JsonIgnore
    private int id;
    @JsonIgnore
    private Integer parentid;
    private String title;
    private String icon;
    private String href;
    private boolean spread=false;
    private List<TreeMenu> children=new ArrayList<>();
}
