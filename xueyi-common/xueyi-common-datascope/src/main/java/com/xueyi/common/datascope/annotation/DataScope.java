package com.xueyi.common.datascope.annotation;

import java.lang.annotation.*;

/**
 * 数据权限过滤注解
 *
 * @author xueyi
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {

    /** 部门级控制 - 字段名 */
    String deptAlias() default "";

    /** 岗位级控制 - 字段名 */
    String postAlias() default "";

    /** 用户级控制 - 字段名 */
    String userAlias() default "";
}