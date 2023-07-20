[![Maven Central](https://img.shields.io/maven-central/v/com.guanmengyuan/spring-ex?label=Maven%20Central)](https://central.sonatype.com/search?q=com.guanmengyuan&smo=true)
[![Contributors](https://img.shields.io/github/contributors/gmy-i/spring-ex)](https://github.com/gmy-i/spring-ex/graphs/contributors)

## 介绍

一个基于Spring的增强组件

## 如何使用

### 引入依赖

```xml

<dependencies>
    <dependency>
        <groupId>com.guanmengyuan</groupId>
        <artifactId>spring-ex-dependencies</artifactId>
        <version>${maven-release}</version>
        <scope>import</scope>
        <type>pom</type>
    </dependency>
</dependencies>
```

> 版本说明:
> * **jdk17on** 适用于jdk17以上的版本

## 组件说明

**[spring-boot-starter-webflux](./spring-boot-starter-webflux)**: 官方spring-boot-starter-webflux的增强版本

```xml

<dependency>
    <groupId>com.guanmengyuan.spring-ex</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>

```

> * 接口文档增强,支持javadoc文档注释
> * 全局异常拦截
> * 全局响应

**[spring-boot-starter-openapi](./spring-boot-starter-openapi)**: 文档增强组件,spring-boot-starter-webflux中默认包含
> * 接口访问地址: http://yourip:port/doc.html

```xml

<dependency>
    <groupId>com.guanmengyuan.spring-ex</groupId>
    <artifactId>spring-boot-starter-openapi</artifactId>
</dependency>

```

**[spring-boot-starter-mybatis-flex](./spring-boot-starter-mybatis-flex)**: mybatis-flex的增强自动配置组件

```xml
<dependency>
    <groupId>com.guanmengyuan.spring-ex</groupId>
    <artifactId>spring-boot-starter-mybatis-flex</artifactId>
</dependency>
```

**[spring-boot-starter-wx-mini-app](./spring-boot-starter-wx-mini-app)**: 微信小程序开发组件增强工具,支持小程序多配置
```xml
<dependency>
    <groupId>com.guanmengyuan.spring-ex</groupId>
    <artifactId>spring-boot-starter-wx-mini-app</artifactId>
</dependency>
```

**[spring-boot-starter-wx-pay](./spring-boot-starter-wx-pay)**: 微信支付开发组件增强工具,支持支付多配置

```xml
<dependency>
    <groupId>com.guanmengyuan.spring-ex</groupId>
    <artifactId>spring-boot-starter-wx-pay</artifactId>
</dependency>
```


**[spring-ex-common-model](./spring-ex-common-model)**: 通用模型包,内置全局响应和接口


```xml
<dependency>
    <groupId>com.guanmengyuan.spring-ex</groupId>
    <artifactId>spring-ex-common-model</artifactId>
</dependency>
```

> * **[BaseDomain](./spring-ex-common-model/src/main/java/com/guanmengyuan/spring/ex/common/model/domain/BaseDomain.java)**: 通用父类
> * **[TenantDomain](./spring-ex-common-model/src/main/java/com/guanmengyuan/spring/ex/common/model/domain/TenantDomain.java)**: 租户模式下的通用父类
> * **[ParamEnum](./spring-ex-common-model/src/main/java/com/guanmengyuan/spring/ex/common/model/enums/ParamEnum.java)**: 通用参数枚举接口,实现该接口可将任意类型的enum作为参数传递

