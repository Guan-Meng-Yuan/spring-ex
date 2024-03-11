[![Maven Central](https://img.shields.io/maven-central/v/com.guanmengyuan/spring-ex?label=Maven%20Central)](https://central.sonatype.com/search?q=com.guanmengyuan&smo=true)
[![Apache Licence2](https://img.shields.io/:License-Apache2-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.txt)
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

## 组件说明

**[spring-boot-starter-web](./spring-boot-starter-web)**: 官方spring-boot-starter-webmvc的增强版本

```xml

<dependency>
    <groupId>com.guanmengyuan.spring-ex</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

```

> * 全局异常拦截
> * 全局响应


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

**[spring-boot-starter-alipay](./spring-boot-starter-alipay)**: 支付宝开发组件增强工具,支持多配置

```xml
<dependency>
    <groupId>com.guanmengyuan.spring-ex</groupId>
    <artifactId>spring-boot-starter-alipay</artifactId>
</dependency>
```
**[spring-cloud-starter-auth](./spring-cloud-starter-auth)**: SpringCloud认证自动配置组件

```xml
<dependency>
    <groupId>com.guanmengyuan.spring-ex</groupId>
    <artifactId>spring-cloud-starter-auth</artifactId>
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

**[spring-boot-starter-oss](./spring-boot-starter-oss)**: AWS s3协议存储通用组件

```xml
<dependency>
    <groupId>com.guanmengyuan.spring-ex</groupId>
    <artifactId>spring-boot-starter-oss</artifactId>
</dependency>
```
