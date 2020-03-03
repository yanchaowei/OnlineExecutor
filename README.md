## 涉及技术

- Java 动态编译
- Java 类文件的结构
- Java 类加载器 & Java 类的热替换
- Java 反射
- 如何将一个类变为线程安全类

## 项目介绍

- **实现编译模块：** 使用动态编译技术，可将客户端发来的源代码字符串直接编译为字节数组；
- **实现字节码修改器：** 根据 Java 类文件结构修改类的字节码，可将客户端程序对 System 的调用替换为对 System的替代类 HackSystem 的调用；
- **实现运行模块：** 自定义类加载器实现类的加载 & 热替换，通过反射实现 main 方法的运行；
- **解决多用户同时发送执行代码请求时的并发问题：** 通过 ThreadLoacl 实现线程封闭，为每个请求创建一个输出流存储标准输出及标准错误结果；

## 从原文件到字节码文件的编译方式

从 JDK 1.6 开始引入了用 Java 代码重写的编译器接口，使得我们可以在运行时编译 Java 源码，然后用类加载器进行加载，让 Java 语言更具灵活性，能够完成许多高级的操作。

传统的编译方式是使用命令行在当前目录下运行：

```shell
javac xxx.java
```

然后在同一目录下生成 Example.class 字节码文件。而使用 Java API 来编译类文件则稍微有点复杂，示例代码如下

```java
public class CompileFileToFile{

    public static void main(String[] args) {
        //获取系统Java编译器
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        //获取Java文件管理器
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        //定义要编译的源文件
        File file = new File("/path/to/file");
        //通过源文件获取到要编译的Java类源码迭代器，包括所有内部类，其中每个类都是一个 JavaFileObject，也被称为一个汇编单元
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjects(file);
        //生成编译任务
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, null, null, compilationUnits);
        //执行编译任务
        task.call();
    }

}
```

上述代码运行之后，会在 xxx.java 的同一目录下生成 Example.class 文件，之所以这么简单的任务还要写这么长的代码，还是与 Java 的设计风格分不开。比如说第一句获取到的编译器实例，在这里获得的是系统使用的Java编译器，但 API 的设计者却想让广大的程序员有机会定制有自己风格的编译器，于是使用了一个接口来定义必要的通用行为，同样的思想还用在了后续几个类的设计上面。

## 从源文件到内存的编译方式

`JavaFileObject` 的 1`openOutputStream()` 方法控制了编译后字节码的输出行为，意味着我们可以根据需要定制自己的 Java 文件对象。比如，当编译完源文件之后，我们不想将字节码输出到文件，而是留在内存中以便后续加载，那么我们可以实现自己的输出文件类 `JavaFileObject`。由于输出文件对象是从文件管理器的 `getJavaFileForOutput() `方法获取的，所以我们还应该重写文件管理器的这一行为。

参考类`org.olexec.compile.StringSourceCompiler`。

## 从内存到内存的编译方式

既然能够自定义 JavaFileObject 来控制字节码的输出行为，那么很明显也能够通过类似的方式来控制源码的输入，比如从字符串中读取源码而非从文件中。

源码的读取是通过 JavaFileObject 的 getCharContent() 方法。在 PathFileObject 的 getCharContent 内部，返回的是 CharBuffer 实例。于是可以自定义 `JavaFileObject `的 `getCharContent()`方法实现。

