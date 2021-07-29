---
title: bytebuddy实现aop
categories:
- java
---

## 1.背景
最近业务服务需要做一些组件第三方组件监控的事，需要用到字节码修改相关的技术，bytebuddy就是其中一种，
网上找了一下bytebuddy相关资料，不少大佬写的不错的帖子就直接拿过来了，下方备注参考连接

本文主要介绍 bytebuddy-aop相关操作

## 2.ByteBuddy简介

Byte Buddy 是一个代码生成和操作库，用于在 Java 应用程序运行时创建和修改 Java 类，无需编译器的帮助。
除了Java 类库附带的代码生成实用程序，Byte Buddy 允许创建任意类，并且不限于实现用于创建运行时代理的接口。
此外，Byte Buddy 提供了一个方便的 API，用于手动、使用 Java 代理或在构建期间更改类。

简单来说，ByteBuddy是一个可以在运行时动态生成java class的类库。在这篇文章中，
我们将会使用ByteBuddy这个框架操作已经存在的类，创建指定的新类，甚至拦截方法调用。

官网：https://bytebuddy.net/#/

* 代码地址参考：https://github.com/wuhaocn/jcode-simple.git

## 3.AOP注解实现
### 3.1 依赖引入
 依赖byte-buddy、byte-buddy-agent相关类

```
dependencies {
  implementation group: 'net.bytebuddy', name: 'byte-buddy', version: '1.11.8'
}

```

### 3.2 定义相关类
定义 注解类、业务类、监听类;

* 注解类

```
@Retention(RetentionPolicy.RUNTIME)
public @interface Monitor {
}
```

* 业务类
此处定义监控方法并加上Monitor注解
```
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
```
* 监听类
 实现 @Advice.OnMethodEnter    @Advice.OnMethodExit 监听业务
```
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
```
  
* 测试类

测试注解生效

```
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
```

* 结果输出

```
> Task :code-gen:bytebuddy:BizAnnotationTest.main()
bar: 11111
onMethodEnter foo with arguments: [99999]
foo: 99999
onMethodExit foo with arguments: [99999]

```


## 4.AOP监听第三方组件
### 4.1 定义相关类

定义 业务类、监听类、测试类;

主要原因是调用代码无法增加注解

* 业务类
  此处定义监控方法并加上未添加注解
```
public class BizService {
    public int foo(int value) {
        System.out.println("foo: " + value);
        return value;
    }

    public int bar(int value) {
        System.out.println("bar: " + value);
        return value;
    }
}
```
* 监听类
  实现 @Advice.OnMethodEnter    @Advice.OnMethodExit 监听业务
```
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
```

* 测试类

测试不添加注解验证

```
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
		service.bar(00000);
		service.foo(99999);
	}

}
```

* 结果输出

```
> Task :code-gen:bytebuddy:BizServiceTest.main()
onMethodEnter bar with arguments: [11111]
bar: 11111
onMethodExit bar with arguments: [11111] return: 11111
onMethodEnter foo with arguments: [99999]
foo: 99999
onMethodExit foo with arguments: [99999] return: 99999
```

## 5.监控耗时
### 5.1 定义相关类
* 业务类
耗时处理
```
public class CostService {
    public int play(int value) throws Exception {
        System.out.println("foo: " + value);
        Thread.sleep(1000);
        return value;
    }

}
```

* 监控类
实现	@RuntimeType通过 Object intercept(@SuperCall Callable<?> callable)返回处理结果
```
public class CostMonitorAdvisor {
	@RuntimeType
	public static Object intercept(@SuperCall Callable<?> callable) throws Exception {
		long start = System.currentTimeMillis();
		try {
			return callable.call();
		} finally {
			System.out.println("方法耗时：" + (System.currentTimeMillis() - start) + "ms");
		}
	}
}
```

* 测试类

通过方法委托实现 ByteBuddy#intercept(MethodDelegation.to(CostMonitorAdvisor.class))

```
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
```

* 结果输出

```
> Task :code-gen:bytebuddy:CostServiceTest.main()
play: 11111
方法耗时：35ms

```

* 带参传递
部分构造函数携带参数，这里以redis为例简单写了下带参数传递的类
```
ByteBuddy byteBuddy = new ByteBuddy();
Class aClass = byteBuddy.subclass(Jedis.class)
        .method(ElementMatchers.any())
        .intercept(MethodDelegation.to(RedisMonitorAdvisor.class))
        .make()
        .load(Jedis.class.getClassLoader())
        .getLoaded();
Class[] p = {String.class, int.class};
Constructor<Jedis> classDeclaredConstructor = aClass.getDeclaredConstructor(p);
Jedis jedis = classDeclaredConstructor.newInstance("10.3.4.111", 6379);
        
```



## 6.总结

* 通过ByteBuddy创建实例，并注入切面可实现横切
    * 可执行onMethodEnter onMethodExit相关操作
    * RuntimeType监听方法耗时
* 对象创建需要通过ByteBuddy创建，自己创建类无法实现

* 无法监控静态对象

## 7.注解含义

 注解 |	说明
-----|-----
@Argument	| 绑定单个参数
@AllArguments |	绑定所有参数的数组
@This	| 当前被拦截的、动态生成的那个对象
@Super	| 当前被拦截的、动态生成的那个对象的父类对象
@Origin	| 可以绑定到以下类型的参数：Method 被调用的原始方法 Constructor 被调用的原始构造器 Class 当前动态创建的类 MethodHandle MethodType String 动态类的toString()的返回值 int 动态方法的修饰符
@DefaultCall  |	调用默认方法而非super的方法
@SuperCall  |	用于调用父类版本的方法
@Super  |	注入父类型对象，可以是接口，从而调用它的任何方法
@RuntimeType  |	可以用在返回值、参数上，提示ByteBuddy禁用严格的类型检查
@Empty  |	注入参数的类型的默认值
@StubValue  |	注入一个存根值。对于返回引用、void的方法，注入null；对于返回原始类型的方法，注入0
@FieldValue  |	注入被拦截对象的一个字段的值
@Morph |	类似于@SuperCall，但是允许指定调用参数

* 代码参考:
  https://github.com/wuhaocn/jcode-simple/tree/master/code-gen/bytebuddy
  
## 8.参考

https://zhuanlan.zhihu.com/p/151843984
https://bytebuddy.net/#/
https://www.jianshu.com/p/be2efc2b0e4c
https://blog.csdn.net/generalfu/article/details/106086475