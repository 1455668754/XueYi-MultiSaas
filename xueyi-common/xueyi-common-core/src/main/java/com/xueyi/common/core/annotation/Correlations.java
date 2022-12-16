package com.xueyi.common.core.annotation;

import java.lang.annotation.*;

/**
 * 从属关联关系定义注解集
 *
 * @author xueyi
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Correlations {

    Correlation[] value();
}