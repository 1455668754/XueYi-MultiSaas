package com.xueyi.common.security.annotation;

import com.xueyi.common.security.config.ResourceConfig;
import com.xueyi.common.security.config.SecurityConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 资源服务注解
 *
 * @author xueyi
 */
@Inherited
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({ResourceConfig.class, SecurityConfig.class})
public @interface EnableResourceServer {
}
