package com.xueyi.common.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 从属关联关系定义注解集
 *
 * @author xueyi
 */
@Documented
@Deprecated
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Correlations {

    Correlation[] value();
}