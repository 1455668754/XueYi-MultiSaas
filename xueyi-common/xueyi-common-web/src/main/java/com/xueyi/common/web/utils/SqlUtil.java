package com.xueyi.common.web.utils;

import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.core.exception.UtilException;
import com.xueyi.common.core.utils.core.ArrayUtil;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.web.entity.domain.SqlField;

/**
 * SQL操作工具类
 *
 * @author xueyi
 */
public class SqlUtil {

    /**
     * Wrapper自定义控制 SQL操作
     *
     * @param i           Wrapper
     * @param sqlFieldArr 动态SQL控制对象集合
     */
    @SuppressWarnings("unchecked")
    public static <P extends BaseEntity, Lambda extends AbstractLambdaWrapper<P, ?>> void fieldCondition(Lambda i, SqlField[] sqlFieldArr) {
        if (ArrayUtil.isNotEmpty(sqlFieldArr)) {
            for (SqlField sqlField : sqlFieldArr) {
                switch (sqlField.getOperateType()) {
                    case SET:
                        if (i instanceof LambdaUpdateWrapper) {
                            ((LambdaUpdateWrapper<P>) i).set((SFunction<P, ?>) sqlField.getFieldFun(), sqlField.getObject());
                        } else {
                            throw new UtilException("wrapper is not LambdaUpdateWrapper,method does not exist!");
                        }
                    case EQ:
                        i.eq((SFunction<P, ?>) sqlField.getFieldFun(), sqlField.getObject());
                        i.eq((SFunction<P, ?>) sqlField.getFieldFun(), sqlField.getObject());
                        break;
                    case NE:
                        i.ne((SFunction<P, ?>) sqlField.getFieldFun(), sqlField.getObject());
                        break;
                    case GT:
                        i.gt((SFunction<P, ?>) sqlField.getFieldFun(), sqlField.getObject());
                        break;
                    case GE:
                        i.ge((SFunction<P, ?>) sqlField.getFieldFun(), sqlField.getObject());
                        break;
                    case LT:
                        i.lt((SFunction<P, ?>) sqlField.getFieldFun(), sqlField.getObject());
                        break;
                    case LE:
                        i.le((SFunction<P, ?>) sqlField.getFieldFun(), sqlField.getObject());
                        break;
                    case LIKE:
                        i.like((SFunction<P, ?>) sqlField.getFieldFun(), sqlField.getObject());
                        break;
                    case NOT_LIKE:
                        i.notLike((SFunction<P, ?>) sqlField.getFieldFun(), sqlField.getObject());
                        break;
                    case LIKE_LEFT:
                        i.likeLeft((SFunction<P, ?>) sqlField.getFieldFun(), sqlField.getObject());
                        break;
                    case LIKE_RIGHT:
                        i.likeRight((SFunction<P, ?>) sqlField.getFieldFun(), sqlField.getObject());
                        break;
                    case IS_NULL:
                        i.isNull((SFunction<P, ?>) sqlField.getFieldFun());
                        break;
                    case IS_NOT_NULL:
                        i.isNotNull((SFunction<P, ?>) sqlField.getFieldFun());
                        break;
                    case IN:
                        i.in((SFunction<P, ?>) sqlField.getFieldFun(), sqlField.getList());
                        break;
                    case NOT_IN:
                        i.notIn((SFunction<P, ?>) sqlField.getFieldFun(), sqlField.getList());
                        break;
                    default:
                        i.apply(SqlConstants.NONE_FIND);
                }
            }
        } else {
            i.apply(SqlConstants.NONE_FIND);
        }
    }

}
