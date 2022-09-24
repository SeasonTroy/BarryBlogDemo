package com.bootdang.system.controller;/*
package com.bootdang.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bootdang.system.entity.Tvtype;
import com.bootdang.system.mapper.TvtypeMapper;
import com.bootdang.system.service.ITvtypeService;
import com.bootdang.tv.JsoupTv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

*/
/**
 * <p>
 *  前端控制器
 * </p>
 *
 *//*

@Controller
@RequestMapping("/admin/tvcj")
public class TvtypeController {
    @Autowired
    JsoupTv jsoupTv;
    @Autowired
    ITvtypeService iTvtypeService;


    @RequestMapping()
    public String index(){
        return "admin/page/tv/collection";
    }

    @ResponseBody
    @GetMapping("/aqywhole")
    public Map<String,Object> aqywhole(){
        HashMap<String, Object> map = new HashMap<>();
        if(iTvtypeService.count()>0){//不为空开始采集如果需要重新采集请删除数据库
            map.put("code",200);
            map.put("msg","类型已经采集过");
            return map;
        }
        boolean aqytypecj = jsoupTv.aqytypecj("https://list.iqiyi.com/www/6/-------------24-1-1-iqiyi--.html");
        if(aqytypecj){
           map.put("code",200);
           map.put("msg","采集成功");
           return map;
        }

        map.put("code",500);
        map.put("msg","采集失败");
       //jsoupTv.aiqiyitv();
        return map;
    }
    @ResponseBody
    @GetMapping("/aqydt")
    public Map<String,Object> aqydt(@RequestParam(value = "url") String url){
        HashMap<String, Object> map = new HashMap<>();
        Tvtype i_dentification = iTvtypeService.getOne(new QueryWrapper<Tvtype>().eq("i_dentification", url));
       if(i_dentification!=null){
           map.put("code",500);
           map.put("msg","已经采集过了");
           return map;
       }


        return map;
    }
}

*/
