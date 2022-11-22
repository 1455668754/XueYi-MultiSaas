package com.xueyi.common.core.annotation;

import java.lang.annotation.*;

/**
 * 参数分组注解
 *
 * @author xueyi
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldGroup {
    Class<?>[] value() default {};
}
