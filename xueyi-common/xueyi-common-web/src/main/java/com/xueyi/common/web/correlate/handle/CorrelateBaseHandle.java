package com.xueyi.common.web.correlate.handle;

import com.xueyi.common.core.exception.UtilException;
import com.xueyi.common.core.utils.core.ArrayUtil;
import com.xueyi.common.core.utils.core.ClassUtil;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.ConvertUtil;
import com.xueyi.common.core.utils.core.NumberUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.web.entity.base.BasisEntity;
import com.xueyi.common.web.correlate.contant.CorrelateConstants;
import com.xueyi.common.web.correlate.domain.BaseCorrelate;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 主从关联数据处理 | 通用
 *
 * @author xueyi
 */
@Slf4j
public sealed class CorrelateBaseHandle permits CorrelateDirectHandle, CorrelateIndirectHandle, CorrelateRemoteHandle {

    /**
     * 获取数据键集合
     *
     * @param info  数据对象
     * @param orm   关联映射执行对象
     * @param field 待提取字段
     * @return 数据主键集合
     */
    protected static <D extends BasisEntity, ORM extends BaseCorrelate.ORM> Set<Object> getFieldKeys(D info, ORM orm, Field field) {
        return getFieldKeys(info, null, orm, field);
    }

    /**
     * 获取数据键集合
     *
     * @param infoList 数据对象集合
     * @param orm      关联映射执行对象
     * @param field    待提取字段
     * @return 数据主键集合
     */
    protected static <D extends BasisEntity, ORM extends BaseCorrelate.ORM, Coll extends Collection<D>> Set<Object> getFieldKeys(Coll infoList, ORM orm, Field field) {
        return getFieldKeys(null, infoList, orm, field);
    }

    /**
     * 获取数据键集合
     *
     * @param info     数据对象
     * @param infoList 数据对象集合
     * @param orm      关联映射执行对象
     * @param field    待提取字段
     * @return 数据主键集合
     */
    private static <D extends BasisEntity, ORM extends BaseCorrelate.ORM, Coll extends Collection<D>> Set<Object> getFieldKeys(D info, Coll infoList, ORM orm, Field field) {
        Set<Object> keys = new HashSet<>();
        switch (orm.getMergeType()) {
            case DIRECT -> {
                if (ObjectUtil.isNotNull(info)) {
                    Object directKey = getFieldObj(info, field);
                    keys.add(directKey);
                }
                if (CollUtil.isNotEmpty(infoList)) {
                    Set<Object> directKeys = infoList.stream().filter(ObjectUtil::isNotNull).map(item -> getFieldSql(item, field))
                            .filter(ObjectUtil::isNotNull).collect(Collectors.toSet());
                    keys.addAll(directKeys);
                }
            }
            case INDIRECT -> {
                if (ObjectUtil.isNotNull(info)) {
                    Object obj = getFieldSql(info, field);
                    if (obj instanceof Collection<?> coll) {
                        keys.addAll(coll);
                    }
                }
                if (CollUtil.isNotEmpty(infoList)) {
                    Set<Object> indirectKeys = infoList.stream().filter(ObjectUtil::isNotNull)
                            .flatMap(item -> {
                                Object obj = getFieldSql(item, field);
                                if (obj instanceof Collection<?> coll) {
                                    return coll.stream();
                                }
                                return null;
                            }).filter(ObjectUtil::isNotNull).collect(Collectors.toSet());
                    keys.addAll(indirectKeys);
                }
            }
        }
        return keys;
    }

    /**
     * 组装数据键
     *
     * @param dto        数据对象
     * @param subList    从数据对象集合
     * @param subDataRow 从数据关联数据类型
     * @param mergeType  间接关联类型
     * @param mainField  主数据待提取字段
     * @param slaveField 从数据待提取字段
     * @param subField   主数据待设值字段
     */
    protected static <D extends BaseEntity, S extends BasisEntity, CollS extends Collection<S>> void setSubField(D dto, CollS subList, CorrelateConstants.DataRow subDataRow, CorrelateConstants.MergeType mergeType, Field mainField, Field slaveField, Field subField) {
        setSubField(dto, null, subList, subDataRow, mergeType, mainField, slaveField, subField, null);
    }

