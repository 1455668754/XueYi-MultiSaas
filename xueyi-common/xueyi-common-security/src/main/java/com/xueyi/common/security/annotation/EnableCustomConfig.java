package com.xueyi.common.security.annotation;

import com.xueyi.common.security.config.ApplicationConfig;
import com.xueyi.common.security.config.JacksonConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注入注解
 *
 * @author xueyi
 */
@Inherited
@Documented
// 开启线程异步执行
@EnableAsync
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
// 指定要扫描的Mapper类的包的路径
@MapperScan("com.xueyi.**.mapper")
// 表示通过aop框架暴露该代理对象,AopContext能够访问
@EnableAspectJAutoProxy(exposeProxy = true)
// 自动加载类
@Import({ApplicationConfig.class, JacksonConfig.class})
public @interface EnableCustomConfig {
}
