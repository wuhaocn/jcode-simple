bytebuddy-替换类实现

## 1.依赖

```
dependencies {
  implementation group: 'net.bytebuddy', name: 'byte-buddy', version: '1.11.8'
  implementation group: 'net.bytebuddy', name: 'byte-buddy-agent', version: '1.11.8'
}
```

## 2.测试类被替换类

```
package org.coral.jcode.simple.bytebuddy.reload;

public class Log {

    public static void log(String a) {

        System.out.println("Log: " + a);
    }

}
```

## 3.测试类替换目的类

```
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
```

## 4.测试验证类

```
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
```

```
Log4j: hello
```

* 代码参考:
  https://github.com/wuhaocn/jcode-simple/tree/master/code-gen/bytebuddy
  
## 5.参考

https://houbb.github.io/2019/10/30/bytecode-byte-buddy-02-replace