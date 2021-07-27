package org.coral.jcode.simple.bytebuddy.annotation;

public class BizAnnotationService {
	@Monitor
    public int foo(int value) {
        System.out.println("foo: " + value);
        return value;
    }

    public int bar(int value) {
        System.out.println("bar: " + value);
        return value;
    }
}