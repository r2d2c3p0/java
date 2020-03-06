package custom.cron.tool;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;

public class CronJob implements Job {
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		try {
			SchedulerContext schedulerContext = context.getScheduler().getContext();
			String oContext = (String) schedulerContext.get("FullCommand");
			ExecuteScript.ExecuteScriptMain(oContext);
		} catch (SchedulerException se) {
			se.printStackTrace();
		}
		
	}
	
}