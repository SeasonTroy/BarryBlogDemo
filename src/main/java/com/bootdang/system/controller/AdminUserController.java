package com.bootdang.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bootdang.system.entity.*;
import com.bootdang.system.mapper.UserMapper;
import com.bootdang.system.service.*;
import com.bootdang.system.service.impl.DefaultAdminuserServiceImpl;
import com.bootdang.util.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.models.auth.In;
import net.sf.ehcache.CacheManager;
import org.apache.ibatis.javassist.bytecode.stackmap.BasicBlock;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.swing.*;
import javax.validation.constraints.Size;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.System;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * 管理员控制器
 */

@Controller
@RequestMapping("/admin/administrators")
public class AdminUserController {

    @Resource
    SystemColumnUrl systemColumnUrl;

    @Autowired
    IUserService userService;

    @Autowired
    IRoleService roleService;

    @Autowired
    IRoleUserService roleUserService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    IDefaultAdminuserService defaultAdminuserService;

    @Autowired
    ITagService tagService;

    @Autowired
    IUserTagService userTagService;

    @Autowired
    FileAdd fileAdd;

    @Autowired
    IDeptService deptService;

    @RequestMapping()
    public String index(@RequestParam(required = false,defaultValue = "1") Integer page,@RequestParam(required = false,defaultValue = "10")Integer limit,Model model){

        List<Map<String, String>> select = systemColumnUrl.select(null);


        PageHelper.startPage(page,limit);
        List<User> list = userService.selectByAllRole();
        PageInfo<User> objectPageInfo = new PageInfo<>(list);

        model.addAttribute("userlist",list);
        model.addAttribute("pageinfo",objectPageInfo);
        model.addAttribute("daohang",select);
        return "admin/page/adminuser/admin-list";
    }

    /**
     * 添加页面跳转
     * @return
     */

    @RequiresRoles(value = "systemadmin")
    @GetMapping("/add")
    public String add(Model model){
        List<Role> list = roleService.list();
        model.addAttribute("rolelist",list);
        return "admin/page/adminuser/admin-add";
    }

    /**
     * 新增管理员 同时赋默认值
     * @param user
     * @return
     */
    @RequiresRoles(value = "systemadmin")
    @ResponseBody
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    public Map<String,Object> install(User user){
        Map<String,Object> map=new HashMap<>();
        String username = user.getUsername();
        User user1 = userMapper.SelectByName(username);
        if(user1!=null){
           map.put("code",500);
           map.put("msg","用户名已经存在");
           return map;
        }
        User user2 = userMapper.SelectByEmail(user.getEmail());
        if(user2!=null){
            map.put("code",500);
            map.put("msg","邮箱已经被注册");
            return map;
        }
        SimpleHash simpleHash=new SimpleHash("MD5", user.getPassword(), user.getUsername(),20);
        user.setPassword(simpleHash.toString());
        user.setCreatetime(LocalDateTime.now());
        User of = User.of();
        BeanUtils.copyProperties(ShiroUtils.getSubjct().getPrincipal(),of);
        user.setCreateuserid(of.getUserId());
        user.setLastlogin(LocalDateTime.now());
        user.setState("1");
        user.setSex("不明");
        DefaultAdminuser defaultAdminuser = defaultAdminuserService.list().get(0);
        user.setLogo(defaultAdminuser.getDefaultadminlogo());
        user.setContext(defaultAdminuser.getDefaultContext());
        user.setIsAdmin(1);
        int insert = userMapper.insert(user);
        if(insert>0){
            List<Role> role = user.getRole();
            role.stream().filter((a)-> (a.getRoleId()!=null&&!(a.getRoleId().equals(0)))).forEach((r)->{
                RoleUser roleUser = new RoleUser().setRoleId(r.getRoleId()).setUserId(user.getUserId());
                roleUserService.save(roleUser);
            });//过滤判断
            map.put("code",200);
            map.put("msg","新增成功");
         return map;
        }else{
            map.put("code",500);
            map.put("msg","新增失败");
            return map;
        }

    }

    /**
     * 管理员控状态修改
     * @param userid
     * @param state
     * @return
     */
    @RequiresRoles(value = "systemadmin")
    @ResponseBody
    @PostMapping(value = "/updatestate")
    public Map<String,Object> updatestate(Integer userid,String state){
        HashMap<String,Object> objectObjectHashMap = new HashMap<>();
        User user = User.of().setUserId(userid).setState(state);
        boolean save = userService.saveOrUpdate(user);
        if(save){
            objectObjectHashMap.put("code",200);
            objectObjectHashMap.put("msg","修改成功");
            return objectObjectHashMap;
        }

        objectObjectHashMap.put("code",500);
        objectObjectHashMap.put("msg","修改失败");


        return objectObjectHashMap;
    }

    /**
     * 管理员控修改跳转
     * @param id
     * @param model
     * @return
     */
    @RequiresRoles(value = "systemadmin")
    @GetMapping(value = "/edit")
    public String edit(Integer id,Model model){
        User byId = userService.selectByUserRole(id);
        List<Role> list = roleService.list();
        Dept dept = deptService.getById(byId.getDepaid());
        if(dept!=null){
            model.addAttribute("deptname",dept.getName());
        }

        model.addAttribute("useradmin",byId);
        model.addAttribute("rolelist",list);
        return "admin/page/adminuser/admin-edit";
    }

