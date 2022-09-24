package com.bootdang.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bootdang.system.entity.Menu;
import com.bootdang.system.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

@Service
public class SystemColumnUrl {

    @Autowired
    IMenuService  menuService;

    public List<Map<String,String>> select(String data){
        //如果没有传入默认获取当前请求的uri
        if(data==null||data.equals("")) {
            data = HttpContextRequestUtil.getUrl();
        }
        QueryWrapper<Menu> queryWrapper=new QueryWrapper();
        queryWrapper.eq("url",data);
        List<Menu> list = menuService.list(queryWrapper);
        List<Map<String,String>> menulist=new CopyOnWriteArrayList<>();
        for (Menu menu:list){
            if(menu.getParentid()==0){
                Map<String,String> maps=new HashMap<>();
                maps.put("title",menu.getTitle());
                maps.put("url",menu.getUrl());
                menulist.add(maps);
            }else{
                //一级栏目
                Menu byId = menuService.getById(menu.getParentid());
                Map<String,String> map1=new HashMap<>();
                map1.put("title",byId.getTitle());
                map1.put("url",byId.getUrl());
                menulist.add(map1);
               //二级栏目
                Map<String,String> map2=new HashMap<>();
                map2.put("title",menu.getTitle());
                map2.put("url",menu.getUrl());
                menulist.add(map2);
            }

        }

        return menulist;
    }
}
