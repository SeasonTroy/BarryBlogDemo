package com.bootdang.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bootdang.common.aspect.Log;
import com.bootdang.system.entity.Arttype;
import com.bootdang.system.entity.Wheel;
import com.bootdang.system.service.IArticleService;
import com.bootdang.system.service.IArttypeService;
import com.bootdang.system.service.IWheelService;
import com.bootdang.util.ShiroUtils;
import com.bootdang.util.SystemColumnUrl;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * <p>
 * 轮播表 前端控制器
 * </p>
 */
@Controller
@RequestMapping("/admin/wheel")
public class WheelController {
    @Autowired
    IWheelService wheelService;
    @Resource
    SystemColumnUrl systemColumnUrl;
    @Autowired
    IArttypeService iArttypeService;
    @Autowired
    IArticleService iArticleService;
    @RequestMapping()
    public String index(Model model){
        model.addAttribute("list",wheelService.selectAll());
        model.addAttribute("daohang",systemColumnUrl.select(null));
        return "admin/page/wheel/wheel_list";
    }

    @GetMapping("/add")
    public String add(Model model){
        List<Arttype> arttypes = iArttypeService.list(new QueryWrapper<Arttype>().eq("parentid", 0));
        model.addAttribute("arttypes",arttypes);
        return "admin/page/wheel/wheelAdd";
    }

    @Log(value = "上传了轮播图")
    @ResponseBody
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public Map<String,Object> fileimage(@RequestParam("file") MultipartFile[] files){
        HashMap<String,Object> r= new HashMap<>();
        StringBuffer stringBuffer = new StringBuffer();
        for(int i=0;i<files.length;i++){
            String upload = iArticleService.upload(files[i]);
            stringBuffer.append(upload+",");
        }
            r.put("code",200);
            r.put("msg","上传成功");
            r.put("url",stringBuffer);

        return r;
    }

    @ResponseBody
    @PostMapping("/insert")
    public Map<String,Object> insert(Wheel wheel){
        HashMap<String,Object> r= new HashMap<>();
        if(wheel.getState()==null){
            wheel.setState(0);
        }
        String imageurls = wheel.getImageurls();
        if(imageurls==null||imageurls.equals("")){
            r.put("code",500);
            r.put("msg","图片不能为空");
         return r;
        }
        Wheel wh = wheel.setCreatetime(LocalDateTime.now()).setCreateuserid(ShiroUtils.getUserId());
        imageurls=imageurls.substring(0,imageurls.length()-1);
        String[] split = imageurls.split(",");
        Arrays.stream(split).forEach((a)->{
             wheelService.save(wheel.setImageurl(a));
                }
        );
        r.put("code",200);
        r.put("msg","上传成功");
        return r;
    }

    @ResponseBody
    @PostMapping("/deleteall")
    public Integer deleteall(String datas){
        String[] split = datas.split(",");
        List<String> strings = Arrays.asList(split);
        boolean b = wheelService.removeByIds(strings);
        if(b){
            return 200;
        }
        return 500;
    }

    @ResponseBody
    @PostMapping("/delete")
    public Map<String,Object> delete(Integer id){
        HashMap<String, Object> map = new HashMap<>();
        boolean b = wheelService.removeById(id);
        if(b){
            map.put("code",200);
            map.put("msg","删除成功");
            return map;
        }
        map.put("code",500);
        map.put("msg","删除失败");
        return map;
    }

    @GetMapping("/edit")
    public String edit(Model model,Integer id){
        model.addAttribute("wheel",wheelService.getById(id));
        List<Arttype> arttypes = iArttypeService.list(new QueryWrapper<Arttype>().eq("parentid", 0));
        model.addAttribute("arttypes",arttypes);
        return "admin/page/wheel/wheelEdit";
    }

    @ResponseBody
    @PostMapping("/update")
    public Map<String,Object> update(Wheel wheel) {
        HashMap<String, Object> map = new HashMap<>();
        if (wheel.getState() == null) {
            wheel.setState(0);
        }
        if(wheel.getImageurl().lastIndexOf(",")!=-1) {
            wheel.setImageurl(wheel.getImageurl().substring(0, wheel.getImageurl().length() - 1));//清除最后一个逗号
        }
        boolean b = wheelService.saveOrUpdate(wheel);
        if(b){
            map.put("code",200);
            map.put("msg","修改成功");
            return map;
        }
        map.put("code",500);
        map.put("msg","修改失败");
        return map;
    }
}

