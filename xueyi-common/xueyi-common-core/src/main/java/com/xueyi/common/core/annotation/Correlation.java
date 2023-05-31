package com.xueyi.common.core.annotation;

import com.xueyi.common.core.constant.basic.OperateConstants;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 从属关联关系定义注解
 *
 * @author xueyi
 */
@Documented
@Deprecated
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Correlation {

    /** 分组名称 */
    String groupName();

    /** 键关联类型 */
    OperateConstants.SubKeyType keyType();

}
