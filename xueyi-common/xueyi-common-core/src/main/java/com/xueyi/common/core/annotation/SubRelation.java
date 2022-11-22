package com.xueyi.common.core.annotation;

import com.xueyi.common.core.constant.basic.OperateConstants;

import java.lang.annotation.*;

/**
 * 主子关联关系定义注解
 *
 * @author xueyi
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface SubRelation {

    /** 分组名称 */
    String groupName();

    /** 键关联类型 */
    OperateConstants.SubKeyType keyType();

    /** 删除类型 */
    OperateConstants.SubDeleteType deleteType() default OperateConstants.SubDeleteType.NORMAL;

}
