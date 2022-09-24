package com.bootdang.quartz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bootdang.system.entity.Article;
import com.bootdang.system.service.IArticleService;
import com.bootdang.util.HttpContextRequestUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.util.List;

@Component
public class ArticleJob implements Job {
    private static final Logger log=LoggerFactory.getLogger(ArticleJob.class);

    @Autowired
    private ServletContext servletContext;
    @Autowired
    IArticleService articleService;

    /**
     * 页面资源推荐
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    public void execute (JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<Article> artnozy =articleService.list(new QueryWrapper<Article>().ne("type", 1).eq("state", 1).orderByDesc("clickcount").last("limit 0,20"));
        List<Article> artzy =articleService.list(new QueryWrapper<Article>().eq("type", 1).eq("state", 1).orderByDesc("clickcount").last("limit 0,20"));
      servletContext.setAttribute("artnozy",artnozy);
      servletContext.setAttribute("artzy",artzy);
      log.error("任务执行");
    }
}
