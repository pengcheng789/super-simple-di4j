# Super Simple DI4J

## 概述

一个简单的依赖注入工具。

## 用法

* [下载](https://github.com/pengcheng789/super-simple-di4j/releases/tag/v0.1.0)这个工具的库文件，并加入到你的项目的`classpath`路径中。

* 在需要注入的组件上使用`Component`注解。
  
  ```java
  //...
  import cn.caipengcheng.ssimpledi4j.annotation.Component;
  
  @Component
  public class A {
      // ...
      public A() {}
  }
  ```
  
  ```java
    //...
    import cn.caipengcheng.ssimpledi4j.annotation.Component;
    
    @Component
    public class B {
        // ...
        private A a;
  
        public B(A a) {
            this.a = a;
        }
    }
    ```
  
* 调用`ssimpleDI4jInit()`进行初始化，然后可通过调用`getInstance()`方法获取实例。

  ```java
  //...
  import cn.caipengcheng.ssimpledi4j.SSimpleDI4jKt;
  
  public class Main {
      // ...
  
      public void main(String[] args) {
          // ...
          SSimpleDI4jKt.ssimple4jInit(Main.class);
          // ...
          A a = SSimpleDI4jKt.getInstance(A.class);
          B b = SSimpleDI4jKt.getInstance(B.class);
      }
  }
  ```