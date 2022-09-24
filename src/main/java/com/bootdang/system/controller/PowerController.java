package com.bootdang.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bootdang.system.entity.Menu;
import com.bootdang.system.entity.Power;
import com.bootdang.system.entity.PowerRole;
import com.bootdang.system.entity.Role;
import com.bootdang.system.service.IMenuService;
import com.bootdang.system.service.IPowerRoleService;
import com.bootdang.system.service.IPowerService;
import com.bootdang.system.service.IRoleService;
import com.bootdang.util.SystemColumnUrl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  权限前端控制器
 * </p>
 */
@Controller
@RequestMapping("/admin/power")
public class PowerController {

      @Autowired
      IMenuService menuService;

      @Resource
      SystemColumnUrl systemColumnUrl;

      @Autowired
      IPowerService iPowerService;

      @Autowired
      IPowerRoleService powerRoleService;

    /**
     * 默认页
     * @param model
     * @return
     */
     @RequestMapping()
     public String index(@RequestParam(required = false,defaultValue = "1") Integer page,@RequestParam(required = false,defaultValue = "10")Integer limit,Model model){

          QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
          queryWrapper.isNotNull("url");//查询出不等于空的栏目做为权限的类别
          List<Menu> list = menuService.list(queryWrapper);
          model.addAttribute("list",list);
           //分页
         PageHelper.startPage(page,limit);
         List<Power> powerlist = iPowerService.MySelectByAllMenu();
         PageInfo<Power> objectPageInfo = new PageInfo<>(powerlist);
         model.addAttribute("pageinfo",objectPageInfo);

         model.addAttribute("powerlist",powerlist);
         List<Map<String, String>> select = systemColumnUrl.select(null);//导航标签
         model.addAttribute("daohang",select);
         return "admin/page/power/admin-rule";
     }

    /**
     * 新增
     * @param power
     * @param model
     * @return
     */
     @RequiresRoles(value = "systemadmin")
     @PostMapping("/insert")
     public String insert(Power power,Model model){
         boolean save = iPowerService.save(power);

         return index(1,10,model);

     }

    /**
     * 修改跳转
     * @param id
     * @param model
     * @return
     */
    @RequiresRoles(value = "systemadmin")
     @GetMapping("/edit")
    public String edit(Integer id,Model model){
         QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
         queryWrapper.isNotNull("url");
         List<Menu> list = menuService.list(queryWrapper);
         model.addAttribute("list",list);
         Power byId = iPowerService.getById(id);
         model.addAttribute("power",byId);

         return "admin/page/power/power_edit";
     }

    /**
     * 修改
     * @param power
     * @return
     */
    @RequiresRoles(value = "systemadmin")
     @ResponseBody
     @PostMapping("/update")
     public Map<String,Object> update(Power power){
         HashMap<String, Object> map = new HashMap<>();
         boolean b = iPowerService.saveOrUpdate(power);
         if(b){
             map.put("code",200);
             map.put("msg","修改成功");
             return map;
         }
         map.put("code",500);
         map.put("msg","修改失败");
         return map;

     }

    /**
     * 删除
     * @param id
     * @return
     */
    @RequiresRoles(value = "systemadmin")
    @ResponseBody
    @PostMapping("/delete")
    public Map<String,Object> delete(Integer id){
        Map<String,Object> map=new HashMap<>();
        //删除权限前先删除权限对应的角色权限
        QueryWrapper<PowerRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pow_id",id);
        powerRoleService.remove(queryWrapper);

        boolean b = iPowerService.removeById(id);
        if(b){
            map.put("code",200);
            map.put("msg","删除成功");
            return map;
        }
        map.put("code",500);
        map.put("msg","删除失败");
        return map;
    }

    /**
     * 批量删除
     * @param datas
     * @return
     */
    @RequiresRoles(value = "systemadmin")
    @ResponseBody
    @PostMapping("/deleteall")
    public Integer deleteall(String datas){
        String[] split = datas.split(",");
        //log.error(split.toString());
        List<String> strings = Arrays.asList(split);
         strings.stream().map((a)->Integer.parseInt(a)).forEach((b)->{
             QueryWrapper<PowerRole> queryWrapper = new QueryWrapper<>();
             queryWrapper.eq("pow_id",b);
             powerRoleService.remove(queryWrapper);
         });

        boolean b = iPowerService.removeByIds(strings);
        if(b){
            return 200;
        }
        return 500;
    }
}

