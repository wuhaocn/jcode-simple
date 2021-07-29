package org.coral.jcode.simple.bytebuddy.reload;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

public class LogMain {

    public static void main(String[] args) {
        // 替换
        ByteBuddyAgent.install();
        new ByteBuddy().redefine(Log.class)
                .method(ElementMatchers.named("log"))
                .intercept(MethodDelegation.to(Log4j.class))
                .make()
                .load(Thread.currentThread().getContextClassLoader(), ClassReloadingStrategy.fromInstalledAgent());

        // 调用
        Log.log("hello");
    }

}