    /**
     * 管理员修改
     * @param user
     * @return
     */
    @RequiresRoles(value = "systemadmin")
    @ResponseBody
    @PostMapping("/update")
    public Map<String,Object> update(User user){
        HashMap<String, Object> map = new HashMap<>();
        Integer userId = user.getUserId();

      //验证判断
        User byId = userService.getById(userId);
        user.setUpdatetime(LocalDateTime.now());
        if(byId==null){
           map.put("code",500);
           map.put("msg","非法修改用户不存在");
        }

        //修改前先删除用户角色
        QueryWrapper<RoleUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        roleUserService.remove(queryWrapper);

        //再添加用户角色
        List<Role> role = user.getRole();
        role.stream().filter((a)-> (a.getRoleId()!=null&&!(a.getRoleId().equals(0)))).forEach((r)->{
            RoleUser roleUser = new RoleUser().setRoleId(r.getRoleId()).setUserId(userId);
            roleUserService.save(roleUser);
        });//过滤判断
        boolean b = userService.saveOrUpdate(user);
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
     * 管理员控删除
     * @param id
     * @return
     */
    @RequiresRoles(value = "systemadmin")
    @ResponseBody
    @PostMapping("/delete")
    public Map<String,Object> delete(Integer id){
        Map<String,Object> map=new HashMap<>();
        HashMap<String, Object> stringIntegerHashMap = new HashMap<>();
        stringIntegerHashMap.put("user_id", id);
        roleUserService.removeByMap(stringIntegerHashMap);//删除用户前先删除用户拥有的角色
        boolean b = userService.removeById(id);
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
     * 管理员p量删除
     * @param datas
     * @return
     */
    @RequiresRoles(value = "systemadmin")
    @ResponseBody
    @PostMapping("/deleteall")
    public Integer deleteall(String datas){
        String[] split = datas.split(",");
        //log.error(split.toString());
        for(String id:split) {
            HashMap<String, Object> stringIntegerHashMap = new HashMap<>();
            stringIntegerHashMap.put("user_id", Integer.parseInt(id));
            roleUserService.removeByMap(stringIntegerHashMap);//删除用户前先删除用户拥有的角色
        }
        List<String> strings = Arrays.asList(split);
        boolean b = userService.removeByIds(strings);
        if(b){
            return 200;
        }
        return 500;
    }

    @GetMapping("/admininfo")
    public String admininfo(Integer id,Map<String,Object> map){
        User user = userService.selectByUserRole(id);
        QueryWrapper<UserTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",id);
        List<UserTag> UserTags = userTagService.list(queryWrapper);
        Set<Integer> collect = UserTags.stream().map((usertag) -> usertag.getTagId()).collect(Collectors.toSet());
        List<Tag> tags = tagService.list();
        map.put("tags",tags);
        map.put("UserTags", collect);
        map.put("userinfo",user);
        return "admin/page/adminuser/userInfo";
    }

    @ResponseBody
    @RequestMapping(value = "/infoinsert",method = RequestMethod.POST)
    public Map<String,Object> userinfoinsert(User user){
        Map<String, Object> map = new HashMap<>();
        Integer userId = user.getUserId();
        JSONObject jsonObject = JSON.parseObject(user.getHobby());
        Collection<Object> values = jsonObject.values();
        values.stream().forEach((a)->{
            userTagService.save(new UserTag().setTagId(Integer.parseInt((String)a)).setUserId(userId));
        });

        boolean b = userService.saveOrUpdate(user);
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
     * 跳转图片剪切页面并附带图片的url
     * @param url
     * @param model
     * @return
     */
    @GetMapping("/logoupload")
    public String logoupload(String url,Model model){
        model.addAttribute("url",url);
        return "admin/page/adminuser/logoupload";
    }

    /**
     * 把头像的base64字符串上传图片
     * @return
     */
    @RequiresAuthentication
    @ResponseBody
    @PostMapping("uploadlogo")
    public Map<String,Object> logoup(String imgbase64, HttpSession session) {

        HashMap<String, Object> map = new HashMap<>();
        int userId = ShiroUtils.getUserId();
        if(userId!=0){
            FileOutputStream fileOutputStream = null;
            String imageurl=null;
            try {
                String substring = imgbase64.substring(imgbase64.indexOf(",") + 1);
                byte[] decode = Base64.getDecoder().decode(substring);
             imageurl=new Date().getTime()+"_"+userId+".jpg";
            File file = new File(fileAdd.path+"logo/"+imageurl);
            if(!file.exists()){
               file.createNewFile();
            }
             fileOutputStream = new FileOutputStream(file);
             fileOutputStream.write(decode);
                userService.saveOrUpdate(User.of().setUserId(userId).setLogo("/upload/logo/"+imageurl));
                session.setAttribute("User",ShiroUtils.getUser().setLogo("/upload/logo/"+imageurl));
            } catch (IOException e) {
                e.printStackTrace();
                map.put("code",500);
                return map;
            }finally {
                try {
                    if(fileOutputStream!=null)fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            map.put("url","/upload/logo/"+imageurl);
            map.put("code",200);
            return map;
        }else{
            map.put("code",500);
            return map;
        }
    }

    @GetMapping("/treeView")
    public String treeView(){
        return "/admin/page/dept/deptTree";
    }

    @ResponseBody
    @GetMapping("/obtaintree")
    public  List<Tree<Dept>> obtaintree(){
        List<Tree<Dept>> tree = deptService.getTree();
        return tree;
    }

    public static void main (String[] args) {
        String encode = MyHashPassWordUtile.encode("MD5", "admin", "admin", 20);
        System.out.println(encode);
    }
}
