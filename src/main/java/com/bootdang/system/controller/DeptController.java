package com.bootdang.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bootdang.system.entity.Dept;
import com.bootdang.system.entity.Links;
import com.bootdang.system.entity.Tree;
import com.bootdang.system.service.IDeptService;
import com.bootdang.util.SystemColumnUrl;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.models.auth.In;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  部门前端控制器
 * </p>
 */
@Controller
@RequestMapping("/admin/dept")
public class DeptController {

    @Autowired
    IDeptService deptService;

    @Resource
    SystemColumnUrl systemColumnUrl;

    private final String path = "admin/page/dept";

    @RequestMapping()
    public String index (Model model) {
        model.addAttribute("daohang", systemColumnUrl.select(null));
        return path + "/deptList";
    }

    @ResponseBody
    @GetMapping("/menujson")
    public Map<String, Object> menujson () {
        Map<String, Object> map = new HashMap<>();
        List<Dept> list = deptService.list(new QueryWrapper<Dept>().orderByAsc("num"));

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

    @GetMapping("/add")
    public String add(Model model, Integer parentid){
        if(parentid!=null&&!parentid.equals(0)){
            Dept byId = deptService.getById(parentid);
            model.addAttribute("dept",byId);
        }

        return "admin/page/dept/deptadd";
    }

    @RequiresPermissions("dept_insert")
    @ResponseBody
    @PostMapping("/insert")
    public Map<String,Object> insert(Dept dept){
        Map<String, Object> map = new HashMap<>();
        if(dept.getIsDelete()==null){
            dept.setIsDelete("0");
        }else{
            dept.setIsDelete("1");
        }
        boolean save = deptService.save(dept);
        if(!save){
            map.put("code",500);
            map.put("msg","新增失败");
            return map;
        }
        map.put("code",200);
        map.put("msg","新增成功");
        return map;
    }
    @RequiresPermissions("dept_delete")
    @ResponseBody
    @PostMapping("/delete")
    public Map<String, Object> delete (Integer id) {
        HashMap<String, Object> map = new HashMap<>();
        //List<Dept> parent_ids = deptService.list(new QueryWrapper<Dept>().eq("parent_id", id));
        Boolean deptdele = deptdele(id);

        boolean b = deptService.removeById(id);
        if (b) {
            map.put("code", 200);
            map.put("msg", "删除成功");
            return map;
        }
        map.put("code", 500);
        map.put("msg", "删除失败");
        return map;
    }

    public Boolean deptdele (Integer id) {
        List<Dept> parent_id = deptService.list(new QueryWrapper<Dept>().eq("parent_id", id));
        if (parent_id.isEmpty()) {
            return false;
        }
        parent_id.forEach((v) -> {
            deptdele(v.getDeptId());//遍历删除子部门
            deptService.removeById(v.getDeptId());

        });

        return true;

    }


    @GetMapping("/edit")
    public String edit(Integer id,Model model){
        model.addAttribute("dept",deptService.getById(id));
        return "admin/page/dept/deptdEdit";
    }
   @RequiresPermissions("dept_update")
    @ResponseBody
    @PostMapping("/update")
    public HashMap<String,Object> update(Dept dept){
        HashMap<String, Object> map = new HashMap<>();
        if(dept.getIsDelete()==null){
            dept.setIsDelete("0");
        }else{
            dept.setIsDelete("1");
        }
        boolean b = deptService.saveOrUpdate(dept);
        if(b) {
            map.put("code",200);
            return map;
        }
        map.put("code",500);
        return map;


    }

}

