package com.bootdang.home.controller;

import com.bootdang.system.entity.Comment;
import com.bootdang.system.service.ICommentService;
import com.bootdang.util.BuildTree;
import com.bootdang.util.ShiroUtils;
import com.bootdang.util.buildTrweeThree;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CommentController {

    @Autowired
    ICommentService iCommentService;

    @ResponseBody
    @GetMapping("/pinglun/{id}")
    public List<Comment> pinglun(@PathVariable(value = "id") Integer id){
        List<Comment> comments = iCommentService.selectByArticleId(id);
        List<Comment> build = new buildTrweeThree<Comment>().build(comments);
        return build;
    }
    @RequiresUser()
    @ResponseBody
    @PostMapping("/pingluninsert")
    public Map<String,Object> pingluninsert(Comment comment){
        HashMap<String, Object> map = new HashMap<>();
        if(comment.getParentid()==null) {
         comment.setCreatetime(LocalDateTime.now()).setCreateuserid(ShiroUtils.getUserId()).setParentid(0);
        }else {
            comment.setCreatetime(LocalDateTime.now()).setCreateuserid(ShiroUtils.getUserId());
        }
        boolean save = iCommentService.save(comment);
        if(save){
            map.put("code",200);
            map.put("msg","评论成功");
        }else {
            map.put("code",500);
            map.put("msg","评论失败");
        }
        return map;
    }
}
