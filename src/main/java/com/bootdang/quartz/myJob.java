package com.bootdang.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class myJob implements Job {
    private static final Logger log=LoggerFactory.getLogger(myJob.class);

    @Override
    public void execute (JobExecutionContext jobExecutionContext) throws JobExecutionException {
     log.error("任务执行"+jobExecutionContext.getJobDetail().getKey().getName());
    }
}
