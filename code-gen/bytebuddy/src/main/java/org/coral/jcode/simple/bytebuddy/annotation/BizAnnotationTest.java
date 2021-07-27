package org.coral.jcode.simple.bytebuddy.annotation;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * @author wuhao
 * @createTime 2021-07-27 14:37:00
 */
public class BizAnnotationTest {
	public static void main(String[] args) throws Exception {
		BizAnnotationService service = new ByteBuddy()
				.subclass(BizAnnotationService.class)
				.method(ElementMatchers.any())
				.intercept(Advice.to(MonitorAnnotationAdvisor.class))
				.make()
				.load(BizAnnotationService.class.getClassLoader())
				.getLoaded()
				.newInstance();
		service.bar(11111);
		service.foo(99999);
	}

}
