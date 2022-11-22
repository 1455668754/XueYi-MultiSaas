package com.xueyi.common.core.annotation.field;

import java.lang.annotation.*;

/**
 * 参数自定义注解
 *
 * @author xueyi
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface MultiField {
}
