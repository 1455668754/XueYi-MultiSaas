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
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 主子关联工具类
 *
 * @author xueyi
 */
public class MergeUtil {

    /**
     * 子数据映射关联 | 查询
     *
     * @param dto         数据对象
     * @param subRelation 子类关联对象
     * @param DClass      数据对象Class
     * @return 数据对象
     */
    public static <D> D subMerge(D dto, SubRelation subRelation, Class<D> DClass) {
        if (ObjectUtil.isNull(dto) || ObjectUtil.isNull(subRelation))
            return dto;
        initRelationField(subRelation, DClass);
        switch (subRelation.getRelationType()) {
            case DIRECT -> assembleDirectObj(dto, subRelation);
            case INDIRECT -> assembleIndirectObj(dto, subRelation);
        }
        return dto;
    }

    /**
     * 子数据映射关联 | 查询（批量）
     *
     * @param dtoList     数据对象集合
     * @param subRelation 子类关联对象
     * @param DClass      数据对象Class
     * @return 数据对象集合
     */
    public static <D> List<D> subMerge(List<D> dtoList, SubRelation subRelation, Class<D> DClass) {
        if (ObjectUtil.isNull(subRelation) || CollUtil.isEmpty(dtoList))
            return dtoList;
        initRelationField(subRelation, DClass);
        switch (subRelation.getRelationType()) {
            case DIRECT -> assembleDirectList(dtoList, subRelation);
            case INDIRECT -> assembleIndirectList(dtoList, subRelation);
        }
        return dtoList;
    }


    /**
     * 子数据映射关联 | 新增
     *
     * @param dto         数据对象
     * @param subRelation 子类关联对象
     * @param DClass      数据对象Class
     * @return 结果
     */
    public static <D> int addMerge(D dto, SubRelation subRelation, Class<D> DClass) {
        if (ObjectUtil.isNull(dto) || ObjectUtil.isNull(subRelation))
            return NumberUtil.Zero;
        initRelationField(subRelation, DClass);
        if (subRelation.getRelationType().isIndirect()) {
            return insertIndirectObj(dto, subRelation);
        }
        return NumberUtil.Zero;
    }

    /**
     * 子数据映射关联 | 新增（批量）
     *
     * @param dtoList     数据对象集合
     * @param subRelation 子类关联对象
     * @param DClass      数据对象Class
     * @return 结果
     */
    public static <D> int addMerge(Collection<D> dtoList, SubRelation subRelation, Class<D> DClass) {
        if (CollUtil.isEmpty(dtoList) || ObjectUtil.isNull(subRelation))
            return NumberUtil.Zero;
        initRelationField(subRelation, DClass);
        if (subRelation.getRelationType().isIndirect()) {
            return insertIndirectList(dtoList, subRelation);
        }
        return NumberUtil.Zero;
    }

    /**
     * 子数据映射关联 | 修改
     *
     * @param originDto   源数据对象
     * @param newDto      新数据对象
     * @param subRelation 子类关联对象
     * @param DClass      数据对象Class
     */
    public static <D> int editMerge(D originDto, D newDto, SubRelation subRelation, Class<D> DClass) {
        if (ObjectUtil.isAllEmpty(originDto, newDto) || ObjectUtil.isNull(subRelation))
            return NumberUtil.Zero;
        initRelationField(subRelation, DClass);
        if (subRelation.getRelationType().isIndirect()) {
            return updateIndirectObj(originDto, newDto, subRelation);
        }
        return NumberUtil.Zero;
    }

    /**
     * 子数据映射关联 | 修改（批量）
     *
     * @param originList  源数据对象集合
     * @param newList     新数据对象集合
     * @param subRelation 子类关联对象
     * @param DClass      数据对象Class
     */
    public static <D> int editMerge(Collection<D> originList, Collection<D> newList, SubRelation subRelation, Class<D> DClass) {
        if ((CollUtil.isEmpty(originList) && CollUtil.isEmpty(newList)) || ObjectUtil.isNull(subRelation))
            return NumberUtil.Zero;
        initRelationField(subRelation, DClass);
        if (subRelation.getRelationType().isIndirect()) {
            return updateIndirectList(originList, newList, subRelation);
        }
        return NumberUtil.Zero;
    }

    /**
     * 子数据映射关联 | 删除
     *
     * @param dto         数据对象
     * @param subRelation 子类关联对象
     * @param DClass      数据对象Class
     */
    public static <D> int delMerge(D dto, SubRelation subRelation, Class<D> DClass) {
        if (ObjectUtil.isNull(dto) || ObjectUtil.isNull(subRelation))
            return NumberUtil.Zero;
        initRelationField(subRelation, DClass);
        if (subRelation.getRelationType().isIndirect()) {
            return deleteIndirectObj(dto, subRelation);
        }
        return NumberUtil.Zero;
    }

