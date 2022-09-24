package com.bootdang.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 实现了CommandLineRunner接口的类会在项目启动后自动执行run方法
 */
@Component
@Order(value = 1)
public class ScheduleJobInitListener implements CommandLineRunner {

	@Autowired
	QuartzManager quartzManager;


	@Override
	public void run(String... arg0) throws Exception {
		try {
			quartzManager.initSchedule();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}