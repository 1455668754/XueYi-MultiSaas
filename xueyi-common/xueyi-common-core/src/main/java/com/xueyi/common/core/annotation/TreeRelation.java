package com.xueyi.common.core.annotation;

import java.lang.annotation.*;

/**
 * 树转换注解
 *
 * @author xueyi
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface TreeRelation {

}