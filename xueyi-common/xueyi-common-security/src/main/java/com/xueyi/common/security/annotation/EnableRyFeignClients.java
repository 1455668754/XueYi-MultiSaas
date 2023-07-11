package com.xueyi.common.security.annotation;

import com.xueyi.common.security.feign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义feign注解
 *
 * @author xueyi
 */
@Inherited
@Documented
@EnableFeignClients
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({FeignAutoConfiguration.class})
public @interface EnableRyFeignClients {

    String[] value() default {};

    String[] basePackages() default {"com.xueyi"};

    Class<?>[] basePackageClasses() default {};

    Class<?>[] defaultConfiguration() default {};

    Class<?>[] clients() default {};
}
