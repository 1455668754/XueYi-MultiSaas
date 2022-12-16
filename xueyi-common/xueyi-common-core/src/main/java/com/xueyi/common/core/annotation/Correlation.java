package com.xueyi.common.core.annotation;

import com.xueyi.common.core.constant.basic.OperateConstants;

import java.lang.annotation.*;

/**
 * 从属关联关系定义注解
 *
 * @author xueyi
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Correlation {

    /** 分组名称 */
    String groupName();

    /** 键关联类型 */
    OperateConstants.SubKeyType keyType();

}
