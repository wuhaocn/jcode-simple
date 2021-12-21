package org.jcode.simple;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author wuhao
 */
public class Log4jDeleteApp3 {

	final static Logger logger = LogManager.getLogger(Log4jDeleteApp3.class);

	public static void main(String[] args) {
		String sName = "Log4jDeleteApp3";
		System.setProperty(LoggerConfiguration.SERVER_NAME, sName);
		LoggerConfiguration loggerConfiguration = new LoggerConfiguration();
		Log4jFileBase.runLog(sName);
	}

}