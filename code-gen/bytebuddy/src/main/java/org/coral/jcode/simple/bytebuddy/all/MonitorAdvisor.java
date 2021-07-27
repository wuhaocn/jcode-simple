package org.coral.jcode.simple.bytebuddy.all;

import net.bytebuddy.asm.Advice;

import java.lang.reflect.Method;
import java.util.Arrays;

class MonitorAdvisor {
    @Advice.OnMethodEnter
    public static void onMethodEnter(@Advice.Origin Method method, @Advice.AllArguments Object[] arguments) {
		System.out.println("onMethodEnter " + method.getName() + " with arguments: " + Arrays.toString(arguments));
    }

    @Advice.OnMethodExit
    public static void onMethodExit(@Advice.Origin Method method, @Advice.AllArguments Object[] arguments, @Advice.Return Object ret) {
		System.out.println("onMethodExit " + method.getName() + " with arguments: " + Arrays.toString(arguments) + " return: " + ret);
    }
}