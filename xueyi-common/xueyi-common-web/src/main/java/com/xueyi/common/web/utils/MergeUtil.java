package com.xueyi.common.web.utils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.core.constant.error.UtilErrorConstants;
import com.xueyi.common.core.exception.UtilException;
import com.xueyi.common.core.utils.core.*;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.web.entity.domain.SqlField;
import com.xueyi.common.web.entity.domain.SubRelation;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 主子关联工具类
 *
 * @author xueyi
 */
public class MergeUtil {

    /**
     * 子数据映射关联
     *
     * @param dto             数据对象
     * @param subRelationList 子类关联对象集合
     * @param DClass          数据对象Class
     */
    public static <D> D subRelationBuild(D dto, List<SubRelation> subRelationList, Class<D> DClass) {
        if (CollUtil.isEmpty(subRelationList))
            return dto;
        for (SubRelation subRelation : subRelationList) {
            if (ObjectUtil.hasNull(subRelation.getMainKeyField(), subRelation.getSubKeyField(), subRelation.getReceiveKeyField()))
                initRelationField(subRelation, DClass);
            switch (subRelation.getRelationType()) {
                case DIRECT:
                    directRelationBuild(dto, null, subRelation, OperateConstants.DataRow.SINGLE);
                    break;
                case INDIRECT:
                    indirectRelationBuild(dto, null, subRelation, OperateConstants.DataRow.SINGLE);
                    break;
            }
        }
        return dto;
    }

    /**
     * 集合子数据映射关联
     *
     * @param dtoList         数据对象集合
     * @param subRelationList 子类关联对象集合
     * @param DClass          数据对象Class
     */
    public static <D> List<D> subRelationBuild(List<D> dtoList, List<SubRelation> subRelationList, Class<D> DClass) {
        if (CollUtil.isEmpty(subRelationList))
            return dtoList;
        for (SubRelation subRelation : subRelationList) {
            if (ObjectUtil.hasNull(subRelation.getMainKeyField(), subRelation.getSubKeyField(), subRelation.getReceiveKeyField()))
                initRelationField(subRelation, DClass);
            switch (subRelation.getRelationType()) {
                case DIRECT:
                    directRelationBuild(null, dtoList, subRelation, OperateConstants.DataRow.COLLECTION);
                    break;
                case INDIRECT:
                    indirectRelationBuild(null, dtoList, subRelation, OperateConstants.DataRow.COLLECTION);
                    break;
            }
        }
        return dtoList;
    }

