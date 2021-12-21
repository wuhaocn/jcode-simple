package org.jcode.simple;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.xml.XmlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author wuhao
 */
public class LoggerConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggerConfiguration.class);
	public static final String SERVER_NAME = "SERVER_NAME";
	private final String serverName;

	/**
	 * 初始化
	 */
	public LoggerConfiguration() {
		this.serverName = System.getProperty(SERVER_NAME, "log4j_log");
		String config = null;
		try {
			config = loadContent("sec/log4j/log4jsname.xml");
		} catch (IOException e) {
			LOGGER.error("LoggerConfiguration init Exception{}", serverName, e);
		}
		loadConfig(config);

	}


	private String loadContent(String fileName) throws IOException {
		File file = new File(fileName);
		FileInputStream fileInputStream = new FileInputStream(file);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] temp = new byte[1024];
		int length = 0;
		while ((length = fileInputStream.read(temp)) > 0) {
			outputStream.write(temp, 0, length);
		}
		return new String(outputStream.toByteArray());
	}


	/**
	 * 刷新日志配置
	 */
	private void loadConfig(String config) {
		try {
			config = config.replace("${server.name}", this.serverName);
			LOGGER.info("loadConfig:{} :{}", serverName, config);
			InputStream is = new ByteArrayInputStream(config.getBytes());
			// 构造新配置并应用到LoggerContext
			LoggerContext ctx = (LoggerContext) LogManager.getContext(false);

			ConfigurationSource source = new ConfigurationSource(is);
			XmlConfiguration xmlConfiguration = new XmlConfiguration(ctx, source);
			ctx.start(xmlConfiguration);
			LOGGER.error("logging refresh success...");
		} catch (Exception e) {
			LOGGER.error("logging refresh error.", e);
		}
	}
}