    /**
     * 组装数据键
     *
     * @param dto         数据对象
     * @param subList     从数据对象集合
     * @param subDataRow  从数据关联数据类型
     * @param mergeType   间接关联类型
     * @param mainField   主数据待提取字段
     * @param slaveField  从数据待提取字段
     * @param subField    主数据待设值字段
     * @param subKeyField 主数据设值提取字段
     */
    protected static <D extends BaseEntity, S extends BasisEntity, CollS extends Collection<S>> void setSubField(D dto, CollS subList, CorrelateConstants.DataRow subDataRow, CorrelateConstants.MergeType mergeType, Field mainField, Field slaveField, Field subField, Field subKeyField) {
        setSubField(dto, null, subList, subDataRow, mergeType, mainField, slaveField, subField, subKeyField);
    }

    /**
     * 组装数据键
     *
     * @param dtoList    数据对象集合
     * @param subList    从数据对象集合
     * @param subDataRow 从数据关联数据类型
     * @param mergeType  间接关联类型
     * @param mainField  主数据待提取字段
     * @param slaveField 从数据待提取字段
     * @param subField   主数据待设值字段
     */
    protected static <D extends BaseEntity, S extends BasisEntity, Coll extends Collection<D>, CollS extends Collection<S>> void setSubField(Coll dtoList, CollS subList, CorrelateConstants.DataRow subDataRow, CorrelateConstants.MergeType mergeType, Field mainField, Field slaveField, Field subField) {
        setSubField(null, dtoList, subList, subDataRow, mergeType, mainField, slaveField, subField, null);
    }

    /**
     * 组装数据键
     *
     * @param dtoList     数据对象集合
     * @param subList     从数据对象集合
     * @param subDataRow  从数据关联数据类型
     * @param mergeType   间接关联类型
     * @param mainField   主数据待提取字段
     * @param slaveField  从数据待提取字段
     * @param subField    主数据待设值字段
     * @param subKeyField 主数据设值提取字段
     */
    protected static <D extends BaseEntity, S extends BasisEntity, Coll extends Collection<D>, CollS extends Collection<S>> void setSubField(Coll dtoList, CollS subList, CorrelateConstants.DataRow subDataRow, CorrelateConstants.MergeType mergeType, Field mainField, Field slaveField, Field subField, Field subKeyField) {
        setSubField(null, dtoList, subList, subDataRow, mergeType, mainField, slaveField, subField, subKeyField);
    }

