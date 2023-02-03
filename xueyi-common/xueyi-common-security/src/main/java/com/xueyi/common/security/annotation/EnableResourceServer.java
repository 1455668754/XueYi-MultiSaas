package com.xueyi.common.security.annotation;

import com.xueyi.common.security.config.ResourceConfig;
import com.xueyi.common.security.config.SecurityConfig;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.lang.annotation.*;

/**
 * 资源服务注解
 *
 * @author xueyi
 */
@Inherited
@Documented
@EnableMethodSecurity
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({ResourceConfig.class, SecurityConfig.class})
public @interface EnableResourceServer {
}
