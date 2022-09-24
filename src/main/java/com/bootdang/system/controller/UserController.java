package com.bootdang.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bootdang.system.entity.User;
import com.bootdang.system.service.IUserService;
import com.bootdang.util.SystemColumnUrl;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 */
@Controller
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    SystemColumnUrl systemColumnUrl;
    @Autowired
    IUserService userService;

    @RequestMapping()
    public String index(Model model){
        model.addAttribute("daohang",systemColumnUrl.select(null));
        /*n*/
        model.addAttribute("users",userService.list(new QueryWrapper<User>().ne("is_admin",1)));
        return "admin/page/user/user-list";
    }
    @ResponseBody
    @PostMapping("/state")
    public Map<String,Object> state(Integer id,boolean state){
        Map<String, Object> map = new HashMap<>();
        User user = User.of().setUserId(id).setState(state ? "1" : "0");
        boolean b = userService.updateById(user);
        if(!b){
            map.put("code",500);
            map.put("msg","修改失败");
            return map;
        }
        map.put("code",200);
        map.put("msg","修改成功");
        return map;
    }

    @GetMapping("/addjf")
    public String addjf(Integer id,Model model){
         model.addAttribute("userid",id);
        return "admin/page/user/user_addjf";
    }
    @ResponseBody
    @PostMapping("/insertjf")
    public Map<String,Object> insertjf(User user){
        Map<String, Object> map = new HashMap<>();
        if(user.getUserId()==null){
            map.put("code",500);
            map.put("msg","非法修改");
            return map;
        }
        User byId = userService.getById(user.getUserId());
        user.setJf((user.getJf())+(byId.getJf()));
        boolean b = userService.updateById(user);
        if(!b){
            map.put("code",500);
            map.put("msg","增加失败");
            return map;
        }
        map.put("code",200);
        map.put("msg","增加成功");
        return map;
    }
    @GetMapping("/edit")
    public String edit(Integer id,Model model){
        model.addAttribute("userid",id);
        model.addAttribute("user",userService.getById(id));
        return "admin/page/user/user-edit";
    }
    @ResponseBody
    @PostMapping("/update")
    public Map<String,Object> update(User user){
        HashMap<String, Object> map = new HashMap<>();
        user.setUpdatetime(LocalDateTime.now());
        SimpleHash simpleHash = new SimpleHash("MD5", user.getPassword(), user.getUsername(), 20);
        user.setPassword(simpleHash.toString());
        boolean b = userService.updateById(user);
        if(!b){
            map.put("code",500);
            map.put("msg","修改失败");
            return map;
        }
        map.put("code",200);
        map.put("msg","修改成功");
        return map;
    }

    @ResponseBody
    @PostMapping("/delete")
    public Map<String,Object> delete(Integer id){
        HashMap<String, Object> map = new HashMap<>();
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

    @ResponseBody
    @PostMapping("/deleteall")
    public Integer deleteall(String datas){
        String[] split = datas.split(",");
        List<String> strings = Arrays.asList(split);
        boolean b = userService.removeByIds(strings);
        if(b){
            return 200;
        }
        return 500;
    }
}

