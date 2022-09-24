package com.bootdang.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bootdang.system.entity.Links;
import com.bootdang.system.entity.Notice;
import com.bootdang.system.entity.NoticeUser;
import com.bootdang.system.entity.User;
import com.bootdang.system.service.IDeptService;
import com.bootdang.system.service.INoticeService;
import com.bootdang.system.service.INoticeUserService;
import com.bootdang.system.service.IUserService;
import com.bootdang.util.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
@RequestMapping("/admin/notice")
public class NoticeController {

    @Autowired
    INoticeService noticeService;
    @Autowired
    IUserService userService;
    @Autowired
    INoticeUserService noticeUserService;
    @Autowired
    IDeptService deptService;

    @RequestMapping()
   public String index(Model model){

        List<Notice> list = noticeService.list();
        model.addAttribute("list",list);
        return "admin/page/notice/noticeList";
   }
   @GetMapping("/add")
   public String add(){
        return "admin/page/notice/noticeAdd";
   }


    @RequiresPermissions("notice-insert")
    @ResponseBody
    @PostMapping("/insert")
    public Map<String,Object> insert(Notice notice){
        Map<String, Object> map = new HashMap<>();
        notice.setCreateuserid(ShiroUtils.getUserId()).setCreatetime(LocalDateTime.now());
        if(notice.getState()==null){
            notice.setState(0);
           //等于0 只新增不发布
            if( noticeService.save(notice)) {
                map.put("code", 200);
                map.put("msg", "发布成功");
                return map;
            }else {
                map.put("code", 500);
                map.put("msg", "发布失败");
                return map;
            }
        }else{
            boolean save = noticeService.save(notice);
            Integer depaid = notice.getDepaid();
            List<User> userList = userService.list(new QueryWrapper<User>().eq("depaid", depaid));
           userList.forEach((a)->{
             noticeUserService.save(new NoticeUser().setNoticeId(notice.getNotId()).setUserId(a.getUserId()).setIsRead(0));//设置需要通知的用户
           });

            if(save) {
                map.put("code", 200);
                map.put("msg", "发布成功");
                return map;
            }else {
                map.put("code", 500);
                map.put("msg", "发布失败");
                return map;
            }
        }
    }
    @RequiresPermissions("notice-insert")
    @ResponseBody
    @PostMapping("/state")
    public Map<String,Object> state(Integer id){
        Map<String, Object> map = new HashMap<>();
        Notice notice = noticeService.getById(id);
        if(notice.getState()==0){
            notice.setState(1);
            noticeService.saveOrUpdate(notice);
            Integer depaid = notice.getDepaid();
            List<User> userList = userService.list(new QueryWrapper<User>().eq("depaid", depaid));
            userList.forEach((a)->{
                noticeUserService.save(new NoticeUser().setNoticeId(notice.getNotId()).setUserId(a.getUserId()).setIsRead(0));//设置需要通知的用户
            });
            map.put("code", 200);
            map.put("msg", "发布成功");
            return map;
        }else {
            map.put("code", 500);
            map.put("msg", "发布失败");
            return map;
        }
    }
    @RequiresPermissions("notice-delete")
    @ResponseBody
    @PostMapping("/delete")
    public Map<String,Object> delete(Integer id){
        HashMap<String, Object> map = new HashMap<>();
        noticeUserService.remove(new QueryWrapper<NoticeUser>().eq("notice_id",id));
        boolean b = noticeService.removeById(id);
        if(b){
            map.put("code",200);
            map.put("msg","删除成功");
            return map;
        }
        map.put("code",500);
        map.put("msg","删除失败");
        return map;
    }
    @RequiresPermissions("notice-delete")
    @ResponseBody
    @PostMapping("/deleteall")
    public Integer deleteall(String datas){
        String[] split = datas.split(",");
        List<String> strings = Arrays.asList(split);
        strings.forEach((a)->{
            noticeUserService.remove(new QueryWrapper<NoticeUser>().eq("notice_id",Integer.parseInt(a)));
        });
        boolean b = noticeService.removeByIds(strings);
        if(b){
            return 200;
        }
        return 500;
    }


    @GetMapping("/edit")
   public String edit(Integer id,Model model){
        Notice notice = noticeService.getById(id);
        model.addAttribute("notice",notice);
        model.addAttribute("deptname",deptService.getById(notice.getDepaid()).getName());
        return "admin/page/notice/noticeEdit";
   }

    @RequiresPermissions("notice-update")
    @ResponseBody
    @PostMapping("/update")
    public HashMap<String,Object> update(Notice notice){
        HashMap<String, Object> map = new HashMap<>();
        if(notice.getState()!=null&&notice.getState()==1){
            Integer depaid = notice.getDepaid();
            List<User> userList = userService.list(new QueryWrapper<User>().eq("depaid", depaid));
            userList.forEach((a)->{
                noticeUserService.save(new NoticeUser().setNoticeId(notice.getNotId()).setUserId(a.getUserId()).setIsRead(0));//设置需要通知的用户
            });
        }
        boolean b = noticeService.saveOrUpdate(notice);
        if(b) {
            map.put("code",200);
            return map;
        }
        map.put("code",500);
        return map;
    }

    public static void main (String[] args) {

    }
}

