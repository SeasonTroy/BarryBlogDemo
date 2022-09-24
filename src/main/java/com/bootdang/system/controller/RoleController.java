package com.bootdang.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bootdang.shiro.myRealm;
import com.bootdang.system.entity.*;
import com.bootdang.system.service.*;
import com.bootdang.util.MyPage;
import com.bootdang.util.ShiroUtils;
import com.bootdang.util.SystemColumnUrl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 */
@Controller
@RequestMapping("/admin/role")
public class RoleController {

    @Autowired
    SystemColumnUrl systemColumnUrl;

    @Autowired
    IRoleService roleService;

    @Autowired
    IMenuService menuService;

    @Autowired
    IPowerService iPowerService;

    @Autowired
    IPowerRoleService iPowerRoleService;

    @Autowired
    IRoleUserService roleUserService;

    @Resource
    com.bootdang.shiro.myRealm myRealm;

    @RequestMapping()
    public String index(@RequestParam(required = false,defaultValue = "1") Integer page,@RequestParam(required = false,defaultValue = "10")Integer limit, Model model){
        List<Map<String, String>> select = systemColumnUrl.select(null);

        PageHelper.startPage(page,limit);
        List<Role> list = roleService.list();
        PageInfo<Role> objectPageInfo = new PageInfo<>(list);
        model.addAttribute("pageinfo",objectPageInfo);
        model.addAttribute("list",list);
        model.addAttribute("daohang",select);
        return "admin/page/role/admin-role";
    }

    @RequiresRoles(value = "systemadmin")
    @ResponseBody
    @PostMapping(value = "/updatestate")
    public Map<String,Object> updatestate(Integer roleid,Integer state){
        HashMap<String,Object> objectObjectHashMap = new HashMap<>();
        Role role = new Role();
        role.setRoleId(roleid);
        role.setState(state);
        boolean save =roleService.saveOrUpdate(role);
        if(save){
            objectObjectHashMap.put("code",200);
            objectObjectHashMap.put("msg","修改成功");
            return objectObjectHashMap;
        }

        objectObjectHashMap.put("code",500);
        objectObjectHashMap.put("msg","修改失败");


        return objectObjectHashMap;
    }

    @RequiresRoles(value = "systemadmin")
    @GetMapping("/add")
    public String add(Model model){
        model.addAttribute("maps",powerMaplist());
        return "admin/page/role/role-add";
    }


    @RequiresRoles(value = "systemadmin")
    @ResponseBody
    @PostMapping("/insert")
    public HashMap<String,Object> insert(Role role){
        HashMap<String, Object> map = new HashMap<>();
        role.setCreatetime(LocalDateTime.now())
                .setCreateuserid(ShiroUtils.getUser().getUserId())
                .setUpdatetime(LocalDateTime.now());
        Integer savec = roleService.insert(role);
        if(savec.intValue()>0) {
            Integer[] powers = role.getPowers();
            if(powers!=null&&powers.length>0){
            Arrays.stream(powers).forEach((powid)->{
                    iPowerRoleService.save(new PowerRole().setPowId(powid).setRoleId(role.getRoleId()));
            });
            }
            map.put("code",200);
            return map;
        }
        map.put("code",500);
        return map;


    }
    @RequiresRoles(value = "systemadmin")
    @ResponseBody
    @PostMapping("/delete")
    public Map<String,Object> delete(Integer id){
        Map<String,Object> map=new HashMap<>();

        QueryWrapper<RoleUser> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("role_id",id);
        roleUserService.remove(queryWrapper1);


        QueryWrapper<PowerRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id",id);
        iPowerRoleService.remove(queryWrapper);
        boolean b = roleService.removeById(id);
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

        strings.stream().forEach((a)->{//删除角色拥有的和用户和权限
            QueryWrapper<RoleUser> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("role_id",Integer.parseInt(a));
            roleUserService.remove(queryWrapper1);
            QueryWrapper<PowerRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("role_id",Integer.parseInt(a));
            iPowerRoleService.remove(queryWrapper);
        });

        boolean b = roleService.removeByIds(strings);
        if(b){
            return 200;
        }
        return 500;
    }

    @RequiresRoles(value = {"systemadmin","admin"},logical = Logical.OR)
    @GetMapping("/edit")
    public String edit(Integer id,Model model){
        Integer userId = ShiroUtils.getUser().getUserId();
        List<Role> roles = roleService.selectRoleUserid(userId);
        Optional<Role> systemadmin = roles.stream().filter((a) -> {
            return a.getSign().equals("systemadmin");
        }).findFirst();
        if(!systemadmin.isPresent()){//除了超级管理员 普通管理员只能管理自己的jio蛇
            Role by = roleService.getById(id);
            if(!roles.contains(by)){
                return "redirect:/unauthorized";
            }
        }

        Role byId = roleService.getById(id);
        QueryWrapper<PowerRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id",id);
        List<PowerRole> list = iPowerRoleService.list(queryWrapper);
        List<Integer> collect = list.stream().map((a) -> a.getPowId()).collect(Collectors.toList());//获取修改角色所拥有的权限id
        model.addAttribute("maps",powerMaplist());
        model.addAttribute("byid",byId);
        model.addAttribute("collect",collect);
        return "admin/page/role/role-edit";
    }

    @RequiresRoles(value = {"systemadmin","admin"},logical = Logical.OR)
    @ResponseBody
    @PostMapping("/update")
    public HashMap<String,Object> update(Role role){
        HashMap<String, Object> map = new HashMap<>();
        role.setUpdatetime(LocalDateTime.now());
        boolean b = roleService.saveOrUpdate(role);
        if(b) {
            /*先删除*/
            QueryWrapper<PowerRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("role_id",role.getRoleId());
            iPowerRoleService.remove(queryWrapper);
            /*再插入*/
            Integer[] powers = role.getPowers();
            if(powers!=null) {
                Arrays.stream(powers).forEach((a) -> {
                    iPowerRoleService.save(new PowerRole().setRoleId(role.getRoleId()).setPowId(a));
                });
            }

            myRealm.clearAllCachedAuthorizationInfo();//清理所有用户授权缓存
            map.put("code",200);
            return map;
        }
        map.put("code",500);
        return map;


    }

    /**
     * 查询处理权限按钮
     * @return
     */
    public LinkedList<Map<String,Object>> powerMaplist(){
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("url");
        List<Menu> list = menuService.list(queryWrapper);
        LinkedList<Map<String,Object>> maps = new LinkedList<>();

        list.stream().forEach((menu)->{
            HashMap<String,Object> map = new HashMap<>();
            QueryWrapper<Power> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("menu_id",menu.getMenuId());
            List<Power> list1 = iPowerService.list(queryWrapper1);
            map.put("menu",menu);
            map.put("list",list1);
            maps.add(map);
        });

        return maps;
    }

}

