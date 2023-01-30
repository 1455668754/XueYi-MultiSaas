package com.xueyi.common.security.annotation;

import com.xueyi.common.security.feign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 自定义feign注解
 * 添加basePackages路径
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
