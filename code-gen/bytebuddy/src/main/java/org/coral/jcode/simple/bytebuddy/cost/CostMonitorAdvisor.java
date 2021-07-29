package org.coral.jcode.simple.bytebuddy.cost;

import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author wuhao
 * @createTime 2021-07-27 14:37:00
 */
public class CostMonitorAdvisor {
	@RuntimeType
	public static Object intercept(@SuperCall Callable<?> callable, @Origin Method method,
								   @AllArguments Object[] args, @This(optional = true) Object object) throws Exception {
		long start = System.currentTimeMillis();
		try {
			return callable.call();
		} finally {
			System.out.println("方法耗时：" + (System.currentTimeMillis() - start) + "ms");
		}
	}
}