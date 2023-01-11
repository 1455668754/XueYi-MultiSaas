package com.xueyi.common.web.utils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.core.exception.UtilException;
import com.xueyi.common.core.utils.core.ArrayUtil;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.entity.base.BasisEntity;
import com.xueyi.common.web.entity.domain.SqlField;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * SQL操作工具类
 *
 * @author xueyi
 */
public class SqlHandleUtil {

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

    /**
     * 获取实体类的数据库字段名
     *
     * @param fieldFun 字段SFunction方法
     * @return 字段对应数据库字段名
     */
    public static <T> String getFieldName(SFunction<T, ?> fieldFun) {
        // 从function取出序列化方法
        Method writeReplaceMethod;
        try {
            writeReplaceMethod = fieldFun.getClass().getDeclaredMethod("writeReplace");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        // 从序列化方法取出序列化的lambda信息
        boolean isAccessible = writeReplaceMethod.canAccess(fieldFun);
        writeReplaceMethod.setAccessible(true);
        SerializedLambda serializedLambda;
        try {
            serializedLambda = (SerializedLambda) writeReplaceMethod.invoke(fieldFun);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        writeReplaceMethod.setAccessible(isAccessible);

        // 从lambda信息取出method、field、class等
        String fieldName = serializedLambda.getImplMethodName().substring("get".length());
        fieldName = fieldName.replaceFirst(fieldName.charAt(0) + StrUtil.EMPTY, (fieldName.charAt(0) + StrUtil.EMPTY).toLowerCase());
        Field field;
        try {
            field = Class.forName(serializedLambda.getImplClass().replace(StrUtil.SLASH, StrUtil.DOT)).getDeclaredField(fieldName);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        // 获取字段名
        TableField tableField = field.getAnnotation(TableField.class);
        return ObjectUtil.isNotNull(tableField) && StrUtil.isNotEmpty(tableField.value())
                ? tableField.value()
                : StrUtil.toUnderlineCase(fieldName);
    }

}
