package org.letter.spring.simple;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.springframework.boot.logging.LogFile;
import org.springframework.boot.logging.log4j2.Log4J2LoggingSystem;
import org.springframework.util.Assert;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wuhao
 * @createTime 2021-12-31 14:39:00
 */
public class Log4j2SystemExt extends Log4J2LoggingSystem {
	public static final Map<String, String> SYSTEMS;

	static {
		Map<String, String> systems = new LinkedHashMap<String, String>();
		systems.put("ch.qos.logback.core.Appender",
				"org.springframework.boot.logging.logback.LogbackLoggingSystem");
		systems.put("org.apache.logging.log4j.core.impl.Log4jContextFactory",
				"org.letter.spring.simple.Log4j2SystemExt");
		systems.put("java.util.logging.LogManager",
				"org.springframework.boot.logging.java.JavaLoggingSystem");
		SYSTEMS = Collections.unmodifiableMap(systems);
	}

	private static final String FILE_PROTOCOL = "file";

	public Log4j2SystemExt(ClassLoader classLoader) {
		super(classLoader);
	}

	public static void setExtLoggerSystem() {
		try {
			Class<?> clz = Class.forName("org.springframework.boot.logging.LoggingSystem");
			Field field = clz.getDeclaredField("SYSTEMS");
			field.setAccessible(true);
			Field modifiers = Field.class.getDeclaredField("modifiers");
			modifiers.setAccessible(true);
			modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			System.out.println("before setExtLoggerSystem: " + field.get(null));
			field.set(null, Log4j2SystemExt.SYSTEMS);
			System.out.println("after setExtLoggerSystem: " + field.get(null));
			modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		} catch (Exception e) {
			System.out.println("setExtLoggerSystem:" + e.getMessage());
			e.printStackTrace();
		}

	}

	@Override
	protected void loadConfiguration(String location, LogFile logFile) {
		Assert.notNull(location, "Location must not be null");
		try {
			LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
			InputStream is = new ByteArrayInputStream(Log4jXmlConfig.log4jXml.getBytes(StandardCharsets.UTF_8));
			ConfigurationSource source = new ConfigurationSource(is);
			ctx.start(ConfigurationFactory.getInstance().getConfiguration(ctx, source));
		} catch (Exception ex) {
			throw new IllegalStateException(
					"Could not initialize Log4J2 logging from " + location, ex);
		}
	}
// 原有
//	@Override
//	protected void loadConfiguration(String location, LogFile logFile) {
//		Assert.notNull(location, "Location must not be null");
//		try {
//			LoggerContext ctx =  (LoggerContext) LogManager.getContext(false);
//			URL url = ResourceUtils.getURL(location);
//			InputStream stream = url.openStream();
//			ConfigurationSource configurationSource = null;
//			if (FILE_PROTOCOL.equals(url.getProtocol())) {
//				configurationSource = new ConfigurationSource(stream, ResourceUtils.getFile(url));
//			} else {
//				configurationSource = new ConfigurationSource(stream, url);
//			}
//			ctx.start(ConfigurationFactory.getInstance().getConfiguration(ctx, configurationSource));
//		}
//		catch (Exception ex) {
//			throw new IllegalStateException(
//					"Could not initialize Log4J2 logging from " + location, ex);
//		}
//	}
}