    /**
     * 组装数据键
     *
     * @param dto         数据对象
     * @param dtoList     数据对象集合
     * @param subList     从数据对象集合
     * @param subDataRow  从数据关联数据类型
     * @param mergeType   间接关联类型
     * @param mainField   主数据待提取字段
     * @param slaveField  从数据待提取字段
     * @param subField    主数据待设值字段
     * @param subKeyField 主数据设值提取字段
     */
    private static <D extends BaseEntity, S extends BasisEntity, Coll extends Collection<D>, CollS extends Collection<S>> void setSubField(D dto, Coll dtoList, CollS subList, CorrelateConstants.DataRow subDataRow, CorrelateConstants.MergeType mergeType, Field mainField, Field slaveField, Field subField, Field subKeyField) {
        // 从数据集合为空 -> 无需组装
        if (CollUtil.isEmpty(subList)) {
            return;
        }
        switch (subDataRow) {
            case SINGLE -> {
                if (ObjectUtil.isNotNull(dto)) {
                    if (subList.size() == NumberUtil.One) {
                        S sub = subList.stream().findFirst().orElseThrow();
                        setField(dto, subField, ObjectUtil.isNotNull(subKeyField) ? getFieldObj(sub, subKeyField) : sub);
                    } else {
                        log.error("data error, Expected one result (or null) to be returned by selectOne(), but found: {}", subList.size());
                        throw new UtilException("数据错误，应查关联为单条，实际结果存在多条");
                    }
                }
                if (CollUtil.isNotEmpty(dtoList)) {
                    Map<Object, S> subMap = subList.stream().collect(Collectors.toMap(item -> getFieldObj(item, slaveField), Function.identity()));
                    dtoList.forEach(item -> {
                        S subInfo = getMapObj(subMap, getFieldObj(item, mainField));
                        setField(item, subField, ObjectUtil.isNotNull(subKeyField) ? getFieldObj(subInfo, subKeyField) : subInfo);
                    });
                }
            }
            case LIST -> {
                boolean isArray = ClassUtil.isArray(subField.getType());
                if (ObjectUtil.isNotNull(dto)) {
                    if (ObjectUtil.isNotNull(subKeyField)) {
                        List<Object> subKeyList = subList.stream().map(item -> getFieldObj(item, subKeyField)).filter(ObjectUtil::isNotNull).toList();
                        setField(dto, subField, isArray ? subKeyList.toArray() : subKeyList);
                    } else {
                        setField(dto, subField, isArray ? subList.toArray() : subList);
                    }
                }
                if (CollUtil.isNotEmpty(dtoList)) {
                    Map<Object, List<S>> subMap = subList.stream().collect(Collectors.groupingBy(item -> getFieldObj(item, slaveField)));
                    switch (mergeType) {
                        case DIRECT -> dtoList.forEach(item -> {
                            List<S> subInfoList = getMapObj(subMap, getFieldObj(item, mainField));
                            if (CollUtil.isNotEmpty(subInfoList)) {
                                if (ObjectUtil.isNotNull(subKeyField)) {
                                    List<Object> subKeyList = subInfoList.stream().map(subItem -> getFieldObj(subItem, subKeyField)).filter(ObjectUtil::isNotNull).toList();
                                    setField(item, subField, isArray ? subKeyList.toArray() : subKeyList);
                                } else {
                                    setField(item, subField, isArray ? subInfoList.toArray() : subInfoList);
                                }
                            }
                        });
                        case INDIRECT -> dtoList.forEach(item -> {
                            Object obj = getFieldSql(item, mainField);
                            Collection<?> coll = null;
                            if (obj instanceof Collection<?> list && CollUtil.isNotEmpty(list)) {
                                coll = list;
                            } else if (obj instanceof Object[] arr && ArrayUtil.isNotEmpty(arr)) {
                                coll = Arrays.stream(arr).toList();
                            }
                            if (CollUtil.isNotEmpty(coll)) {
                                List<?> subObjList = coll.stream().map(collItem -> {
                                    List<S> subInfoList = getMapObj(subMap, collItem);
                                    if (ObjectUtil.isNotNull(subKeyField) && CollUtil.isNotEmpty(subInfoList)) {
                                        return subInfoList.stream().map(subItem -> getFieldObj(subItem, subKeyField)).filter(ObjectUtil::isNotNull).toList();
                                    } else {
                                        return subInfoList;
                                    }
                                }).filter(ObjectUtil::isNotNull).flatMap(Collection::stream).filter(ObjectUtil::isNotNull).toList();
                                setField(item, subField, isArray ? subObjList.toArray() : subObjList);
                            }
                        });
                    }
                }
            }
        }
    }

    /**
     * 获取数据对象指定字段值
     *
     * @param map 数据Map
     * @param key 数据键值
     * @return 字段值
     */
    protected static <K, V> V getMapObj(Map<K, V> map, K key) {
        return ObjectUtil.isNotNull(key) ? map.get(key) : null;
    }

    /**
     * 获取数据对象指定字段值
     *
     * @param item  数据对象
     * @param field 字段
     * @return 字段值
     */
    protected static <T> Object getFieldObj(T item, Field field) {
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
    protected static <T> Object getFieldSql(T item, Field field) {
        try {
            return field.get(item);
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
    protected static void setField(Object item, Field field, Object obj) {
        try {
            field.set(item, ConvertUtil.convert(field.getType(), obj));
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
    protected static void setField(Object item, Field field, Collection<?> coll) {
        try {
            field.set(item, ConvertUtil.convert(field.getType(), coll));
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
    protected static <D> D createObj(Class<D> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 空判断/存空集合判空
     *
     * @param findInSet 集合
     * @return 结果
     */
    protected static Boolean isEmpty(Set<Object> findInSet) {
        if (CollUtil.isEmpty(findInSet)) {
            return Boolean.TRUE;
        }
        // 重新清洗一次数据
        Set<Object> distinctSet = findInSet.stream().filter(ObjectUtil::isNotNull).collect(Collectors.toSet());
        if (CollUtil.isEmpty(distinctSet)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 输出错误日志
     *
     * @param msg 日志信息
     */
    public static void logReturn(String msg) {
        throw new UtilException(msg);
    }
}
