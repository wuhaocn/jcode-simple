package org.coral.jcode.simple.bytebuddy.all;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * @author wuhao
 * @createTime 2021-07-27 14:37:00
 */
public class BizServiceTest {
	public static void main(String[] args) throws Exception {
		BizService service = new ByteBuddy()
				.subclass(BizService.class)
				.method(ElementMatchers.any())
				.intercept(Advice.to(MonitorAdvisor.class))
				.make()
				.load(BizService.class.getClassLoader())
				.getLoaded()
				.newInstance();
		service.bar(11111);
		service.foo(99999);
	}

}