    /**
     * 直接关联
     *
     * @param dto         数据对象
     * @param dtoList     数据对象集合
     * @param subRelation 子类关联对象
     * @param dataRow     数据类型
     */
    private static <D> void directRelationBuild(D dto, List<D> dtoList, SubRelation subRelation, OperateConstants.DataRow dataRow) {
        TableField tableField = subRelation.getSubKeyField().getAnnotation(TableField.class);
        String subFieldName = StrUtil.isNotEmpty(tableField.value()) ? tableField.value() : StrUtil.toUnderlineCase(subRelation.getSubKeyField().getName());
        switch (dataRow) {
            case SINGLE:
                if (ObjectUtil.isNull(dto))
                    return;
                try {
                    Object value = subRelation.getMainKeyField().get(dto);
                    SqlField singleSqlField = new SqlField(SqlConstants.OperateType.EQ, subFieldName, value instanceof String ? StrUtil.SINGLE_QUOTES + value + StrUtil.SINGLE_QUOTES : value);
                    Class<?> fieldType = subRelation.getReceiveKeyField().getType();
                    if (ClassUtil.isNormalClass(fieldType)) {
                        subRelation.getReceiveKeyField().set(dto, SpringUtil.getBean(subRelation.getSubClass()).selectByField(singleSqlField));
                    } else if (ClassUtil.isCollection(fieldType)) {
                        subRelation.getReceiveKeyField().set(dto, SpringUtil.getBean(subRelation.getSubClass()).selectListByField(singleSqlField));
                    } else {
                        throw new UtilException(UtilErrorConstants.MergeUtil.RECEIVE_KEY_TYPE_ERROR.getInfo());
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                break;
            case COLLECTION:
                if (CollUtil.isEmpty(dtoList))
                    return;
                Set<Object> findInSet = dtoList.stream().map(item -> {
                    try {
                        Object value = subRelation.getMainKeyField().get(item);
                        if (value instanceof String)
                            return StrUtil.SINGLE_QUOTES + value + StrUtil.SINGLE_QUOTES;
                        else
                            return value;
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toSet());
                if (CollUtil.isEmpty(findInSet))
                    return;
                SqlField collSqlField = new SqlField(SqlConstants.OperateType.IN, subFieldName, findInSet);
                List<?> subList = SpringUtil.getBean(subRelation.getSubClass()).selectListByField(collSqlField);
                if (CollUtil.isEmpty(subList))
                    return;
                // assemble relation
                assembleRelationList(dtoList, subRelation, subList);
                break;
        }
    }

    /**
     * 间接关联
     *
     * @param dto         数据对象
     * @param dtoList     数据对象集合
     * @param subRelation 子类关联对象
     * @param dataRow     数据类型
     */
    private static <D> void indirectRelationBuild(D dto, List<D> dtoList, SubRelation subRelation, OperateConstants.DataRow dataRow) {

    }

    /**
     * 组装集合关联数据
     *
     * @param dtoList     数据对象集合
     * @param subRelation 子类关联对象
     * @param subList     子数据集合
     */
    private static <D> void assembleRelationList(List<D> dtoList, SubRelation subRelation, List<?> subList) {
        Class<?> fieldType = subRelation.getReceiveKeyField().getType();
        if (ClassUtil.isNormalClass(fieldType)) {
            Map<Object, Object> subMap = subList.stream().collect(Collectors.toMap(item -> {
                try {
                    return subRelation.getSubKeyField().get(item);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }, item -> item));
            dtoList.forEach(item -> {
                try {
                    Object subObj = subMap.get(subRelation.getMainKeyField().get(item));
                    subRelation.getReceiveKeyField().set(item, subObj);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
        } else if (ClassUtil.isCollection(fieldType)) {
            Map<Object, List<Object>> subMap = subList.stream().collect(Collectors.groupingBy(item -> {
                try {
                    return subRelation.getSubKeyField().get(item);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }, Collectors.mapping(item -> item, Collectors.toList())));
            dtoList.forEach(item -> {
                try {
                    List<Object> subObjList = subMap.get(subRelation.getMainKeyField().get(item));
                    subRelation.getReceiveKeyField().set(item, subObjList);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            throw new UtilException(UtilErrorConstants.MergeUtil.RECEIVE_KEY_TYPE_ERROR.getInfo());
        }
    }

    /**
     * 初始化数据字段关系
     *
     * @param subRelation 子类关联对象
     * @param DClass      数据对象Class
     */
    private static <D> void initRelationField(SubRelation subRelation, Class<D> DClass) {
        // select subKeyField
        Class<? extends BaseEntity> subPClass = SpringUtil.getBean(subRelation.getSubClass()).getPClass();
        Field[] subFields = ReflectUtil.getFields(subPClass);
        subRelation.setSubKeyField(Arrays.stream(subFields).filter(field -> {
            com.xueyi.common.core.annotation.SubRelation relation = field.getAnnotation(com.xueyi.common.core.annotation.SubRelation.class);
            return relation != null && StrUtil.equals(relation.groupName(), subRelation.getGroupName()) && relation.keyType().isSubKey();
        }).findFirst().orElse(null));
        if (ObjectUtil.isNull(subRelation.getSubKeyField()))
            throw new UtilException(StrUtil.format(UtilErrorConstants.MergeUtil.FIELD_NULL.getInfo(), subRelation.getGroupName(), OperateConstants.SubKeyType.SUB_KEY.getInfo()));
        Field[] mainFields = ReflectUtil.getFields(DClass);
        for (Field field : mainFields) {
            com.xueyi.common.core.annotation.SubRelation relation = field.getAnnotation(com.xueyi.common.core.annotation.SubRelation.class);
            if (relation != null) {
                if (StrUtil.notEquals(relation.groupName(), subRelation.getGroupName()))
                    continue;
                if (relation.keyType().isMainKey() && ObjectUtil.isNull(subRelation.getMainKeyField()))
                    subRelation.setMainKeyField(field);
                else if (relation.keyType().isReceiveKey() && ObjectUtil.isNull(subRelation.getReceiveKeyField()))
                    subRelation.setReceiveKeyField(field);
            }
        }
        // if null then assignment idKey
        if (ObjectUtil.isNull(subRelation.getMainKeyField()))
            subRelation.setMainKeyField(Arrays.stream(mainFields).filter(field -> field.getAnnotation(TableId.class) != null).findFirst().orElse(null));
        // if it has any null throws exception
        if (ObjectUtil.isNull(subRelation.getMainKeyField()))
            throw new UtilException(StrUtil.format(UtilErrorConstants.MergeUtil.FIELD_NULL.getInfo(), subRelation.getGroupName(), OperateConstants.SubKeyType.MAIN_KEY.getInfo()));
        if (ObjectUtil.isNull(subRelation.getReceiveKeyField()))
            throw new UtilException(StrUtil.format(UtilErrorConstants.MergeUtil.FIELD_NULL.getInfo(), subRelation.getGroupName(), OperateConstants.SubKeyType.RECEIVE_KEY.getInfo()));
        Class<?> fieldType = subRelation.getReceiveKeyField().getType();
        if (!(ClassUtil.isNormalClass(fieldType) || ClassUtil.isCollection(fieldType)))
            throw new UtilException(UtilErrorConstants.MergeUtil.RECEIVE_KEY_TYPE_ERROR.getInfo());
        subRelation.getMainKeyField().setAccessible(true);
        subRelation.getSubKeyField().setAccessible(true);
        subRelation.getReceiveKeyField().setAccessible(true);
    }
}