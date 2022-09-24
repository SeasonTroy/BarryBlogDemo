package com.bootdang.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bootdang.system.entity.Menu;
import com.bootdang.system.entity.User;
import com.bootdang.system.service.IMenuService;
import com.bootdang.util.ShiroUtils;
import com.bootdang.util.SystemColumnUrl;
import com.bootdang.util.TreeMenu;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 *  栏目控制器
 *  前端控制器
 * </p>
 */
@Controller
@RequestMapping("/admin/systemcolumn")
public class MenuController {
    private final Logger log= LoggerFactory.getLogger(MenuController.class);
    @Autowired
    IMenuService menuService;

    @Resource
    SystemColumnUrl systemColumnUrl;

     public void sysurl(){}
    /**
     * 栏目查询 按照排序
     * @param model
     * @return
     */
    @GetMapping()
    public String index(Model model){
        List<Menu> menus = menuService.selectMenuAll();
        ArrayList<Menu> menu = new ArrayList<>();
        for(Menu men:menus){
           if(men.getParentid()==0){
               menu.add(men);
               Integer menuId = men.getMenuId();
               for (Menu me:menus){
                    if(me.getParentid()==menuId){
                        menu.add(me);
                    }
               }
           }
        }

        model.addAttribute("lists",menu);
        List<Map<String, String>> select = systemColumnUrl.select(null);
        model.addAttribute("daohang",select);
        return "admin/page/systemcolumn/cate";
    }

    /**
     * 状态修改
     * @param id
     * @param state
     * @return 状态码
     */
    @ResponseBody
    @PostMapping("/updatestate")
    public Integer updatestate(Integer id,boolean state){
        Menu menu = new Menu();
        menu.setMenuId(id);
        menu.setState(state==true? 1:0);
        boolean b = menuService.saveOrUpdate(menu);
        if(b){
           return 200;
        }
        return 400;
    }

    /**
     * 栏目新增跳转
     * @return
     */
    @GetMapping("/add")
    public String add(Model model){
        List<Menu> parentid = menuService.list(new QueryWrapper<Menu>().eq("parentid", 0));
        model.addAttribute("list",parentid);
        return "admin/page/systemcolumn/system-add";
    }
    /**
     * 栏目添加
     * @param menu
     * @param result
     * @return
     */
    @ResponseBody
    @PostMapping("/insert")
    public Map<String,Object> insert(@Valid Menu menu, BindingResult result){
        HashMap<String, Object> map = new HashMap<>();
        if(result.hasErrors()){
            map.put("code",500);
            map.put("msg",result.getFieldError().getDefaultMessage());
          return map;
      }
        Object principal = SecurityUtils.getSubject().getPrincipal();
        User user=User.of();
        try {
            BeanUtils.copyProperties(principal, user);
        } catch (Exception e) {

        }
        menu.setCreateuserid(user.getUserId());
            menu.setCreatetime(LocalDateTime.now());
            boolean save = menuService.save(menu);
            if(save){
                map.put("code",200);
                map.put("msg","新增成功");
                return map;
            }
        map.put("code",500);
        map.put("msg","新增失败");
        return map;

    }
    /**
     * 栏目修改跳转
     * @return
     */
    @GetMapping("/edit")
    public String edit(Integer id,Model model){
        List<Menu> parentid = menuService.list(new QueryWrapper<Menu>().eq("parentid", 0));
        model.addAttribute("list",parentid);
        Menu byId = menuService.getById(id);
        model.addAttribute("menu",byId);
        return "admin/page/systemcolumn/system-edit";
    }

    /**
     * 修改方法
     * @param menu
     * @param result
     * @return
     */
    @ResponseBody
    @PostMapping("/update")
    public Map<String,Object> update(@Valid Menu menu,BindingResult result){
        HashMap<String, Object> map = new HashMap<>();
        if(result.hasErrors()){
            map.put("code",500);
            return map;
        }
          menu.setUpdatetime(LocalDateTime.now());
          menuService.saveOrUpdate(menu);
          map.put("code",200);

        return map;
    }

    /**
     * 删除方法
     * @param id
     * @return
     */
    @ResponseBody
    @PostMapping("/delete")
    public Integer delete(Integer id){

        HashMap<String, Object> stringIntegerHashMap = new HashMap<>();
        stringIntegerHashMap.put("parentid", id);
        menuService.removeByMap(stringIntegerHashMap);
        boolean b = menuService.removeById(id);
        if(b){
            return 200;
        }
        return 500;
    }

    /**
     * 批量删除
     * @param datas
     * @return
     */
    @ResponseBody
    @PostMapping("/deleteall")
    public Integer deleteall(@RequestParam("datas") String datas){
        String[] split = datas.split(",");
        log.error(split.toString());
        for(String id:split) {
            HashMap<String, Object> stringIntegerHashMap = new HashMap<>();
            stringIntegerHashMap.put("parentid", Integer.parseInt(id));
            menuService.removeByMap(stringIntegerHashMap);
        }
        List<String> strings = Arrays.asList(split);
        boolean b = menuService.removeByIds(strings);
        if(b){
            return 200;
        }
        return 500;

    }
    @RequestMapping("/loweradd")
    public String loweradd(Integer id,Model model){
        Menu byId = menuService.getById(id);
        model.addAttribute("bymenu",byId);
        return "admin/page/systemcolumn/system-loweradd";
    }


    @ResponseBody
    @PostMapping("/lowerinsert")
    public Map<String,Object> lowerinsert(@Valid Menu menu, BindingResult result){
        HashMap<String, Object> map = new HashMap<>();
        if(result.hasErrors()){
            map.put("code",500);
            map.put("msg",result.getFieldError().getDefaultMessage());
            return map;
        }
        Object principal = SecurityUtils.getSubject().getPrincipal();
        User user=User.of();
        try {
            BeanUtils.copyProperties(principal, user);
        } catch (Exception e) {

        }
        menu.setCreateuserid(user.getUserId());
        menu.setCreatetime(LocalDateTime.now());
        boolean save = menuService.save(menu);
        if(save){
            map.put("code",200);
            map.put("msg","新增成功");
            return map;
        }
        map.put("code",500);
        map.put("msg","新增失败");
        return map;

    }


}

