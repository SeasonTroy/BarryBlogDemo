package com.bootdang.system.controller;


import com.bootdang.quartz.QuartzManager;
import com.bootdang.system.entity.Article;
import com.bootdang.system.entity.Links;
import com.bootdang.system.entity.Task;
import com.bootdang.system.service.ITaskService;
import com.bootdang.util.ShiroUtils;
import com.bootdang.util.SystemColumnUrl;
import lombok.RequiredArgsConstructor;
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
 *  前端控制器
 * </p>
 */
@Controller
@RequestMapping("/admin/task")
public class TaskController {
    @Resource
    SystemColumnUrl systemColumnUrl;
    @Autowired
    ITaskService taskService;

    @RequestMapping()
    public String index(Model model){
        model.addAttribute("daohang",systemColumnUrl.select(null));
        model.addAttribute("countjob",QuartzManager.schedulecount());
        return "admin/page/task/task_list";
    }

    @ResponseBody
    @GetMapping("/selectall")
    public List<Task> selectall(){
        List<Task> list = taskService.list();
        return list;

    }

    @GetMapping("/add")
    public String add(){
        return "admin/page/task/task_add";
    }

    @RequiresPermissions("task-insert")
    @ResponseBody
    @PostMapping("/insert")
    public Map<String,Object> insert(Task task){
        Map<String, Object> map = new HashMap<>();
        if(task.getState()==null){
            task.setState("0");
        }
        boolean save = taskService.save(task.setCreatetime(LocalDateTime.now()).setCreateuserid(ShiroUtils.getUserId()));

        if(save){
            if(task.getState().equals("1")){
                QuartzManager.addjob(task);
            }
            map.put("code",200);
            map.put("msg","新增成功");
            return map;

        }
        map.put("code",500);
        map.put("msg","新增失败");
        return map;
    }

    /*
    * 删除调度任务
    * */
    @RequiresPermissions("task-delete")
    @ResponseBody
    @PostMapping("/delete")
    public Map<String,Object> delete(Integer taskid){
        HashMap<String, Object> map = new HashMap<>();
        Task byId = taskService.getById(taskid);
        boolean b = taskService.removeById(taskid);
        if(b){
            QuartzManager.delete(byId);//删除调度
            map.put("code",200);
            map.put("msg","删除成功");
            return map;
        }
        map.put("code",500);
        map.put("msg","删除失败");
        return map;
    }
/*修改任务状态*/
    @RequiresPermissions("task-update")
    @ResponseBody
    @PostMapping("/state")
  public Map<String,Object> state(Integer id,boolean state){
        HashMap<String, Object> result = new HashMap<>();
        if(id==null||id.equals(0)){
            result.put("code",500);
            result.put("msg","修改失败");
            return result;
        }
        String statec=state?"1":"0";
        boolean b = taskService.saveOrUpdate(new Task().setTaskId(id).setState(statec));
        if(b){
            Task byId = taskService.getById(id);
            if(state){
                QuartzManager.addjob(byId);//开启
            }else{
                QuartzManager.delete(byId);//停止调度
            }
            result.put("code",200);
            result.put("msg","修改成功");
            return result;
        }
        result.put("code",500);
        result.put("msg","修改失败");
        return result;
    }

    @GetMapping("/edit")
    public String edit(Integer id,Model model){
        Task byId = taskService.getById(id);
        model.addAttribute("task",byId);
        return "admin/page/task/task_edit";
    }

    @RequiresPermissions("task-update")
    @ResponseBody
    @PostMapping("/update")
    public Map<String,Object> update(Task task){
        HashMap<String, Object> map = new HashMap<>();

        task.setUpdatetime(LocalDateTime.now());
        boolean b = taskService.saveOrUpdate(task);
        if(b){
            Task byId = taskService.getById(task.getTaskId());
            if(byId.getState().equals("1")) {//允许中再修改
                QuartzManager.updatejob(byId);
            }
            map.put("code",200);
            map.put("msg","修改成功");
            return map;
        }
        map.put("code",500);
        map.put("msg","修改失败");
        return map;

    }

}

