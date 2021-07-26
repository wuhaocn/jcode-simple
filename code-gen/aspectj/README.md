## 1.AspectJ 使用介绍

AspectJ 作为 AOP 编程的完全解决方案，提供了三种织入时机，分别为
* compile-time：编译期织入，在编译的时候一步到位，直接编译出包含织入代码的 .class 文件
* post-compile：编译后织入，增强已经编译出来的类，如我们要增强依赖的 jar 包中的某个类的某个方法
* load-time：在 JVM 进行类加载的时候进行织入

### 1.1 编译插桩分类

编译插桩技术具体可以分为两类，如下所示：
* 1）、APT（Annotation Process Tools） ：用于生成 Java 代码。
* 2）、AOP（Aspect Oriented Programming）：用于操作字节码。

我们分别来详细介绍下它们的作用。

* 1、APT（Annotation Process Tools）
  总所周知，ButterKnife、Dagger、GreenDao、Protocol Buffers 这些常用的注解生成框架都会在编译过程中生成代码。 
  而 使用 AndroidAnnotation 结合 APT 技术 来生成代码的时机，是在编译最开始的时候介入的。
  但是 AOP 是在编译完成后生成 dex 文件之前的时候，直接通过修改 .class 文件的方式，来直接添加或者修改代码逻辑的。
  使用 APT 技术生成 Java 代码的方式具有如下 两方面 的优势：

  1）、隔离了框架复杂的内部实现，使得开发更加地简单高效。
  2）、大大减少了手工重复的工作量，降低了开发时出错的机率。

*  2、AOP（Aspect Oriented Programming）
   而对于操作字节码的方式来说，一般都在 代码监控、代码修改、代码分析 这三个场景有着很广泛的应用。
   相对于 Java 代码生成的方式，操作字节码的方式有如下 特点：

    1）、应用场景更广。
    2）、功能更加强大。
    3）、使用复杂度较高。
   
## 2.依赖引入
以gradle依赖为例

```
buildscript {
  repositories {
    mavenLocal()
    maven { url 'https://plugins.gradle.org/m2/' }

  }
  dependencies {
    classpath group: 'io.freefair.gradle', name: 'aspectj-plugin', version: '5.3.3.3'

  }
}
apply plugin: "io.freefair.aspectj"


dependencies {
  compile group: 'org.aspectj', name: 'aspectjweaver', version: '1.9.5'
  compile group: 'org.aspectj', name: 'aspectjrt', version: '1.9.5'
 
}


```

* aspectj底层依赖库
    * net.bytebuddy:byte-buddy

## 3.代码编写

* 代码地址请参考：https://github.com/wuhaocn/jcode-simple.git

* 注意下面操作类应放在"src/main/aspectj"包下面

### 3.1.定义业务类
* Account.java
```
public class Account {

    public int balance = 20;

    public boolean pay(int amount) {
        if (balance < amount) {
            return false;
        }
        balance -= amount;
        return true;
    }
}
```
### 3.2.定义注解类
* AccountAspect.aj
```
public aspect AccountAspect {

    pointcut callPay(int amount, Account account):
            call(boolean com.rcloud.Account.pay(int)) && args(amount) && target(account);

    before(int amount, Account account): callPay(amount, account) {
        System.out.println("[AccountAspect]付款前总金额: " + account.balance);
        System.out.println("[AccountAspect]需要付款: " + amount);
    }

    boolean around(int amount, Account account): callPay(amount, account) {
        if (account.balance < amount) {
            System.out.println("[AccountAspect]拒绝付款!");
            return false;
        }
        return proceed(amount, account);
    }

    after(int amount, Account balance): callPay(amount, balance) {
        System.out.println("[AccountAspect]付款后，剩余：" + balance.balance);
    }

}
```
### 3.3.使用类
* AccountDoWork.java
```
public class AccountDoWork {
    public static void pay() {
        Account account = new Account();
        account.pay(1);
    }
}

```
## 4.AspectJ 的优势与局限性
最常用的字节码处理框架有 AspectJ、ASM 等等，它们的相同之处在于输入输出都是 Class 文件。
并且，它们都是 在 Java 文件编译成 .class 文件之后，生成 Dalvik 字节码之前执行。
而 AspectJ 作为 Java 中流行的 AOP（aspect-oriented programming） 编程扩展框架，
其内部使用的是 BCEL框架 来完成其功能。下面，我们就来了解下 AspectJ 具备哪些优势。
### 4.1.AspectJ 的优势
   它的优势有两点：成熟稳定、使用非常简单。
* 1、成熟稳定
字节码的处理并不简单，特别是 针对于字节码的格式和各种指令规则，如果处理出错，
就会导致程序编译或者运行过程中出现问题。而 AspectJ 作为从 2001 年发展至今的框架，
它已经发展地非常成熟，通常不用考虑插入的字节码发生正确性相关的问题。
* 2、使用非常简单
AspectJ 的使用非常简单，并且它的功能非常强大，我们完全不需要理解任何 Java 字节码相关的知识，
就可以在很多情况下对字节码进行操控。例如，它可以在如下五个位置插入自定义的代码：
1）、在方法（包括构造方法）被调用的位置。
2）、在方法体（包括构造方法）的内部。
3）、在读写变量的位置。
4）、在静态代码块内部。
5）、在异常处理的位置的前后。
此外，它也可以 直接将原位置的代码替换为自定义的代码。