    /**
     * 子数据映射关联 | 删除（批量）
     *
     * @param dtoList     数据对象集合
     * @param subRelation 子类关联对象
     * @param DClass      数据对象Class
     */
    public static <D> int delMerge(Collection<D> dtoList, SubRelation subRelation, Class<D> DClass) {
        if (CollUtil.isEmpty(dtoList) || ObjectUtil.isNull(subRelation))
            return NumberUtil.Zero;
        initRelationField(subRelation, DClass);
        if (subRelation.getRelationType().isIndirect()) {
            return deleteIndirectList(dtoList, subRelation);
        }
        return NumberUtil.Zero;
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
            subRelation.setSubKeyField(Arrays.stream(subFields).filter(field -> ObjectUtil.isNotNull(field.getAnnotation(TableId.class))).findFirst().orElse(null));
        if (ObjectUtil.isNull(subRelation.getSubKeyField()))
            throw new UtilException(StrUtil.format(UtilErrorConstants.MergeUtil.FIELD_NULL.getInfo(), subRelation.getGroupName(), OperateConstants.SubKeyType.SUB_KEY.getInfo()));

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
            // 间接连接时，关联类class必须存在
            if (ObjectUtil.isNull(subRelation.getMergeClass()))
                throw new UtilException(StrUtil.format(UtilErrorConstants.MergeUtil.MERGE_CLASS_NULL.getInfo(), subRelation.getGroupName()));
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

            Class<? extends BasisEntity> mergePoClass = TypeUtil.getClazz(subRelation.getMergeClass().getGenericSuperclass(), NumberUtil.Zero);
            // 间接连接时，关联类对象class必须存在 且必须为merge类型
            if (ObjectUtil.isNull(mergePoClass))
                throw new UtilException(StrUtil.format(UtilErrorConstants.MergeUtil.MERGE_PO_CLASS_NULL.getInfo(), subRelation.getGroupName()));
            subRelation.setMergePoClass(mergePoClass);
            // select mergeMainKeyField and mergeSubKeyField
            Field[] mergeFields = ReflectUtil.getFields(mergePoClass);
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
            subRelation.setSubFieldSqlName(ObjectUtil.isNotNull(idField) && StrUtil.isNotEmpty(idField.value())
                    ? idField.value()
                    : StrUtil.toUnderlineCase(subRelation.getSubKeyField().getName()));
        } else {
            subRelation.setSubFieldSqlName(StrUtil.isNotEmpty(tableField.value())
                    ? tableField.value()
                    : StrUtil.toUnderlineCase(subRelation.getSubKeyField().getName()));
        }

