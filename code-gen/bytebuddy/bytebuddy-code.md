---
title: 字节码增强技术-Byte Buddy
categories:
- java
---

# 字节码增强技术-Byte Buddy
为什么需要在运行时生成代码？
Java 是一个强类型语言系统，要求变量和对象都有一个确定的类型，不兼容类型赋值都会造成转换异常，
通常情况下这种错误都会被编译器检查出来，如此严格的类型在大多数情况下是比较令人满意的，这对
构建具有非常强可读性和稳定性的应用有很大的帮助，这也是 Java 能在企业编程中的普及的一个原因之一。
然而，因为起强类型的检查，限制了其他领域语言应用范围。比如在编写一个框架是，通常我们并不知道应用程序定义的类型，
因为当这个库被编译时，我们还不知道这些类型，为了能在这种情况下能调用或者访问应用程序的方法或者变量，
Java 类库提供了一套反射 API。使用这套反射 API，我们就可以反省为知类型，进而调用方法或者访问属性。但是，Java 反射有如下缺点：

需要执行一个相当昂贵的方法查找来获取描述特定方法的对象，因此，相比硬编码的方法调用，使用 反射 API 非常慢。
反射 API 能绕过类型安全检查，可能会因为使用不当照成意想不到的问题，这样就错失了 Java 编程语言的一大特性。
简介
正如官网说的：Byte Buddy 是一个代码生成和操作库，用于在Java应用程序运行时创建和修改Java类，而无需编译器的帮助。
除了Java类库附带的代码生成实用程序外，Byte Buddy还允许创建任意类，并且不限于实现用于创建运行时代理的接口。
此外，Byte Buddy提供了一种方便的API，可以使用Java代理或在构建过程中手动更改类。Byte Buddy 相比其他字节码操作库有如下优势：

无需理解字节码格式，即可操作，简单易行的 API 能很容易操作字节码。
支持 Java 任何版本，库轻量，仅取决于Java字节代码解析器库ASM的访问者API，它本身不需要任何其他依赖项。
比起JDK动态代理、cglib、Javassist，Byte Buddy在性能上具有优势。
性能
在选择字节码操作库时，往往需要考虑库本身的性能。对于许多应用程序，生成代码的运行时特性更有可能确定最佳选择。而
在生成的代码本身的运行时间之外，用于创建动态类的运行时也是一个问题。官网对库进行了性能测试，给出以下结果图：

