package com.xueyi.common.security.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 外系统端认证注解
 *
 * @author xueyi
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ExternalAuth {

    /** 是否匿名认证 */
    boolean isAnonymous() default false;

    /** 是否校验用户信息 */
    boolean isUser() default false;
}
