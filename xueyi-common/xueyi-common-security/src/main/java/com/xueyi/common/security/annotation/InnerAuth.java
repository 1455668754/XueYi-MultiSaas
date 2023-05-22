package com.xueyi.common.security.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 内部认证注解
 *
 * @author xueyi
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InnerAuth {

    /** 是否匿名认证 */
    boolean isAnonymous() default false;

    /** 是否校验用户信息 */
    boolean isUser() default false;
}