![](https://user-gold-cdn.xitu.io/2019/10/13/16dc4ade119d5610?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

图中的每一行分别为，类的创建、接口实现、方法调用、类型扩展、父类方法调用的性能结果。
从性能报告中可以看出，Byte Buddy 的主要侧重点在于以最少的运行时生成代码，需要注意的是，我们这些衡量 Java 代码性能的测试，
都由 Java 虚拟机即时编译器优化过，如果你的代码只是偶尔运行，没有得到虚拟机的优化，可能性能会有所偏差。
所以我们在使用 Byte Buddy 开发时，我们希望监控这些指标，以避免在添加新功能时造成性能损失。

## 1.Hello world代码
```
Class<?> dynamicType = new ByteBuddy()
                .subclass(Object.class)
                .method(ElementMatchers.named("toString"))
                .intercept(FixedValue.value("Hello World"))
                .make()
                .load(HelloWorldBuddy.class.getClassLoader())
                .getLoaded();

        Object instance = dynamicType.newInstance();
        String toString = instance.toString();
        System.out.println(toString);
        System.out.println(instance.getClass().getCanonicalName());
```
从例子中看到，操作创建一个类如此的简单。正如 ByteBuddy 说明的，ByteBuddy 提供了一个领域特定语言，
这样就可以尽可能地提高人类可读性简单易行的 API，可能能让你在初次使用的过程中就能不需要查阅 API 的前提下完成编码。
这也真是 ByteBuddy 能完爆其他同类型库的一个原因。
上面的示例中使用的默认ByteBuddy配置会以最新版本的类文件格式创建Java类，该类文件格式可以被正在处理的Java虚拟机理解。
subclass 指定了新创建的类的父类，同时 method 指定了 Object 的 toString 方法，intercept 拦截了 toString 方法并返回固定的 value ，
最后 make 方法生产字节码，有类加载器加载到虚拟机中。
此外，Byte Buddy不仅限于创建子类和操作类，还可以转换现有代码。Byte Buddy 还提供了一个方便的 API，
用于定义所谓的 Java 代理，该代理允许在任何 Java 应用程序的运行期间进行代码转换，代理会在下篇单独写一篇文章讲解。

## 2.创建一个类
任何一个由 ByteBuddy 创建的类型都是通过 ByteBuddy 类的实例来完成的。通过简单地调用 new ByteBuddy() 就可以创建一个新实例。

```
DynamicType.Unloaded<?> dynamicType = new ByteBuddy()
    .subclass(Object.class)
    .make();
```
上面的示例代码会创建一个继承至 Object 类型的类。这个动态创建的类型与直接扩展 Object 并且没有实现任何方法、属性和构造函数的类型是等价的
。该列子没有命名动态生成的类型，但是在定义 Java 类时却是必须的，所以很容易的你会想到，ByteBuddy 会有默认的策略给我们生成。当然，你也可以很容易地明确地命名这个类型。
```
DynamicType.Unloaded<?> dynamicType = new ByteBuddy()
.subclass(Object.class)
.name("example.Type")
.make();
```
那么默认的策略是如何做的呢？这个将与 ByteBuddy 与 约定大于配置息息相关，它提供了我们认为比较全面的默认配置。
至于类型命名，ByteBuddy 的默认配置提供了 NamingStrategy，它基于动态类型的超类名称来随机生成类名。
此外，名称定义在与父类相同的包下，这样父类的包级访问权限的方法对动态类型也可见。如果你将示例子类命名为 example.Foo，
那么生成的名称将会类似于 example.FooByteBuddy1376491271，这里的数字序列是随机的。

此外，在一些需要指定类型的场景中，可以通过重写 NamingStrategy 的方法来实现，或者使用 ByteBuddy 内置的NamingStrategy.SuffixingRandom 来实现。

同时需要注意的是，我们编码时需要遵守所谓的领域特定语言和不变性原则，这是说明意思呢？就是说在 ByteBuddy 中，
几乎所有的类都被构建成不可变的；极少数情况，我们不可能把对象构建成不可变的。请看下面一个例子：
```
ByteBuddy byteBuddy = new ByteBuddy();
byteBuddy.with(new NamingStrategy.SuffixingRandom("suffix"));
DynamicType.Unloaded<?> dynamicType1 = byteBuddy.subclass(Object.class).make();
```
上述例子你会发现类的命名策略还是默认的，其根本原因就是没有遵守上述原则导致的。所以在编码过程中要基于此原则进行。

## 3.加载类
上节创建的 DynamicType.Unloaded，代表一个尚未加载的类，顾名思义，这些类型不会加载到 Java 虚拟机中，它仅仅表示创建好了类的字节码，
通过 DynamicType.Unloaded 中的 getBytes 方法你可以获取到该字节码，在你的应用程序中，
你可能需要将该字节码保存到文件，或者注入的现在的 jar 文件中，因此该类型还提供了一个 saveIn(File) 方法，
可以将类存储在给定的文件夹中； inject(File) 方法将类注入到现有的 Jar 文件中，
另外你只需要将该字节码直接加载到虚拟机使用，你可以通过 ClassLoadingStrategy 来加载。

如果不指定ClassLoadingStrategy，Byte Buffer根据你提供的ClassLoader来推导出一个策略，内置的策略定义在枚举ClassLoadingStrategy.Default中

WRAPPER：创建一个新的Wrapping类加载器
CHILD_FIRST：类似上面，但是子加载器优先负责加载目标类
INJECTION：利用反射机制注入动态类型
示例
```
Class<?> type = new ByteBuddy()
.subclass(Object.class)
.make()
.load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
.getLoaded()
```
这样我们创建并加载了一个类。我们使用 WRAPPER 策略来加载适合大多数情况的类。getLoaded 方法返回一个 Java Class 的实例，它就表示现在加载的动态类。

重新加载类
得益于JVM的HostSwap特性，已加载的类可以被重新定义：

// 安装Byte Buddy的Agent，除了通过-javaagent静态安装，还可以：
```
ByteBuddyAgent.install();
Foo foo = new Foo();
    new ByteBuddy()
    .redefine(Bar.class)
    .name(Foo.class.getName())
    .make()
    .load(Foo.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
    assertThat(foo.m(), is("bar"));
```
可以看到，即使时已经存在的对象，也会受到类Reloading的影响。但是需要注意的是HostSwap具有限制：

类再重新载入前后，必须具有相同的Schema，也就是方法、字段不能减少（可以增加）
不支持具有静态初始化块的类
修改类
redefine
重定义一个类时，Byte Buddy 可以对一个已有的类添加属性和方法，或者删除已经存在的方法实现。新添加的方法，如果签名和原有方法一致，则原有方法会消失。

rebase
类似于redefine，但是原有的方法不会消失，而是被重命名，添加后缀 $original，这样，就没有实现会被丢失。重定义的方法可以继续通过它们重命名过的名称调用原来的方法，例如类：
```
class Foo {
    String bar() { return "bar"; }
}
```
rebase 之后：
```
class Foo {
    String bar() { return "foo" + bar$original(); }
    private String bar$original() { return "bar"; }
}
```
## 4.方法拦截
通过匹配模式拦截
ByteBuddy 提供了很多用于匹配方法的 DSL，如下例子：
```
Foo dynamicFoo = new ByteBuddy()
    .subclass(Foo.class)
    // 匹配由Foo.class声明的方法
    .method(isDeclaredBy(Foo.class)).intercept(FixedValue.value("One!"))
    // 匹配名为foo的方法
    .method(named("foo")).intercept(FixedValue.value("Two!"))
    // 匹配名为foo，入参数量为1的方法
    .method(named("foo").and(takesArguments(1))).intercept(FixedValue.value("Three!"))
    .make()
    .load(getClass().getClassLoader())
    .getLoaded()
    .newInstance();
```
ByteBuddy 通过 net.bytebuddy.matcher.ElementMatcher 来定义配置策略，可以通过此接口实现自己定义的匹配策略。库本身提供的 Matcher 非常多。Uploading file...

## 5.方法委托
使用MethodDelegation可以将方法调用委托给任意POJO。Byte Buddy不要求Source（被委托类）、Target类的方法名一致
```
class Source {
    public String hello(String name) { return null; }
}

class Target {
    public static String hello(String name) {
        return "Hello " + name + "!";
    }
}

String helloWorld = new ByteBuddy()
    .subclass(Source.class)
    .method(named("hello")).intercept(MethodDelegation.to(Target.class))
    .make()
    .load(getClass().getClassLoader())
    .getLoaded()
    .newInstance()
    .hello("World");
```
其中 Target 还可以如下实现：
```
class Target {
    public static String intercept(String name) { return "Hello " + name + "!"; }
    public static String intercept(int i) { return Integer.toString(i); }
    public static String intercept(Object o) { return o.toString(); }
}
```
前一个实现因为只有一个方法，而且类型也匹配，很好理解，那么后一个呢，Byte Buddy到底会委托给哪个方法？Byte Buddy遵循一个最接近原则：

intercept(int)因为参数类型不匹配，直接Pass
另外两个方法参数都匹配，但是 intercept(String)类型更加接近，因此会委托给它
同时需要注意的是被拦截的方法需要声明为 public，否则没法进行拦截增强。除此之外，还可以使用 @RuntimeType 注解来标注方法
```
@RuntimeType
public static Object intercept(@RuntimeType Object value) {
    System.out.println("Invoked method with: " + value);
    return value;
}
```
## 6.参数绑定
可以在拦截器（Target）的拦截方法 intercept 中使用注解注入参数，ByteBuddy 会根据注解给我们注入对于的参数值。比如：
```
void intercept(Object o1, Object o2)
// 等同于
void intercept(@Argument(0) Object o1, @Argument(1) Object o2)复制代码
常用的注解如下表：
```

注解   | 描述
----- | -----
@Argument | 	绑定单个参数
@AllArguments	 | 绑定所有参数的数组
@This	 | 当前被拦截的、动态生成的那个对象
@DefaultCall | 	调用默认方法而非super的方法
@SuperCall | 	用于调用父类版本的方法
@RuntimeType | 	可以用在返回值、参数上，提示ByteBuddy禁用严格的类型检查
@Super | 	当前被拦截的、动态生成的那个对象的父类对象
@FieldValue | 	注入被拦截对象的一个字段的值

## 7.字段属性
```
public class UserType {
  public String doSomething() { return null; }
}

public interface Interceptor {
  String doSomethingElse();
}

public interface InterceptionAccessor {
  Interceptor getInterceptor();
  void setInterceptor(Interceptor interceptor);
}

public interface InstanceCreator {
  Object makeInstance();
}

public class HelloWorldInterceptor implements Interceptor {
  @Override
  public String doSomethingElse() {
    return "Hello World!";
  }
}

Class<? extends UserType> dynamicUserType = new ByteBuddy()
  .subclass(UserType.class)
    .method(not(isDeclaredBy(Object.class))) // 非父类 Object 声明的方法
    .intercept(MethodDelegation.toField("interceptor")) // 拦截委托给属性字段 interceptor
  .defineField("interceptor", Interceptor.class, Visibility.PRIVATE) // 定义一个属性字段
  .implement(InterceptionAccessor.class).intercept(FieldAccessor.ofBeanProperty()) // 实现 InterceptionAccessor 接口
  .make()
  .load(getClass().getClassLoader())
  .getLoaded();
    
InstanceCreator factory = new ByteBuddy()
  .subclass(InstanceCreator.class)
    .method(not(isDeclaredBy(Object.class))) // 非父类 Object 声明的方法
    .intercept(MethodDelegation.toConstructor(dynamicUserType)) // 委托拦截的方法来调用提供的类型的构造函数
  .make()
  .load(dynamicUserType.getClassLoader())
  .getLoaded().newInstance();

UserType userType = (UserType) factory.makeInstance();
((InterceptionAccessor) userType).setInterceptor(new HelloWorldInterceptor());
String s = userType.doSomething();
System.out.println(s); // Hello World!
```
上述例子将 UserType 类实现了 InterceptionAccessor 接口，同时使用 MethodDelegation.toField 可以使拦截的方法可以委托给新增的字段。
## 8.参考

https://juejin.cn/post/6844903965553852423