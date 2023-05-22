package com.xueyi.common.web.correlate.utils;

import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.core.exception.UtilException;
import com.xueyi.common.core.utils.core.ArrayUtil;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.entity.base.BasisEntity;
import com.xueyi.common.web.correlate.domain.SqlField;

/**
 * SQL操作工具类
 *
 * @author xueyi
 */
public final class SqlHandleUtil {

    /**
     * Wrapper自定义控制 SQL操作
     *
     * @param i           Wrapper
     * @param sqlFieldArr 动态SQL控制对象集合
     */
    @SuppressWarnings("unchecked")
    public static <P extends BasisEntity, Lambda extends AbstractLambdaWrapper<P, ?>> void fieldCondition(Lambda i, SqlField[] sqlFieldArr) {
        if (ArrayUtil.isNotEmpty(sqlFieldArr)) {
            for (SqlField sqlField : sqlFieldArr) {
                if (StrUtil.isNotEmpty(sqlField.getFieldStr())) {
                    switch (sqlField.getOperateType()) {
                        case SET -> {
                            if (i instanceof LambdaUpdateWrapper) {
                                ((LambdaUpdateWrapper<P>) i).setSql(StrUtil.format(sqlField.getOperateType().getSql(), sqlField.getFieldStr(), sqlField.getObject()));
                            } else {
                                throw new UtilException("wrapper is not LambdaUpdateWrapper,method does not exist!");
                            }
                        }
                        case EQ, NE, GT, GE, LT, LE, LIKE, NOT_LIKE, LIKE_LEFT, LIKE_RIGHT ->
                                i.apply(StrUtil.format(sqlField.getOperateType().getSql(), sqlField.getFieldStr(), sqlField.getObject()));
                        case IS_NULL, IS_NOT_NULL ->
                                i.apply(StrUtil.format(sqlField.getOperateType().getSql(), sqlField.getFieldStr()));
                        case IN, NOT_IN ->
                                i.apply(StrUtil.format(sqlField.getOperateType().getSql(), sqlField.getFieldStr(), CollUtil.join(sqlField.getColl(), StrUtil.COMMA)));
                        default -> i.apply(SqlConstants.NONE_FIND);
                    }
                } else {
                    i.apply(SqlConstants.NONE_FIND);
                }
            }
        } else {
            i.apply(SqlConstants.NONE_FIND);
        }
    }
}
