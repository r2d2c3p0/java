package org.r2d2c3p0.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;

import org.apache.log4j.Logger;

public class GetVMArguments {

	private final static Logger logger = Logger.getLogger("CRGAppLogger");

	/*
	 * program to check if log4j.configuration is present in the JVM arguments.
	*/
	public static boolean checkl4j() {

		logger.debug(" [GetVMArguments.checkl4j] start");
		RuntimeMXBean mBean = ManagementFactory.getRuntimeMXBean();
		List<String> jvmArguments = mBean.getInputArguments();

		for (int iargs = 0; iargs < jvmArguments.size(); iargs++) {
			if(jvmArguments.get(iargs).indexOf("log4j.configuration") != -1) {
				return true;
			}
		}
		logger.debug(" [GetVMArguments.checkl4j] end");
		return false;

	}

	/*
	 * simple class to prints the command line arguments used when launching the JVM.
	*/
	public static void run() {

		logger.debug(" [GetVMArguments] start");
		RuntimeMXBean mbean = ManagementFactory.getRuntimeMXBean();
		List<String> jvmArguments = mbean.getInputArguments();
		for (int iargs = 0; iargs < jvmArguments.size(); iargs++) {
			logger.debug(" [GetVMArguments] jvm argument" +iargs+ " "+jvmArguments.get(iargs));
		}
		logger.debug(" [GetVMArguments] -classpath :" + System.getProperty("java.class.path"));
		logger.debug(" [GetVMArguments] executing :" + System.getProperty("sun.java.command"));
		logger.debug(" [GetVMArguments] end");
	}

}