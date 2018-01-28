package com.sat.util;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class CronTrigger {
	
    public static void main(String[] args) throws Exception {
    	
    	JobDetail job = JobBuilder.newJob(CronJob.class).
    			withIdentity("YER", "web-middleware").
    			build();

    	Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity("YERTrigger", "web-middleware")
				.withSchedule(CronScheduleBuilder.cronSchedule("0 5 17 * * ?"))
				.build();
    	
    	Scheduler scheduler = new StdSchedulerFactory().getScheduler();
    	scheduler.start();
    	scheduler.scheduleJob(job, trigger);
    
    }
}