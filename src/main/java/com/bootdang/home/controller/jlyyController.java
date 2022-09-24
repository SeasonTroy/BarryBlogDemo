package com.bootdang.home.controller;/*
package com.bootdang.home.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bootdang.system.entity.Tv;
import com.bootdang.system.entity.Tvtype;
import com.bootdang.system.entity.Wheel;
import com.bootdang.system.service.IArticleService;
import com.bootdang.system.service.ITvtypeService;
import com.bootdang.system.service.IWheelService;
import com.bootdang.system.service.impl.TvServiceImpl;
import com.bootdang.tv.UrlUtils;
import com.bootdang.util.FileAdd;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
@EnableConfigurationProperties(value = UrlUtils.class)
@Controller
public class jlyyController {
    @Autowired
    ITvtypeService tvtypeService;
    @Autowired
    TvServiceImpl tvService;
    @Autowired
    IWheelService iWheelService;

    @Autowired
    UrlUtils urlUtils;



    @GetMapping("/jlyy/{id}")
    public String index(@PathVariable(name = "id") Integer id, Model model){
        if(id==null){
            return "404";
        }
        List<Wheel> list = iWheelService.list(new QueryWrapper<Wheel>().eq("state", 1).eq("arttype_id", id).orderByAsc("num"));
        model.addAttribute("lunbo",list);
        Tvtype[] tvtypes = tvtypeService.selectDistinct();
        model.addAttribute("tv",1);//修改搜索状态 改变成电影搜索
        model.addAttribute("tvtype",tvtypes);
        model.addAttribute("tyid",id);
        //电影渲染
        ArrayList<Map<String,Object>> arrs = new ArrayList<>();
        for(Tvtype tvtype:tvtypes){
            HashMap<String, Object> map = new HashMap<>();
            List<Tv> tvs = tvService.list(new QueryWrapper<Tv>().eq("type_id", tvtype.getTvtypeId()).last("limit 0,16"));
             map.put("tvtypename",tvtype.getTitle());
             map.put("tv",tvs);
             arrs.add(map);

        }
        model.addAttribute("arrs",arrs);


        return "home/tv/index";
    }

    @GetMapping("/tvselect/{id}")
    public String tvselect(@PathVariable(name = "id") Integer id,Model model){
        Tv tv = tvService.getById(id);
        tvService.updateById(new Tv().setTvId(tv.getTvId()).setClick(tv.getClick()+1));
        model.addAttribute("tv",tv);
        Tvtype tvtype = tvtypeService.getById(tv.getTypeId());
        model.addAttribute("type",tvtype.getTitle());//电影所属类型名称
        model.addAttribute("jx",urlUtils.getUrl());//解析api
        List<Tv> type_id = tvService.list(new QueryWrapper<Tv>().gt("tv_id",id).eq("type_id", tv.getTypeId()).last("limit 0,6"));
        model.addAttribute("tjtv",type_id);
        return "home/tv/select";
    }

    @GetMapping("/tvinfo/{tyid}/{id}")
    public String typeinfo(@PathVariable(name="tyid") Integer tyid,@PathVariable(name="id") Integer id,Model model){
        if(id==null){
            return "404";
        }
        if(!model(id,model)){
          return "404";
        }
        List<Wheel> list = iWheelService.list(new QueryWrapper<Wheel>().eq("state", 1).eq("arttype_id", tyid).orderByAsc("num"));
        model.addAttribute("lunbo",list);
        model.addAttribute("tyid",tyid);

       model.addAttribute("type",tvtypeService.getById(id));
        return "home/tv/typeIndex";
    }

    @ResponseBody
    @GetMapping("/tvpage")
    public Map<String,Object>  tvpage(Integer typeid,Integer limit,Integer page){
        Page<Object> objects = PageHelper.startPage(page+1, limit);
        List<Tv> tvs= tvService.list(new QueryWrapper<Tv>().ge("type_id",typeid).le("type_id", typeid+29).orderByDesc("click"));
        HashMap<String, Object> map = new HashMap<>();
        map.put("rows",tvs);
        map.put("total",objects.getTotal());
        return map;
    }

    public boolean model(Integer id,Model model){
        try {
            List<Wheel> list = iWheelService.list(new QueryWrapper<Wheel>().eq("state", 1).eq("arttype_id", id).orderByAsc("num"));
            model.addAttribute("lunbo", list);
            Tvtype[] tvtypes = tvtypeService.selectDistinct();
            model.addAttribute("tv", 1);//修改搜索状态 改变成电影搜索
            model.addAttribute("tvtype", tvtypes);
            return true;
        }catch (Exception e){
            e.getMessage();
            return false;
        }
    }
}
*/
