package com.xueyi.common.datascope.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据权限过滤注解
 *
 * @author xueyi
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {

    /** 租户的别名 | 控制 enterpriseId */
    public String eAlias() default "";

    /** 租户的别名 | 控制 enterpriseId（包含 enterpriseId = 0 的数据） */
    public String edAlias() default "";

    /** 租户更新控制的别名 | 控制 enterpriseId（empty 无前缀更新 | other 有前缀更新） */
    public String ueAlias() default "";
    
    /** 租户更新控制的别名 | 控制 enterpriseId（包含 enterpriseId = 0 的数据）(empty 无前缀更新 | other 有前缀更新） */
    public String uedAlias() default "";

    /** 部门表的别名 */
    public String deptAlias() default "";

    /** 岗位表的别名 */
    public String postAlias() default "";

    /** 用户表的别名 */
    public String userAlias() default "";
}