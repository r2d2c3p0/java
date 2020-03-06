package com.certificate.tracker.utils;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GetLocalTime {
	
	public static String run(long time) {
	    Date date = new Date(time);
	    Format format = new SimpleDateFormat("[MM/dd/yyyy] HH:mm:ss z");
	    //Format format = new SimpleDateFormat("yyyy-MM-dd");
	    return format.format(date);
	}

}