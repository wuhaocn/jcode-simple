package org.jcode.simple;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author wuhao
 */
public class Log4jFileBase {

	final static Logger logger = LogManager.getLogger(Log4jFileBase.class);

	public static void runLog(String parameterParam) {
		String parameter;
		for (int i = 0; i < 1000; ++i) {
			parameter = parameterParam + i;
			System.out.println("log iteration: " + i);
			if (logger.isDebugEnabled()) {
				logger.debug("This is debug : " + parameter);
			}

			if (logger.isInfoEnabled()) {
				logger.info("This is info : " + parameter);
			}

			logger.warn("This is warn : " + parameter);
			logger.error("This is error : " + parameter);
			logger.fatal("This is fatal : " + parameter);
			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}