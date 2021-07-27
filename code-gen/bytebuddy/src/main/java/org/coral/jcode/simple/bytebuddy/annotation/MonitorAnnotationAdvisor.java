package org.coral.jcode.simple.bytebuddy.annotation;

import net.bytebuddy.asm.Advice;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

class MonitorAnnotationAdvisor {
    @Advice.OnMethodEnter
    public static void onMethodEnter(@Advice.Origin Method method, @Advice.AllArguments Object[] arguments) {
		if (method.getAnnotation(Monitor.class) != null) {
			System.out.println("onMethodEnter " + method.getName() + " with arguments: " + Arrays.toString(arguments));
		}
    }

    @Advice.OnMethodExit
    public static void onMethodExit(@Advice.Origin Method method, @Advice.AllArguments Object[] arguments, @Advice.Return Object ret) {
		if (method.getAnnotation(Monitor.class) != null) {
			System.out.println("onMethodExit " + method.getName() + " with arguments: " + Arrays.toString(arguments));
		}
    }
}