### 4.2.AspectJ 的缺陷
而 AspectJ 的缺点可以归结为如下 三点：
* 1、切入点固定
AspectJ 只能在一些固定的切入点来进行操作，如果想要进行更细致的操作则很难实现，它
无法针对一些特定规则的字节码序列做操作。
* 2、正则表达式的局限性
AspectJ 的匹配规则采用了类似正则表达式的规则，比如 匹配 Activity 生命周期的 onXXX 方法，如果有自定义的其他以 on 开头的方法也会匹配到，这样匹配的正确性就无法满足。
* 3、性能较低
AspectJ 在实现时会包装自己一些特定的类，它并不会直接把 Trace 函数直接插入到代码中，而是经过一系列自己的封装。这样不仅生成的字节码比较大，而且对原函数的性能会有不小的影响。如果想对 App 中所有的函数都进行插桩，性能影响肯定会比较大。如果你只插桩一小部分函数，那么 AspectJ 带来的性能损耗几乎可以忽略不计。
## 5.AspectJ 核心语法简介
AspectJ 其实就是一种 AOP 框架，AOP 是实现程序功能统一维护的一种技术。
利用 AOP 可以对业务逻辑的各个部分进行隔离，从而使得业务逻辑各部分之间的耦合性降低，
提高程序的可重用性，同时大大提高了开发效率。因此 AOP 的优势可总结为如下 两点：

1）、无侵入性。
2）、修改方便。

此外，AOP 不同于 OOP 将问题划分到单个模块之中，它把 涉及到众多模块的同一类问题进行了统一处理。
比如我们可以设计两个切面，一个是用于处理 App 中所有模块的日志输出功能，
另外一个则是用于处理 App 中一些特殊函数调用的权限检查。
下面👇，我们就来看看要掌握 AspectJ 的使用，我们需要了解的一些 核心概念。
* 1、横切关注点
对哪些方法进行拦截，拦截后怎么处理。
* 2、切面（Aspect）
类是对物体特征的抽象，切面就是对横切关注点的抽象。
* 3、连接点（JoinPoint）
JPoint 是一个程序的关键执行点，也是我们关注的重点。
  它就是指被拦截到的点（如方法、字段、构造器等等）。
* 4、切入点（PointCut）
对 JoinPoint 进行拦截的定义。
PointCut 的目的就是提供一种方法使得开发者能够选择自己感兴趣的 JoinPoint。
* 5、通知（Advice）
切入点仅用于捕捉连接点集合，但是，除了捕捉连接点集合以外什么事情都没有做。 
事实上实现横切行为我们要使用通知。
它 一般指拦截到 JoinPoint 后要执行的代码，分为 前置、后置、环绕 三种类型。
这里，我们需要 注意 Advice Precedence（优先权） 的情况，
比如我们对同一个切面方法同时使用了 @Before 和 @Around 时就会报错，
此时会提示需要设置 Advice 的优先级。
AspectJ 作为一种基于 Java 语言实现的一套面向切面程序设计规范。
它向 Java 中加入了 连接点(Join Point) 这个新概念 ，
其实它也只是现存的一个 Java 概 念的名称而已。它向 Java 语言中加入了少许新结构，
譬如 切入点(pointcut)、通知(Advice)、类型间声明(Inter-type declaration) 和 切面(Aspect)。切入点和通知动态地影响程序流程，
类型间声明则是静态的影响程序的类等级结构，而切面则是对所有这些新结构的封装。
对于 AsepctJ 中的各个核心概念来说，其 连接点就恰如程序流中适当的一点。
而切入点收集特定的连接点集合和在这些点中的值。
一个通知则是当一个连接点到达时执行的代码，这些都是 AspectJ 的动态部分。
其实连接点就好比是 程序中那一条一条的语句，
而切入点就是特定一条语句处设置的一个断点，它收集了断点处程序栈的信息，
而通知就是在这个断点前后想要加入的程序代码。
此外，AspectJ 中也有许多不同种类的类型间声明，
这就允许程序员修改程序的静态结构、名称、类的成员以及类之间的关系。 
AspectJ 中的切面是横切关注点的模块单元。它们的行为与 Java 语言中的类很象，
但是切面 还封装了切入点、通知以及类型间声明。


## 6.小结
AspectJ 的三种织入方式中，个人觉得前面的两种会比较实用一些，因为第三种需要修改启动脚本，对于大型公司来说会比较不友好，
需要专门找运维人员配置。 在实际生产中，我们用得最多的还是纯 Spring AOP，通过本文的介绍，相信大家对于 AspectJ 的使用应该也没什么压力了。
大家如果对于本文介绍的内容有什么不清楚的，请直接在评论区留言，如果对于 Spring + AspectJ 感兴趣的读者，碰到问题也可以在评论区和大家互动讨论。

## 7.参考

https://javadoop.com/post/aspectj
https://juejin.cn/post/6844904112396615688

