package org.coral.jcode.simple.bytebuddy.reload;

public class Log4j {

    /**
     * 注意代理类要和原实现类的方法声明保持一致
     * @param a
     */
    public static void log(String a) {
        System.err.println("Log4j: " + a);
    }

}