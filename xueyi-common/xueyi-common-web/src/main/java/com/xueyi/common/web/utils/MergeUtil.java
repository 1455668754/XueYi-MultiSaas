package com.xueyi.common.web.utils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.core.constant.error.UtilErrorConstants;
import com.xueyi.common.core.exception.UtilException;
import com.xueyi.common.core.utils.core.*;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.web.entity.base.BasisEntity;
import com.xueyi.common.web.entity.domain.SqlField;
import com.xueyi.common.web.entity.domain.SubRelation;

import java.lang.reflect.Field;
import java.util.*;
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
        if (ObjectUtil.isNull(dto) || CollUtil.isEmpty(subRelationList))
            return dto;
        for (SubRelation subRelation : subRelationList) {
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
        if (CollUtil.isEmpty(dtoList) || CollUtil.isEmpty(subRelationList))
            return dtoList;
        for (SubRelation subRelation : subRelationList) {
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
        switch (dataRow) {
            case SINGLE:
                assembleDirectRelationObj(dto, subRelation);
                break;
            case COLLECTION:
                assembleDirectRelationList(dtoList, subRelation);
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
        switch (dataRow) {
            case SINGLE:
                assembleIndirectRelationObj(dto, subRelation);
                break;
            case COLLECTION:
                assembleIndirectRelationList(dtoList, subRelation);
                break;
        }
    }

    /**
     * 组装数据对象关联数据 | 直接关联
     *
     * @param dto         数据对象
     * @param subRelation 子类关联对象
     */
    private static <D> void assembleDirectRelationObj(D dto, SubRelation subRelation) {
        try {
            Object value = subRelation.getMainKeyField().get(dto);
            SqlField singleSqlField = new SqlField(SqlConstants.OperateType.EQ, subRelation.getSubFieldSqlName(),
                    value instanceof String ? StrUtil.SINGLE_QUOTES + value + StrUtil.SINGLE_QUOTES : value);
            // assemble dto receive key relation
            Class<?> fieldType = subRelation.getReceiveKeyField().getType();
            if (ClassUtil.isNormalClass(fieldType)) {
                subRelation.getReceiveKeyField().set(dto, SpringUtil.getBean(subRelation.getSubClass()).selectByField(singleSqlField));
            } else if (ClassUtil.isCollection(fieldType)) {
                subRelation.getReceiveKeyField().set(dto, SpringUtil.getBean(subRelation.getSubClass()).selectListByField(singleSqlField));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 组装集合关联数据 | 直接关联
     *
     * @param dtoList     数据对象集合
     * @param subRelation 子类关联对象
     */
    private static <D> void assembleDirectRelationList(List<D> dtoList, SubRelation subRelation) {
        Set<Object> findInSet = dtoList.stream().map(item -> getFieldSql(item, subRelation.getMainKeyField())).collect(Collectors.toSet());
        if (CollUtil.isEmpty(findInSet))
            return;
        SqlField collSqlField = new SqlField(SqlConstants.OperateType.IN, subRelation.getSubFieldSqlName(), findInSet);
        List<?> subList = SpringUtil.getBean(subRelation.getSubClass()).selectListByField(collSqlField);
        if (CollUtil.isEmpty(subList))
            return;

        // assemble receive key relation
        Class<?> fieldType = subRelation.getReceiveKeyField().getType();
        if (ClassUtil.isNormalClass(fieldType)) {
            Map<Object, Object> subMap = subList.stream().collect(
                    Collectors.toMap(item -> getFieldObj(item, subRelation.getSubKeyField()), item -> item));
            dtoList.forEach(item -> {
                Object subObj = subMap.get(getFieldObj(item, subRelation.getMainKeyField()));
                setField(item, subRelation.getReceiveKeyField(), subObj);
            });
        } else if (ClassUtil.isCollection(fieldType)) {
            Map<Object, List<Object>> subMap = subList.stream().collect(
                    Collectors.groupingBy(item -> getFieldObj(item, subRelation.getSubKeyField())
                            , Collectors.mapping(item -> item, Collectors.toList())));
            dtoList.forEach(item -> {
                List<Object> subObjList = subMap.get(getFieldObj(item, subRelation.getMainKeyField()));
                setField(item, subRelation.getReceiveKeyField(), subObjList);
            });
        }
    }

    /**
     * 组装数据对象关联数据 | 间接关联
     *
     * @param dto         数据对象
     * @param subRelation 子类关联对象
     */
    private static <D> void assembleIndirectRelationObj(D dto, SubRelation subRelation) {
        try {
            Object value = subRelation.getMainKeyField().get(dto);
            SqlField singleMergeSqlField = new SqlField(SqlConstants.OperateType.EQ, subRelation.getMergeMainFieldSqlName(),
                    value instanceof String ? StrUtil.SINGLE_QUOTES + value + StrUtil.SINGLE_QUOTES : value);
            List<?> mergeList = SpringUtil.getBean(subRelation.getMergeClass()).selectListByField(singleMergeSqlField);
            // if null return
            if (CollUtil.isEmpty(mergeList))
                return;
            // select merge relation list
            List<Object> mergeKeyList = mergeList.stream().map(item -> getFieldObj(item, subRelation.getMergeSubKeyField()))
                    .collect(Collectors.toList());
            // assemble merge arr
            if (ObjectUtil.isNotNull(subRelation.getReceiveArrKeyField())) {
                Class<?> fieldType = subRelation.getReceiveArrKeyField().getType();
                if (ClassUtil.isArray(fieldType)) {
                    subRelation.getReceiveArrKeyField().set(dto, mergeKeyList.toArray());
                } else if (ClassUtil.isCollection(fieldType)) {
                    subRelation.getReceiveArrKeyField().set(dto, mergeKeyList);
                }
            }
            if (ObjectUtil.isNull(subRelation.getReceiveKeyField()))
                return;
            Set<Object> findInSet = mergeKeyList.stream().map(item ->
                    item instanceof String ? StrUtil.SINGLE_QUOTES + item + StrUtil.SINGLE_QUOTES : item).collect(Collectors.toSet());
            SqlField singleSubSqlField = new SqlField(SqlConstants.OperateType.IN, subRelation.getSubFieldSqlName(), findInSet);
            List<?> subList = SpringUtil.getBean(subRelation.getSubClass()).selectListByField(singleSubSqlField);
            if (CollUtil.isEmpty(subList))
                return;
            subRelation.getReceiveKeyField().set(dto, subList);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 组装集合关联数据 | 间接关联
     *
     * @param dtoList     数据对象集合
     * @param subRelation 子类关联对象
     */
    private static <D> void assembleIndirectRelationList(List<D> dtoList, SubRelation subRelation) {
        Set<Object> findInSet = dtoList.stream().map(item -> getFieldSql(item, subRelation.getMainKeyField())).collect(Collectors.toSet());
        if (CollUtil.isEmpty(findInSet))
            return;
        SqlField collMergeSqlField = new SqlField(SqlConstants.OperateType.IN, subRelation.getMergeMainFieldSqlName(), findInSet);
        List<?> mergeList = SpringUtil.getBean(subRelation.getMergeClass()).selectListByField(collMergeSqlField);
        if (CollUtil.isEmpty(mergeList))
            return;

        // build merge map
        Map<Object, List<Object>> mergeMap = mergeList.stream().collect(
                Collectors.groupingBy(item -> getFieldObj(item, subRelation.getMergeMainKeyField())
                        , Collectors.mapping(item -> getFieldObj(item, subRelation.getMergeSubKeyField()), Collectors.toList())));
        // assemble merge arr
        if (ObjectUtil.isNotNull(subRelation.getReceiveArrKeyField())) {
            Class<?> fieldType = subRelation.getReceiveArrKeyField().getType();
            dtoList.forEach(item -> {
                List<Object> mergeRelationList = mergeMap.get(getFieldObj(item, subRelation.getMainKeyField()));
                if (CollUtil.isNotEmpty(mergeRelationList)) {
                    if (ClassUtil.isArray(fieldType)) {
                        setField(item, subRelation.getReceiveArrKeyField(), mergeRelationList.toArray());
                    } else if (ClassUtil.isCollection(fieldType)) {
                        setField(item, subRelation.getReceiveArrKeyField(), mergeRelationList);
                    }
                }
            });
        }
        // if receiveKey is null
        if (ObjectUtil.isNull(subRelation.getReceiveKeyField()))
            return;

        // select sub relation list
        List<Object> subFindInSet = mergeList.stream().map(item -> getFieldSql(item, subRelation.getMergeSubKeyField())).collect(Collectors.toList());
        SqlField collSqlField = new SqlField(SqlConstants.OperateType.IN, subRelation.getSubFieldSqlName(), subFindInSet);
        List<?> mergeSubList = SpringUtil.getBean(subRelation.getSubClass()).selectListByField(collSqlField);
        if (CollUtil.isEmpty(mergeSubList))
            return;
        Map<Object, Object> subMap = mergeSubList.stream().collect(
                Collectors.toMap(item -> getFieldObj(item, subRelation.getSubKeyField()), item -> item));
        // assemble receive key relation
        dtoList.forEach(item -> {
            List<Object> mergeRelationList = mergeMap.get(getFieldObj(item, subRelation.getMainKeyField()));
            if (CollUtil.isEmpty(mergeRelationList))
                return;
            List<Object> subList = new ArrayList<>();
            mergeRelationList.forEach(mergeItem -> {
                Object sub = subMap.get(mergeItem);
                if (ObjectUtil.isNotNull(sub))
                    subList.add(sub);
            });
            if (CollUtil.isNotEmpty(subList))
                setField(item, subRelation.getReceiveKeyField(), subList);
        });
    }

    /**
     * 初始化数据字段关系
     *
     * @param subRelation 子类关联对象
     * @param mainDClass  数据对象Class
     */
    private static <D> void initRelationField(SubRelation subRelation, Class<D> mainDClass) {
        if (subRelation.getRelationType().isDirect()) {
            if (ArrayUtil.isAllNotNull(subRelation.getMainKeyField(), subRelation.getSubKeyField(), subRelation.getReceiveKeyField()))
                return;
        } else if (subRelation.getRelationType().isIndirect()) {
            if (ArrayUtil.isAllNotNull(subRelation.getMainKeyField(), subRelation.getSubKeyField(), subRelation.getMergeMainKeyField(), subRelation.getMergeSubKeyField())
                    && ArrayUtil.isAllNull(subRelation.getReceiveKeyField(), subRelation.getReceiveArrKeyField()) && StrUtil.isNotEmpty(subRelation.getMergeMainFieldSqlName()))
                return;
        } else if (StrUtil.isNotEmpty(subRelation.getSubFieldSqlName()))
            return;
        // select subKeyField
        Class<? extends BaseEntity> subPClass = SpringUtil.getBean(subRelation.getSubClass()).getPClass();
        Field[] subFields = ReflectUtil.getFields(subPClass);
        for (Field field : subFields) {
            com.xueyi.common.core.annotation.SubRelation relation = field.getAnnotation(com.xueyi.common.core.annotation.SubRelation.class);
            if (relation != null && StrUtil.equals(relation.groupName(), subRelation.getGroupName())
                    && relation.keyType().isSubKey() && ObjectUtil.isNull(subRelation.getSubKeyField())) {
                subRelation.setSubKeyField(field);
                break;
            }
        }
        // if relationType is indirect and subKeyField is null then assignment idKey
        if (subRelation.getRelationType().isIndirect() && ObjectUtil.isNull(subRelation.getSubKeyField()))
            subRelation.setSubKeyField(Arrays.stream(subFields).filter(field -> field.getAnnotation(TableId.class) != null).findFirst().orElse(null));
        if (ObjectUtil.isNull(subRelation.getSubKeyField()))
            throw new UtilException(StrUtil.format(UtilErrorConstants.MergeUtil.FIELD_NULL.getInfo()
                    , subRelation.getGroupName(), OperateConstants.SubKeyType.SUB_KEY.getInfo()));

        // select mainKeyField and ( receiveKey or receiveArrKey )
        Field[] mainFields = ReflectUtil.getFields(mainDClass);
        for (Field field : mainFields) {
            com.xueyi.common.core.annotation.SubRelation relation = field.getAnnotation(com.xueyi.common.core.annotation.SubRelation.class);
            if (relation == null || StrUtil.notEquals(relation.groupName(), subRelation.getGroupName()))
                continue;
            if (relation.keyType().isMainKey() && ObjectUtil.isNull(subRelation.getMainKeyField()))
                subRelation.setMainKeyField(field);
            else if (relation.keyType().isReceiveKey() && ObjectUtil.isNull(subRelation.getReceiveKeyField()))
                subRelation.setReceiveKeyField(field);
            if (subRelation.getRelationType().isIndirect()) {
                if (relation.keyType().isReceiveArrKey() && ObjectUtil.isNull(subRelation.getReceiveArrKeyField()))
                    subRelation.setReceiveArrKeyField(field);
            }
        }

        // if null then assignment idKey
        if (ObjectUtil.isNull(subRelation.getMainKeyField()))
            subRelation.setMainKeyField(Arrays.stream(mainFields).filter(field -> field.getAnnotation(TableId.class) != null).findFirst().orElse(null));
        // if it has any null throws exception
        if (ObjectUtil.isNull(subRelation.getMainKeyField()))
            throw new UtilException(StrUtil.format(UtilErrorConstants.MergeUtil.FIELD_NULL.getInfo(), subRelation.getGroupName(), OperateConstants.SubKeyType.MAIN_KEY.getInfo()));
        subRelation.getMainKeyField().setAccessible(true);
        subRelation.getSubKeyField().setAccessible(true);

        if (subRelation.getRelationType().isDirect()) {
            // check receiveKey is true
            if (ObjectUtil.isNull(subRelation.getReceiveKeyField())) {
                throw new UtilException(StrUtil.format(UtilErrorConstants.MergeUtil.FIELD_NULL.getInfo(), subRelation.getGroupName(), OperateConstants.SubKeyType.RECEIVE_KEY.getInfo()));
            } else {
                Class<?> fieldType = subRelation.getReceiveKeyField().getType();
                if (!(ClassUtil.isNormalClass(fieldType) || ClassUtil.isCollection(fieldType)))
                    throw new UtilException(UtilErrorConstants.MergeUtil.RECEIVE_KEY_TYPE_ERROR.getInfo());
                subRelation.getReceiveKeyField().setAccessible(true);
            }
        }
        // if is indirect
        else if (subRelation.getRelationType().isIndirect()) {
            if (ObjectUtil.isAllEmpty(subRelation.getReceiveKeyField(), subRelation.getReceiveArrKeyField()))
                throw new UtilException(StrUtil.format(UtilErrorConstants.MergeUtil.RECEIVE_NULL.getInfo()));
            // check receiveKey is true
            if (ObjectUtil.isNotNull(subRelation.getReceiveKeyField())) {
                Class<?> fieldType = subRelation.getReceiveKeyField().getType();
                if (!(ClassUtil.isCollection(fieldType)))
                    throw new UtilException(UtilErrorConstants.MergeUtil.RECEIVE_KEY_TYPE_INDIRECT_ERROR.getInfo());
                subRelation.getReceiveKeyField().setAccessible(true);
            }
            // check receiveArrKey is true
            if (ObjectUtil.isNotNull(subRelation.getReceiveArrKeyField())) {
                Class<?> fieldType = subRelation.getReceiveArrKeyField().getType();
                if (!(ClassUtil.isArray(fieldType) || ClassUtil.isCollection(fieldType)))
                    throw new UtilException(UtilErrorConstants.MergeUtil.RECEIVE_ARR_KEY_TYPE_ERROR.getInfo());
                subRelation.getReceiveArrKeyField().setAccessible(true);
            }

            Class<? extends BasisEntity> mergePClass = TypeUtil.getClazz(subRelation.getMergeClass().getGenericSuperclass(), NumberUtil.One);
            // select mergeMainKeyField and mergeSubKeyField
            Field[] mergeFields = ReflectUtil.getFields(mergePClass);
            for (Field field : mergeFields) {
                com.xueyi.common.core.annotation.SubRelation relation = field.getAnnotation(com.xueyi.common.core.annotation.SubRelation.class);
                if (relation != null) {
                    if (StrUtil.notEquals(relation.groupName(), subRelation.getGroupName()))
                        continue;
                    if (relation.keyType().isMergeMainKey() && ObjectUtil.isNull(subRelation.getMergeMainKeyField()))
                        subRelation.setMergeMainKeyField(field);
                    else if (relation.keyType().isMergeSubKey() && ObjectUtil.isNull(subRelation.getMergeSubKeyField()))
                        subRelation.setMergeSubKeyField(field);
                }
            }
            if (ObjectUtil.isNull(subRelation.getMergeMainKeyField()))
                throw new UtilException(StrUtil.format(UtilErrorConstants.MergeUtil.FIELD_NULL.getInfo(), subRelation.getGroupName(), OperateConstants.SubKeyType.MERGE_MAIN_KEY.getInfo()));
            if (ObjectUtil.isNull(subRelation.getMergeSubKeyField()))
                throw new UtilException(StrUtil.format(UtilErrorConstants.MergeUtil.FIELD_NULL.getInfo(), subRelation.getGroupName(), OperateConstants.SubKeyType.MERGE_SUB_KEY.getInfo()));
            subRelation.getMergeMainKeyField().setAccessible(true);
            subRelation.getMergeSubKeyField().setAccessible(true);
        }

        // init subKeyField sql name
        TableField tableField = subRelation.getSubKeyField().getAnnotation(TableField.class);
        if (ObjectUtil.isNull(tableField)) {
            TableId idField = subRelation.getSubKeyField().getAnnotation(TableId.class);
            subRelation.setSubFieldSqlName(StrUtil.isNotEmpty(idField.value())
                    ? idField.value()
                    : StrUtil.toUnderlineCase(subRelation.getSubKeyField().getName()));
        } else {
            subRelation.setSubFieldSqlName(StrUtil.isNotEmpty(tableField.value())
                    ? tableField.value()
                    : StrUtil.toUnderlineCase(subRelation.getSubKeyField().getName()));
        }

        if (subRelation.getRelationType().isIndirect()) {
            TableField mergeTableField = subRelation.getMergeMainKeyField().getAnnotation(TableField.class);
            subRelation.setMergeMainFieldSqlName(StrUtil.isNotEmpty(mergeTableField.value())
                    ? mergeTableField.value()
                    : StrUtil.toUnderlineCase(subRelation.getMergeMainKeyField().getName()));
        }
    }

    /**
     * 获取数据对象指定字段值
     *
     * @param item  数据对象
     * @param field 字段
     * @return 字段值
     */
    private static Object getFieldObj(Object item, Field field) {
        try {
            return field.get(item);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取数据对象指定字段值 | SQL使用
     *
     * @param item  数据对象
     * @param field 字段
     * @return 字段值
     */
    private static Object getFieldSql(Object item, Field field) {
        try {
            Object value = field.get(item);
            if (value instanceof String)
                return StrUtil.SINGLE_QUOTES + value + StrUtil.SINGLE_QUOTES;
            else
                return value;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 数据对象指定字段赋值
     *
     * @param item  数据对象
     * @param field 字段
     * @param obj   字段值
     */
    private static void setField(Object item, Field field, Object obj) {
        try {
            field.set(item, obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 数据对象指定字段赋值
     *
     * @param item  数据对象
     * @param field 字段
     * @param coll  字段值集合
     */
    private static void setField(Object item, Field field, Collection<?> coll) {
        try {
            field.set(item, coll);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}