package custom.cron.tool;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class RunCron {
	
    public static void main(String[] args) throws Exception {
    	
    	String fullCommand = args[0]+" "+args[1];
    	
    	String jobName = GetCronProperties.getJobName();
    	String jobGroup = GetCronProperties.getJobGroup();
    	String triggerName = jobName+"Trigger";
    	String cronTime = GetCronProperties.getCronTime();
    	
    	JobDetail job = JobBuilder.newJob(CronJob.class).
    			withIdentity(jobName, jobGroup).
    			build();

    	Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity(triggerName, jobGroup)
				.withSchedule(CronScheduleBuilder.cronSchedule(cronTime))
				.build();
    	
    	Scheduler scheduler = new StdSchedulerFactory().getScheduler();
    	scheduler.getContext().put("FullCommand", fullCommand);
    	scheduler.start();
    	scheduler.scheduleJob(job, trigger);
    
    }
}