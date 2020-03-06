package custom.cron.tool;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class GetCronProperties {
	
	private static String PATH = "./cron.properties";
	
	public static String getCronTime() {

	    String crontime = null;
	    Properties mainProperties = new Properties();
	    FileInputStream file;
	    
	    try {
			file = new FileInputStream(PATH);
			try {
				mainProperties.load(file);
			} catch (IOException ioe) {
				System.err.println("\t Error: File "+PATH+" io exception.");
				System.exit(1);
			}
		    try {
				file.close();
			} catch (IOException ioe) {
				System.err.println("\t Error: File "+PATH+" io exception.");
				System.exit(1);
			}
		    try {
		    	crontime = mainProperties.getProperty("cron.time");
		    	return crontime;
		    } catch (Exception e) {
		    	System.err.println("\t Error: property 'cron.time' not set???");
				System.exit(1);
		    }
		} catch (FileNotFoundException fnfe) {
			System.err.println("\t Error: File "+PATH+" not found.");
			System.exit(1);
		}
	    return null;

	}

	public static String getJobName() {

	    String jobname = null;
	    Properties mainProperties = new Properties();
	    FileInputStream file;
	    
	    try {
			file = new FileInputStream(PATH);
			try {
				mainProperties.load(file);
			} catch (IOException ioe) {
				System.err.println("\t Error: File "+PATH+" io exception.");
				System.exit(1);
			}
		    try {
				file.close();
			} catch (IOException ioe) {
				System.err.println("\t Error: File "+PATH+" io exception.");
				System.exit(1);
			}
		    try {
		    	jobname = mainProperties.getProperty("job.name");
		    	return jobname;
		    } catch (Exception e) {
		    	System.err.println("\t Error: property 'job.name' not set???");
				System.exit(1);
		    }
		} catch (FileNotFoundException fnfe) {
			System.err.println("\t Error: File "+PATH+" not found.");
			System.exit(1);
		}
	    return null;

	}
		
	public static String getJobGroup() {

	    String jobgroup = null;
	    Properties mainProperties = new Properties();
	    FileInputStream file;
	    
	    try {
			file = new FileInputStream(PATH);
			try {
				mainProperties.load(file);
			} catch (IOException ioe) {
				System.err.println("\t Error: File "+PATH+" io exception.");
				System.exit(1);
			}
		    try {
				file.close();
			} catch (IOException ioe) {
				System.err.println("\t Error: File "+PATH+" io exception.");
				System.exit(1);
			}
		    try {
		    	jobgroup = mainProperties.getProperty("job.group");
		    	return jobgroup;
		    } catch (Exception e) {
		    	System.err.println("\t Error: property 'job.group' not set???");
				System.exit(1);
		    }
		} catch (FileNotFoundException fnfe) {
			System.err.println("\t Error: File "+PATH+" not found.");
			System.exit(1);
		}
	    return null;

	}

}