package com.bootdang.quartz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bootdang.system.entity.Task;
import com.bootdang.system.service.ITaskService;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.management.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


@Component
public class QuartzManager {

    static Scheduler  scheduler;

    @Autowired
    ITaskService taskService;

    @Autowired
    public QuartzManager(Scheduler scheduler){
       this.scheduler=scheduler;
    }
     public static void addjob(Task task) {
         try {
         Class<? extends Job > classs = (Class<? extends Job>)(Class.forName(task.getClassmethod()));
         JobDetail job = JobBuilder.newJob(classs).withIdentity(task.getJobName(), task.getJobGroup()).build();
         CronTrigger trigger = TriggerBuilder.newTrigger()
                 .startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND))
                 .withIdentity(task.getJobName(), task.getJobGroup())
                 .withSchedule(CronScheduleBuilder.cronSchedule(task.getCron()))
                 .startNow()
                 .build();

             scheduler.scheduleJob(job,trigger);
             if (!scheduler.isShutdown()) {
                 scheduler.start();
             }
         } catch (Exception e) {
             e.printStackTrace();
         }

     }
     public static Integer schedulecount(){
         Set<JobKey> jobKeys=null;
         try {
            jobKeys = scheduler.getJobKeys(GroupMatcher.anyGroup());
         } catch (SchedulerException e) {
             e.printStackTrace();
         }
         if(jobKeys!=null)
         { return jobKeys.size();}
         else{
             return 0;
         }
     }


    public void initSchedule() throws SchedulerException {
        List<Task> state = taskService.list(new QueryWrapper<Task>().eq("state", "1"));
        state.forEach((a)->{
            addjob(a);
        });

    }

    /**
     * 暂停一个
     * @param task
     */
     public static void paustjob(Task task){
         JobKey jobKey = JobKey.jobKey(task.getJobName(), task.getJobGroup());
         try {
             scheduler.pauseJob(jobKey);
         } catch (SchedulerException e) {
             e.printStackTrace();
         }
     }

    /**
     * 恢复一个
     * @param task
     */
     public static void resumejob(Task task){
         JobKey jobKey = JobKey.jobKey(task.getJobName(), task.getJobGroup());
         try {
             scheduler.resumeJob(jobKey);
         } catch (SchedulerException e) {
             e.printStackTrace();
         }
     }

    /**
     * 删除一个
     */
    public static void delete(Task task) {
        JobKey jobKey = JobKey.jobKey(task.getJobName(), task.getJobGroup());
        if (jobKey != null) {
            try {
                scheduler.deleteJob(jobKey);
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 更新job主要跟新trigger cron
     * @param task
     */
    public  static void updatejob(Task task){
        TriggerKey triggerKey = TriggerKey.triggerKey(task.getJobName(), task.getJobGroup());
        CronTrigger build = TriggerBuilder.newTrigger().
                withSchedule(CronScheduleBuilder.cronSchedule(task.getCron()))
                .withIdentity(triggerKey).build();
        try {
            scheduler.rescheduleJob(triggerKey,build);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }



}
