package org.letter.spring.simple;

/**
 * @author wuhao
 * @createTime 2021-12-31 15:40:00
 */
public class Log4jXmlConfig {
	public static String log4jXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
			"<Configuration status=\"WARN\">\n" +
			"\t<Properties>\n" +
			"\t\t<Property name=\"PID\">????</Property>\n" +
			"\t\t<Property name=\"LOG_EXCEPTION_CONVERSION_WORD\">%xwEx</Property>\n" +
			"\t\t<Property name=\"LOG_LEVEL_PATTERN\">%5p</Property>\n" +
			"\t\t<Property name=\"LOG_PATTERN\">%clr{%d{yyyy-MM-dd HH:mm:ss.SSS}}{faint} %clr{${LOG_LEVEL_PATTERN}} %clr{${sys:PID}}{magenta} %clr{---}{faint} %clr{[%15.15t]}{faint} %clr{%-40.40c{1.}}{cyan} %clr{:}{faint} %m%n${sys:LOG_EXCEPTION_CONVERSION_WORD}</Property>\n" +
			"\t</Properties>\n" +
			"\t<Appenders>\n" +
			"\t\t<Console name=\"Console\" target=\"SYSTEM_OUT\" follow=\"true\">\n" +
			"\t\t\t<PatternLayout pattern=\"${LOG_PATTERN}\" />\n" +
			"\t\t</Console>\n" +
			"\t</Appenders>\n" +
			"\t<Loggers>\n" +
			"\t\t<Logger name=\"org.apache.catalina.startup.DigesterFactory\" level=\"error\" />\n" +
			"\t\t<Logger name=\"org.apache.catalina.util.LifecycleBase\" level=\"error\" />\n" +
			"\t\t<Logger name=\"org.apache.coyote.http11.Http11NioProtocol\" level=\"warn\" />\n" +
			"\t\t<logger name=\"org.apache.sshd.common.util.SecurityUtils\" level=\"warn\"/>\n" +
			"\t\t<Logger name=\"org.apache.tomcat.util.net.NioSelectorPool\" level=\"warn\" />\n" +
			"\t\t<Logger name=\"org.crsh.plugin\" level=\"warn\" />\n" +
			"\t\t<logger name=\"org.crsh.ssh\" level=\"warn\"/>\n" +
			"\t\t<Logger name=\"org.eclipse.jetty.util.component.AbstractLifeCycle\" level=\"error\" />\n" +
			"\t\t<Logger name=\"org.hibernate.validator.internal.util.Version\" level=\"warn\" />\n" +
			"\t\t<logger name=\"org.springframework.boot.actuate.autoconfigure.CrshAutoConfiguration\" level=\"warn\"/>\n" +
			"\t\t<logger name=\"org.springframework.boot.actuate.endpoint.jmx\" level=\"warn\"/>\n" +
			"\t\t<logger name=\"org.thymeleaf\" level=\"warn\"/>\n" +
			"\t\t<Root level=\"info\">\n" +
			"\t\t\t<AppenderRef ref=\"Console\" />\n" +
			"\t\t</Root>\n" +
			"\t</Loggers>\n" +
			"</Configuration>";
}