        // init mergeKeyField sql name
        if (subRelation.getRelationType().isIndirect()) {
            TableField mergeMainKeyField = subRelation.getMergeMainKeyField().getAnnotation(TableField.class);
            subRelation.setMergeMainFieldSqlName(ObjectUtil.isNotNull(mergeMainKeyField) && StrUtil.isNotEmpty(mergeMainKeyField.value())
                    ? mergeMainKeyField.value()
                    : StrUtil.toUnderlineCase(subRelation.getMergeMainKeyField().getName()));
            TableField mergeSubKeyField = subRelation.getMergeSubKeyField().getAnnotation(TableField.class);
            subRelation.setMergeMainFieldSqlName(ObjectUtil.isNotNull(mergeSubKeyField) && StrUtil.isNotEmpty(mergeSubKeyField.value())
                    ? mergeSubKeyField.value()
                    : StrUtil.toUnderlineCase(subRelation.getMergeSubKeyField().getName()));
        }
    }

    /**
     * 组装数据对象关联数据 | 直接关联
     *
     * @param dto         数据对象
     * @param subRelation 子类关联对象
     */
    private static <D> void assembleDirectObj(D dto, SubRelation subRelation) {
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
    private static <D> void assembleDirectList(List<D> dtoList, SubRelation subRelation) {
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
                    Collectors.toMap(item -> getFieldObj(item, subRelation.getSubKeyField()), Function.identity()));
            dtoList.forEach(item -> {
                Object subObj = subMap.get(getFieldObj(item, subRelation.getMainKeyField()));
                setField(item, subRelation.getReceiveKeyField(), subObj);
            });
        } else if (ClassUtil.isCollection(fieldType)) {
            Map<Object, List<Object>> subMap = subList.stream().collect(
                    Collectors.groupingBy(item -> getFieldObj(item, subRelation.getSubKeyField())));
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
    private static <D> void assembleIndirectObj(D dto, SubRelation subRelation) {
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
    private static <D> void assembleIndirectList(List<D> dtoList, SubRelation subRelation) {
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
                Collectors.toMap(item -> getFieldObj(item, subRelation.getSubKeyField()), Function.identity()));
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
     * 新增关联数据 | 间接关联
     *
     * @param dto         数据对象
     * @param subRelation 子类关联对象
     */
    private static <D, MP extends BasisEntity> int insertIndirectObj(D dto, SubRelation subRelation) {
        Collection<MP> mergeList = insertIndirectBuild(dto, subRelation);
        if (CollUtil.isEmpty(mergeList))
            return NumberUtil.Zero;
        return SpringUtil.getBean(subRelation.getMergeClass()).insertByField(mergeList, subRelation.getMergePoClass());
    }

    /**
     * 新增关联数据 | 间接关联
     *
     * @param dtoList     数据对象集合
     * @param subRelation 子类关联对象
     */
    private static <D, MP extends BasisEntity> int insertIndirectList(Collection<D> dtoList, SubRelation subRelation) {
        List<MP> list = new ArrayList<>();
        for (D dto : dtoList) {
            List<MP> mergeList = insertIndirectBuild(dto, subRelation);
            if (CollUtil.isNotEmpty(mergeList)) {
                list.addAll(mergeList);
            }
        }
        if (CollUtil.isEmpty(list))
            return NumberUtil.Zero;
        return SpringUtil.getBean(subRelation.getMergeClass()).insertByField(list, subRelation.getMergePoClass());
    }

    /**
     * 新增关联数据 | 间接关联
     *
     * @param originDto   源数据对象
     * @param newDto      新数据对象
     * @param subRelation 子类关联对象
     */
    private static <D, MP extends BasisEntity> int updateIndirectObj(D originDto, D newDto, SubRelation subRelation) {
        return 0;
    }

    /**
     * 新增关联数据 | 间接关联
     *
     * @param originList  源数据对象集合
     * @param newList     新数据对象集合
     * @param subRelation 子类关联对象
     */
    private static <D, MP extends BasisEntity> int updateIndirectList(Collection<D> originList, Collection<D> newList, SubRelation subRelation) {
        return 0;
    }

    /**
     * 新增关联数据 | 间接关联
     *
     * @param dto         数据对象
     * @param subRelation 子类关联对象
     */
    private static <D> int deleteIndirectObj(D dto, SubRelation subRelation) {
        Object delKey = getFieldObj(dto, subRelation.getMainKeyField());
        SqlField sqlField = new SqlField(SqlConstants.OperateType.EQ, subRelation.getMergeMainFieldSqlName(), delKey);
        return SpringUtil.getBean(subRelation.getMergeClass()).deleteByField(sqlField);
    }

    /**
     * 新增关联数据 | 间接关联
     *
     * @param dtoList     数据对象集合
     * @param subRelation 子类关联对象
     */
    private static <D> int deleteIndirectList(Collection<D> dtoList, SubRelation subRelation) {
        Set<Object> delKeys = dtoList.stream().map(item -> getFieldObj(item, subRelation.getMainKeyField())).collect(Collectors.toSet());
        SqlField sqlField = new SqlField(SqlConstants.OperateType.IN, subRelation.getMergeMainFieldSqlName(), delKeys);
        return SpringUtil.getBean(subRelation.getMergeClass()).deleteByField(sqlField);
    }

    /**
     * 新增关联数据 | 数据组装 | 间接关联
     *
     * @param dto         数据对象
     * @param subRelation 子类关联对象
     * @return 数据对象集合
     */
    @SuppressWarnings("unchecked")
    private static <D, MP extends BasisEntity> List<MP> insertIndirectBuild(D dto, SubRelation subRelation) {
        Object mainKey = getFieldObj(dto, subRelation.getMainKeyField());
        Object mergeObj = getFieldObj(dto, subRelation.getMergeSubKeyField());
        Class<?> fieldType = subRelation.getMergeSubKeyField().getType();
        List<MP> mergeList = new ArrayList<>();
        if (ClassUtil.isCollection(fieldType)) {
            Collection<Object> objColl = (Collection<Object>) mergeObj;
            mergeList = objColl.stream().distinct().map(item -> {
                Object mergePo = createObj(subRelation.getMergePoClass());
                setField(mergePo, subRelation.getMergeMainKeyField(), mainKey);
                setField(mergePo, subRelation.getMergeSubKeyField(), item);
                return (MP) mergePo;
            }).collect(Collectors.toList());
        } else if (ClassUtil.isArray(fieldType)) {
            Object[] objArr = (Object[]) mergeObj;
            mergeList = Arrays.stream(objArr).distinct().map(item -> {
                Object mergePo = createObj(subRelation.getMergePoClass());
                setField(mergePo, subRelation.getMergeMainKeyField(), mainKey);
                setField(mergePo, subRelation.getMergeSubKeyField(), item);
                return (MP) mergePo;
            }).collect(Collectors.toList());
        }
        return mergeList;
    }

    /**
     * 获取数据对象指定字段值
     *
     * @param item  数据对象
     * @param field 字段
     * @return 字段值
     */
    private static <T> Object getFieldObj(T item, Field field) {
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
    private static <T> Object getFieldSql(T item, Field field) {
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

    /**
     * 创建数据对象
     *
     * @param clazz 对象Class
     * @return 数据对象
     */
    private static <D> D createObj(Class<D> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}