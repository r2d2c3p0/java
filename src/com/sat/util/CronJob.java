package com.sat.util;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CronJob implements Job {
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Telnet.TelnetMain("www.google.com", "443");
	}
	
}