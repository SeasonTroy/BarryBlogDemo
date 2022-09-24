package com.bootdang.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bootdang.system.entity.Power;
import com.bootdang.system.entity.Tag;
import com.bootdang.system.entity.UserTag;
import com.bootdang.system.service.ITagService;
import com.bootdang.system.service.IUserTagService;
import com.bootdang.util.ShiroUtils;
import com.bootdang.util.SystemColumnUrl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/admin/tager")
public class TagController extends SystemColumnUrl {

    @Autowired
    ITagService tagservice;

    @Autowired
    IUserTagService userTagService;

    @RequestMapping()
    public String index(@RequestParam(required = false,defaultValue = "1") Integer page,@RequestParam(required = false,defaultValue = "10")Integer limit,Map<String,Object> map){

        PageHelper.startPage(page,limit);
        List<Tag> list = tagservice.list();
        PageInfo<Tag> objectPageInfo = new PageInfo<>( list);
        map.put("list",list);
        map.put("pageinfo",objectPageInfo);
        map.put("daohang",select(null));
        return "admin/page/tag/admin-tag";
    }

    /**
     * 标签新增
     * @param title
     * @param map
     * @return
     */
    @RequiresPermissions(value = "tag_insert")
    @PostMapping("/insert")
    public String insert(String title,Map<String,Object> map){
        Tag tag = new Tag().setTitle(title).setCreatetime(LocalDateTime.now()).setCreateuserid(ShiroUtils.getUserId());
        boolean save = tagservice.save(tag);
        Integer tagId = tag.getTagId();
        return  index((tagId%10)-1,10,map);
    }

    /**
     * 单个删除
     * @param id
     * @return
     */
    @RequiresPermissions(value = "tag_delete")
    @ResponseBody
    @PostMapping(value = "delete")
    public HashMap<String,Object> delete(Integer id){
        HashMap<String, Object> map = new HashMap<>();
        //删除拥有这个标签的用户的标签
        QueryWrapper<UserTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tag_id",id);
        userTagService.remove(queryWrapper);

        boolean b = tagservice.removeById(id);
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
    @RequiresPermissions(value = "tag_delete")
    @ResponseBody
    @PostMapping("/deleteall")
    public Integer deleteall(@RequestParam("datas") String datas){
        String[] split = datas.split(",");
        List<String> strings = Arrays.asList(split);
        strings.forEach((a)->{
            QueryWrapper<UserTag> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tag_id",Integer.parseInt(a));
            userTagService.remove(queryWrapper);
        });
        boolean b = tagservice.removeByIds(strings);
        if(b){
            return 200;
        }
        return 500;

    }
}

