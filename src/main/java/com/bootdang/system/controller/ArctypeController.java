package com.bootdang.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bootdang.common.aspect.Log;
import com.bootdang.system.entity.Article;
import com.bootdang.system.entity.Arttype;
import com.bootdang.system.entity.Dept;
import com.bootdang.system.service.IArttypeService;
import com.bootdang.util.Pinyin4j;

import com.bootdang.util.SystemColumnUrl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/arctype")
public class ArctypeController {

    @Autowired
    IArttypeService iArttypeService;

    @Resource
    SystemColumnUrl systemColumnUrl;


    @GetMapping()
    public String arctypeindex(Model model){
        model.addAttribute("daohang",systemColumnUrl.select(null));
        return "admin/page/articletype/type_List";
    }

    @ResponseBody
    @GetMapping("/arttypejson")
    public Map<String, Object> menujson () {
        Map<String, Object> map = new HashMap<>();
        QueryWrapper<Arttype> sort = new QueryWrapper<Arttype>().orderByAsc("sort");
        List<Arttype> list = iArttypeService.list(sort);
        if (!list.isEmpty()) {
            map.put("code", 0);
            map.put("msg", "");
            map.put("count", list.size());
            map.put("data", list);
            return map;
        }
        map.put("code", 500);
        map.put("msg", "数据为空");
        return map;
    }

    @Log(value = "添加栏目操作")
    @RequiresPermissions("arttype-insert")
    @ResponseBody
    @PostMapping("/insert")
    public Map<String,String> arctypeinset(Arttype arttype){
        arttype.setCreatetime(LocalDateTime.now());
       if(arttype.getState()==null||arttype.getState().equals(0)){
           arttype.setState(0);
       }
       if(arttype.getSort()==null){
           arttype.setSort(99);
       }
       arttype.setPinyin(Pinyin4j.getpinyin(arttype.getTitle()));
        boolean save = iArttypeService.save(arttype);
        Map<String,String> map = new HashMap<>();
        if(save){
         map.put("code","200");

        }else{
            map.put("code","500");
        }

        return map;
    }

    /**
     * 分页
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("/addpage")
    public HashMap<String, Object> arctypeadd(@RequestParam(name = "page",required = false,defaultValue = "1") String page, @RequestParam(name = "limit",required = false,defaultValue = "10")String limit){
        PageHelper.startPage(Integer.parseInt(page),Integer.parseInt(limit));
         List<Arttype> list =iArttypeService.selectAll();
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
         if(list!=null){
             PageInfo<Arttype> info=new PageInfo<>(list);
             objectObjectHashMap.put("code",200);
             objectObjectHashMap.put("pages",info.getPages());
             objectObjectHashMap.put("datas",list);
             return objectObjectHashMap;
         }

        objectObjectHashMap.put("code",500);
        return objectObjectHashMap;
    }

    @RequiresPermissions("arttype-update")
    @Log(value = "修改栏目状态")
    @PostMapping(value = "/updatestate")
    @ResponseBody
    public  HashMap<String, Integer> updatestate(Integer id, boolean state){
        Arttype arttype = new Arttype();
        arttype.setActtypeId(id);
        arttype.setState(state==true?1:0);
        boolean save = iArttypeService.saveOrUpdate(arttype);
        HashMap<String, Integer> result = new HashMap<>();
        if(save){
            result.put("code",200);
           return result;
        }
        result.put("code",500);
        return result;
    }

    @GetMapping(value = "/add")
     public String add(Model model,@RequestParam(required = false,value = "parentid",defaultValue = "0") Integer parentid){
        if(parentid==0){
            model.addAttribute("list",iArttypeService.list());
            return "admin/page/articletype/type_add";
        }else{
            model.addAttribute("list",iArttypeService.list());
            model.addAttribute("parentid",parentid);
            return "admin/page/articletype/type_add";
        }
     }

    @GetMapping(value = "/edit")
    public ModelAndView edit(Integer id){
        Arttype byId = iArttypeService.getById(id);
        ModelAndView modelAndView = new ModelAndView();
        if(byId!=null) {
            modelAndView.addObject("list",iArttypeService.selectArttype());
            modelAndView.addObject("articletype", byId);
            modelAndView.setViewName("admin/page/articletype/type_edit");
        }else{
            modelAndView.addObject("msg","非法输入");
            modelAndView.setView(new MappingJackson2JsonView());
        }

        return modelAndView;
    }
    @RequiresPermissions("arttype-update")
    @Log(value = "修改栏目操作")
    @ResponseBody
    @PostMapping(value = "update")
    public Map<String,Object> update(Arttype arttype){
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        if(arttype.getSort()==null){
            arttype.setSort(99);
        }
        arttype.setPinyin(Pinyin4j.getpinyin(arttype.getTitle()));
        boolean save = iArttypeService.saveOrUpdate(arttype);
        if(save){
            objectObjectHashMap.put("code",200);
            return objectObjectHashMap;
        }else{
            objectObjectHashMap.put("code",500);
            return objectObjectHashMap;
        }

    }


    @RequiresPermissions("arttype-delete")
    @Log(value = "删除栏目操作")
    @ResponseBody
    @PostMapping(value = "/delete")
    public HashMap<String,Object> delete(Integer id){
        arttypedele(id);
        boolean b = iArttypeService.removeById(id);
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        if(b){
           objectObjectHashMap.put("code","200");
           objectObjectHashMap.put("msg","删除成功");
        }else {
           objectObjectHashMap.put("code","500");
           objectObjectHashMap.put("msg","删除失败");
        }

        return objectObjectHashMap;
    }

    //遍历删除子栏目
    public Boolean arttypedele (Integer id) {
        List<Arttype> parent_id = iArttypeService.list(new QueryWrapper<Arttype>().eq("parentid", id));
        if (parent_id.isEmpty()) {
            return false;
        }
        parent_id.forEach((v) -> {
            arttypedele(v.getActtypeId());//删除子栏目
            iArttypeService.removeById(v.getActtypeId());
        });
        return true;

    }

}
