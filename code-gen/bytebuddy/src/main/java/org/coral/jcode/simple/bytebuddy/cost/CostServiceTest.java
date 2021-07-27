package org.coral.jcode.simple.bytebuddy.cost;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * @author wuhao
 * @createTime 2021-07-27 14:37:00
 */
public class CostServiceTest {
	public static void main(String[] args) throws Exception {
		CostService service = new ByteBuddy()
				.subclass(CostService.class)
				.method(ElementMatchers.any())
				.intercept(MethodDelegation.to(CostMonitorAdvisor.class))
				.make()
				.load(CostService.class.getClassLoader())
				.getLoaded()
				.newInstance();
		service.play(11111);
	}

}
