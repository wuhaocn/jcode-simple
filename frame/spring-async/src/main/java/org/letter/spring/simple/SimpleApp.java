package org.letter.spring.simple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wuhao
 */
@SpringBootApplication
public class SimpleApp {
	static Logger logger = LoggerFactory.getLogger(SimpleApp.class);

	public static void main(String[] args) throws Exception {
//		Log4j2SystemExt.setExtLoggerSystem();
		SpringApplication.run(SimpleApp.class, args);
		logger.info("third party service started suc!");

	}


}
