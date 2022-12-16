package com.xueyi.common.web.utils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xueyi.common.core.annotation.Correlation;
import com.xueyi.common.core.annotation.Correlations;
import com.xueyi.common.core.constant.basic.OperateConstants.SubOperate;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.core.constant.error.UtilErrorConstants;
import com.xueyi.common.core.exception.UtilException;
import com.xueyi.common.core.utils.core.*;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.web.entity.base.BasisEntity;
import com.xueyi.common.web.entity.domain.SlaveRelation;
import com.xueyi.common.web.entity.domain.SqlField;

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
     * @param dto           数据对象
     * @param slaveRelation 从属关联关系定义对象
     * @param DClass        数据对象Class
     * @return 数据对象
     */
    public static <D> D subMerge(D dto, SlaveRelation slaveRelation, Class<D> DClass) {
        if (ObjectUtil.isNull(dto) || ObjectUtil.isNull(slaveRelation))
            return dto;
        initCorrelationField(slaveRelation, DClass, SubOperate.SELECT);
        switch (slaveRelation.getRelationType()) {
            case DIRECT -> assembleDirectObj(dto, slaveRelation);
            case INDIRECT -> assembleIndirectObj(dto, slaveRelation);
        }
        return dto;
    }

    /**
     * 子数据映射关联 | 查询（批量）
     *
     * @param dtoList       数据对象集合
     * @param slaveRelation 从属关联关系定义对象
     * @param DClass        数据对象Class
     * @return 数据对象集合
     */
    public static <D> List<D> subMerge(List<D> dtoList, SlaveRelation slaveRelation, Class<D> DClass) {
        if (CollUtil.isEmpty(dtoList) || ObjectUtil.isNull(slaveRelation))
            return dtoList;
        initCorrelationField(slaveRelation, DClass, SubOperate.SELECT);
        switch (slaveRelation.getRelationType()) {
            case DIRECT -> assembleDirectList(dtoList, slaveRelation);
            case INDIRECT -> assembleIndirectList(dtoList, slaveRelation);
        }
        return dtoList;
    }


    /**
     * 子数据映射关联 | 新增
     *
     * @param dto           数据对象
     * @param slaveRelation 从属关联关系定义对象
     * @param DClass        数据对象Class
     * @return 结果
     */
    public static <D> int addMerge(D dto, SlaveRelation slaveRelation, Class<D> DClass) {
        if (ObjectUtil.isNull(dto) || ObjectUtil.isNull(slaveRelation))
            return NumberUtil.Zero;
        initCorrelationField(slaveRelation, DClass, SubOperate.ADD);
        if (slaveRelation.getRelationType().isIndirect()) {
            return insertIndirectObj(dto, slaveRelation);
        }
        return NumberUtil.Zero;
    }

    /**
     * 子数据映射关联 | 新增（批量）
     *
     * @param dtoList       数据对象集合
     * @param slaveRelation 从属关联关系定义对象
     * @param DClass        数据对象Class
     * @return 结果
     */
    public static <D> int addMerge(Collection<D> dtoList, SlaveRelation slaveRelation, Class<D> DClass) {
        if (CollUtil.isEmpty(dtoList) || ObjectUtil.isNull(slaveRelation))
            return NumberUtil.Zero;
        initCorrelationField(slaveRelation, DClass, SubOperate.ADD);
        if (slaveRelation.getRelationType().isIndirect()) {
            return insertIndirectList(dtoList, slaveRelation);
        }
        return NumberUtil.Zero;
    }

    /**
     * 子数据映射关联 | 修改
     *
     * @param originDto     源数据对象
     * @param newDto        新数据对象
     * @param slaveRelation 从属关联关系定义对象
     * @param DClass        数据对象Class
     */
    public static <D> int editMerge(D originDto, D newDto, SlaveRelation slaveRelation, Class<D> DClass) {
        if (ObjectUtil.isAllEmpty(originDto, newDto) || ObjectUtil.isNull(slaveRelation))
            return NumberUtil.Zero;
        initCorrelationField(slaveRelation, DClass, SubOperate.EDIT);
        if (slaveRelation.getRelationType().isIndirect()) {
            return updateIndirectObj(originDto, newDto, slaveRelation);
        }
        return NumberUtil.Zero;
    }

    /**
     * 子数据映射关联 | 修改（批量）
     *
     * @param originList    源数据对象集合
     * @param newList       新数据对象集合
     * @param slaveRelation 从属关联关系定义对象
     * @param DClass        数据对象Class
     */
    public static <D> int editMerge(Collection<D> originList, Collection<D> newList, SlaveRelation slaveRelation, Class<D> DClass) {
        if ((CollUtil.isEmpty(originList) && CollUtil.isEmpty(newList)) || ObjectUtil.isNull(slaveRelation))
            return NumberUtil.Zero;
        initCorrelationField(slaveRelation, DClass, SubOperate.EDIT);
        if (slaveRelation.getRelationType().isIndirect()) {
            return updateIndirectList(originList, newList, slaveRelation);
        }
        return NumberUtil.Zero;
    }

    /**
     * 子数据映射关联 | 删除
     *
     * @param dto           数据对象
     * @param slaveRelation 从属关联关系定义对象
     * @param DClass        数据对象Class
     */
    public static <D> int delMerge(D dto, SlaveRelation slaveRelation, Class<D> DClass) {
        if (ObjectUtil.isNull(dto) || ObjectUtil.isNull(slaveRelation))
            return NumberUtil.Zero;
        initCorrelationField(slaveRelation, DClass, SubOperate.DELETE);
        if (slaveRelation.getRelationType().isIndirect()) {
            return deleteIndirectObj(dto, slaveRelation);
        }
        return NumberUtil.Zero;
    }

    /**
     * 子数据映射关联 | 删除（批量）
     *
     * @param dtoList       数据对象集合
     * @param slaveRelation 从属关联关系定义对象
     * @param DClass        数据对象Class
     */
    public static <D> int delMerge(Collection<D> dtoList, SlaveRelation slaveRelation, Class<D> DClass) {
        if (CollUtil.isEmpty(dtoList) || ObjectUtil.isNull(slaveRelation))
            return NumberUtil.Zero;
        initCorrelationField(slaveRelation, DClass, SubOperate.DELETE);
        if (slaveRelation.getRelationType().isIndirect()) {
            return deleteIndirectList(dtoList, slaveRelation);
        }
        return NumberUtil.Zero;
    }

    /**
     * 组装数据对象关联数据 | 直接关联
     *
     * @param dto           数据对象
     * @param slaveRelation 从属关联关系定义对象
     */
    private static <D> void assembleDirectObj(D dto, SlaveRelation slaveRelation) {
        try {
            Object value = slaveRelation.getMainField().get(dto);
            SqlField singleSqlField = new SqlField(SqlConstants.OperateType.EQ, slaveRelation.getSlaveFieldSqlName(),
                    value instanceof String ? StrUtil.SINGLE_QUOTES + value + StrUtil.SINGLE_QUOTES : value);
            // assemble dto receive key relation
            Class<?> fieldType = slaveRelation.getReceiveField().getType();
            if (ClassUtil.isNormalClass(fieldType)) {
                slaveRelation.getReceiveField().set(dto, SpringUtil.getBean(slaveRelation.getSlaveClass()).selectByField(singleSqlField));
            } else if (ClassUtil.isCollection(fieldType)) {
                slaveRelation.getReceiveField().set(dto, SpringUtil.getBean(slaveRelation.getSlaveClass()).selectListByField(singleSqlField));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 组装集合关联数据 | 直接关联
     *
     * @param dtoList       数据对象集合
     * @param slaveRelation 从属关联关系定义对象
     */
    private static <D> void assembleDirectList(List<D> dtoList, SlaveRelation slaveRelation) {
        Set<Object> findInSet = dtoList.stream().map(item -> getFieldSql(item, slaveRelation.getMainField())).collect(Collectors.toSet());
        if (CollUtil.isEmpty(findInSet))
            return;
        SqlField collSqlField = new SqlField(SqlConstants.OperateType.IN, slaveRelation.getSlaveFieldSqlName(), findInSet);
        List<?> subList = SpringUtil.getBean(slaveRelation.getSlaveClass()).selectListByField(collSqlField);
        if (CollUtil.isEmpty(subList))
            return;

        // assemble receive key relation
        Class<?> fieldType = slaveRelation.getReceiveField().getType();
        if (ClassUtil.isNormalClass(fieldType)) {
            Map<Object, Object> subMap = subList.stream().collect(
                    Collectors.toMap(item -> getFieldObj(item, slaveRelation.getSlaveField()), Function.identity()));
            dtoList.forEach(item -> {
                Object subObj = subMap.get(getFieldObj(item, slaveRelation.getMainField()));
                setField(item, slaveRelation.getReceiveField(), subObj);
            });
        } else if (ClassUtil.isCollection(fieldType)) {
            Map<Object, List<Object>> subMap = subList.stream().collect(
                    Collectors.groupingBy(item -> getFieldObj(item, slaveRelation.getSlaveField())));
            dtoList.forEach(item -> {
                List<Object> subObjList = subMap.get(getFieldObj(item, slaveRelation.getMainField()));
                setField(item, slaveRelation.getReceiveField(), subObjList);
            });
        }
    }

    /**
     * 组装数据对象关联数据 | 间接关联
     *
     * @param dto           数据对象
     * @param slaveRelation 从属关联关系定义对象
     */
    private static <D> void assembleIndirectObj(D dto, SlaveRelation slaveRelation) {
        try {
            Object value = slaveRelation.getMainField().get(dto);
            SqlField singleMergeSqlField = new SqlField(SqlConstants.OperateType.EQ, slaveRelation.getMergeMainFieldSqlName(),
                    value instanceof String ? StrUtil.SINGLE_QUOTES + value + StrUtil.SINGLE_QUOTES : value);
            List<?> mergeList = SpringUtil.getBean(slaveRelation.getMergeClass()).selectListByField(singleMergeSqlField);
            // if null return
            if (CollUtil.isEmpty(mergeList))
                return;
            // select merge relation list
            List<Object> mergeKeyList = mergeList.stream().map(item -> getFieldObj(item, slaveRelation.getMergeSlaveField()))
                    .collect(Collectors.toList());
            // assemble merge arr
            if (ObjectUtil.isNotNull(slaveRelation.getReceiveArrField())) {
                Class<?> fieldType = slaveRelation.getReceiveArrField().getType();
                if (ClassUtil.isArray(fieldType)) {
                    slaveRelation.getReceiveArrField().set(dto, mergeKeyList.toArray());
                } else if (ClassUtil.isCollection(fieldType)) {
                    slaveRelation.getReceiveArrField().set(dto, mergeKeyList);
                }
            }
            if (ObjectUtil.isNull(slaveRelation.getReceiveField()))
                return;
            Set<Object> findInSet = mergeKeyList.stream().map(item ->
                    item instanceof String ? StrUtil.SINGLE_QUOTES + item + StrUtil.SINGLE_QUOTES : item).collect(Collectors.toSet());
            SqlField singleSubSqlField = new SqlField(SqlConstants.OperateType.IN, slaveRelation.getSlaveFieldSqlName(), findInSet);
            List<?> subList = SpringUtil.getBean(slaveRelation.getSlaveClass()).selectListByField(singleSubSqlField);
            if (CollUtil.isEmpty(subList))
                return;
            slaveRelation.getReceiveField().set(dto, subList);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 组装集合关联数据 | 间接关联
     *
     * @param dtoList       数据对象集合
     * @param slaveRelation 从属关联关系定义对象
     */
    private static <D> void assembleIndirectList(List<D> dtoList, SlaveRelation slaveRelation) {
        Set<Object> findInSet = dtoList.stream().map(item -> getFieldSql(item, slaveRelation.getMainField())).collect(Collectors.toSet());
        if (CollUtil.isEmpty(findInSet))
            return;
        SqlField collMergeSqlField = new SqlField(SqlConstants.OperateType.IN, slaveRelation.getMergeMainFieldSqlName(), findInSet);
        List<?> mergeList = SpringUtil.getBean(slaveRelation.getMergeClass()).selectListByField(collMergeSqlField);
        if (CollUtil.isEmpty(mergeList))
            return;

        // build merge map
        Map<Object, List<Object>> mergeMap = mergeList.stream().collect(
                Collectors.groupingBy(item -> getFieldObj(item, slaveRelation.getMergeMainField())
                        , Collectors.mapping(item -> getFieldObj(item, slaveRelation.getMergeSlaveField()), Collectors.toList())));
        // assemble merge arr
        if (ObjectUtil.isNotNull(slaveRelation.getReceiveArrField())) {
            Class<?> fieldType = slaveRelation.getReceiveArrField().getType();
            dtoList.forEach(item -> {
                List<Object> mergeRelationList = mergeMap.get(getFieldObj(item, slaveRelation.getMainField()));
                if (CollUtil.isNotEmpty(mergeRelationList)) {
                    if (ClassUtil.isArray(fieldType)) {
                        setField(item, slaveRelation.getReceiveArrField(), mergeRelationList.toArray());
                    } else if (ClassUtil.isCollection(fieldType)) {
                        setField(item, slaveRelation.getReceiveArrField(), mergeRelationList);
                    }
                }
            });
        }
        // if receiveKey is null
        if (ObjectUtil.isNull(slaveRelation.getReceiveField()))
            return;

        // select sub relation list
        List<Object> subFindInSet = mergeList.stream().map(item -> getFieldSql(item, slaveRelation.getMergeSlaveField())).collect(Collectors.toList());
        SqlField collSqlField = new SqlField(SqlConstants.OperateType.IN, slaveRelation.getSlaveFieldSqlName(), subFindInSet);
        List<?> mergeSubList = SpringUtil.getBean(slaveRelation.getSlaveClass()).selectListByField(collSqlField);
        if (CollUtil.isEmpty(mergeSubList))
            return;
        Map<Object, Object> subMap = mergeSubList.stream().collect(
                Collectors.toMap(item -> getFieldObj(item, slaveRelation.getSlaveField()), Function.identity()));
        // assemble receive key relation
        dtoList.forEach(item -> {
            List<Object> mergeRelationList = mergeMap.get(getFieldObj(item, slaveRelation.getMainField()));
            if (CollUtil.isEmpty(mergeRelationList))
                return;
            List<Object> subList = new ArrayList<>();
            mergeRelationList.forEach(mergeItem -> {
                Object sub = subMap.get(mergeItem);
                if (ObjectUtil.isNotNull(sub))
                    subList.add(sub);
            });
            if (CollUtil.isNotEmpty(subList))
                setField(item, slaveRelation.getReceiveField(), subList);
        });
    }

    /**
     * 新增关联数据 | 间接关联
     *
     * @param dto           数据对象
     * @param slaveRelation 从属关联关系定义对象
     */
    private static <D, MP extends BasisEntity> int insertIndirectObj(D dto, SlaveRelation slaveRelation) {
        Collection<MP> mergeList = insertIndirectBuild(dto, slaveRelation);
        if (CollUtil.isEmpty(mergeList))
            return NumberUtil.Zero;
        return SpringUtil.getBean(slaveRelation.getMergeClass()).insertByField(mergeList, slaveRelation.getMergePoClass());
    }

    /**
     * 新增关联数据 | 间接关联
     *
     * @param dtoList       数据对象集合
     * @param slaveRelation 从属关联关系定义对象
     */
    private static <D, MP extends BasisEntity> int insertIndirectList(Collection<D> dtoList, SlaveRelation slaveRelation) {
        List<MP> list = new ArrayList<>();
        for (D dto : dtoList) {
            List<MP> mergeList = insertIndirectBuild(dto, slaveRelation);
            if (CollUtil.isNotEmpty(mergeList)) {
                list.addAll(mergeList);
            }
        }
        if (CollUtil.isEmpty(list))
            return NumberUtil.Zero;
        return SpringUtil.getBean(slaveRelation.getMergeClass()).insertByField(list, slaveRelation.getMergePoClass());
    }

    /**
     * 新增关联数据 | 间接关联
     *
     * @param originDto     源数据对象
     * @param newDto        新数据对象
     * @param slaveRelation 从属关联关系定义对象
     */
    private static <D, MP extends BasisEntity> int updateIndirectObj(D originDto, D newDto, SlaveRelation slaveRelation) {
        return 0;
    }

    /**
     * 新增关联数据 | 间接关联
     *
     * @param originList    源数据对象集合
     * @param newList       新数据对象集合
     * @param slaveRelation 从属关联关系定义对象
     */
    private static <D, MP extends BasisEntity> int updateIndirectList(Collection<D> originList, Collection<D> newList, SlaveRelation slaveRelation) {
        return 0;
    }

    /**
     * 新增关联数据 | 间接关联
     *
     * @param dto           数据对象
     * @param slaveRelation 从属关联关系定义对象
     */
    private static <D> int deleteIndirectObj(D dto, SlaveRelation slaveRelation) {
        Object delKey = getFieldObj(dto, slaveRelation.getMainField());
        SqlField sqlField = new SqlField(SqlConstants.OperateType.EQ, slaveRelation.getMergeMainFieldSqlName(), delKey);
        return SpringUtil.getBean(slaveRelation.getMergeClass()).deleteByField(sqlField);
    }

    /**
     * 新增关联数据 | 间接关联
     *
     * @param dtoList       数据对象集合
     * @param slaveRelation 从属关联关系定义对象
     */
    private static <D> int deleteIndirectList(Collection<D> dtoList, SlaveRelation slaveRelation) {
        Set<Object> delKeys = dtoList.stream().map(item -> getFieldObj(item, slaveRelation.getMainField())).collect(Collectors.toSet());
        SqlField sqlField = new SqlField(SqlConstants.OperateType.IN, slaveRelation.getMergeMainFieldSqlName(), delKeys);
        return SpringUtil.getBean(slaveRelation.getMergeClass()).deleteByField(sqlField);
    }

    /**
     * 新增关联数据 | 数据组装 | 间接关联
     *
     * @param dto           数据对象
     * @param slaveRelation 从属关联关系定义对象
     * @return 数据对象集合
     */
    @SuppressWarnings("unchecked")
    private static <D, MP extends BasisEntity> List<MP> insertIndirectBuild(D dto, SlaveRelation slaveRelation) {
        Object mainKey = getFieldObj(dto, slaveRelation.getMainField());
        Object mergeObj = getFieldObj(dto, slaveRelation.getMergeSlaveField());
        Class<?> fieldType = slaveRelation.getMergeSlaveField().getType();
        List<MP> mergeList = new ArrayList<>();
        if (ClassUtil.isCollection(fieldType)) {
            Collection<Object> objColl = (Collection<Object>) mergeObj;
            mergeList = objColl.stream().distinct().map(item -> {
                Object mergePo = createObj(slaveRelation.getMergePoClass());
                setField(mergePo, slaveRelation.getMergeMainField(), mainKey);
                setField(mergePo, slaveRelation.getMergeSlaveField(), item);
                return (MP) mergePo;
            }).collect(Collectors.toList());
        } else if (ClassUtil.isArray(fieldType)) {
            Object[] objArr = (Object[]) mergeObj;
            mergeList = Arrays.stream(objArr).distinct().map(item -> {
                Object mergePo = createObj(slaveRelation.getMergePoClass());
                setField(mergePo, slaveRelation.getMergeMainField(), mainKey);
                setField(mergePo, slaveRelation.getMergeSlaveField(), item);
                return (MP) mergePo;
            }).collect(Collectors.toList());
        }
        return mergeList;
    }

    /**
     * 初始化数据字段关系 | 初始化校验
     *
     * @param slaveRelation 从属关联关系定义对象
     * @param mainDClass    数据对象Class
     */
    private static <D> void initCorrelationField(SlaveRelation slaveRelation, Class<D> mainDClass, SubOperate operate) {
        // 1.校验数据字段是否合规
        if (checkOperateLegal(slaveRelation, operate))
            return;
        // 2.初始化主数据对象映射关系
        initMainCorrelation(slaveRelation, mainDClass);
        // 3.初始化子数据对象映射关系
        initSlaveCorrelation(slaveRelation);
        // 4.初始化中间数据对象映射关系
        initMergeCorrelation(slaveRelation);
        // 5.二次校验 - 如果依旧未通过，则代表人为配置出错，检查代码
        if(!checkOperateLegal(slaveRelation, operate))
            throw new UtilException(StrUtil.format(UtilErrorConstants.MergeError.CORRELATION_ERROR.getInfo(), slaveRelation.getGroupName()));
    }

    /**
     * 校验数据字段是否合规 | 初始化校验
     *
     * @param relation 从属关联关系定义对象
     * @param operate  操作类型
     */
    private static boolean checkOperateLegal(SlaveRelation relation, SubOperate operate) {
        // 主数据的主键 及 子数据class 不能为null
        if (ObjectUtil.hasNull(relation.getMainField(), relation.getSlaveClass()))
            return Boolean.FALSE;
        switch (relation.getRelationType()) {
            case DIRECT -> {
                switch (operate) {
                    case SELECT -> {
                        return ObjectUtil.hasNull(relation.getSlaveField(), relation.getSlaveFieldSqlName(), relation.getReceiveField());
                    }
                    case ADD, EDIT, DELETE -> {

                    }
                }
            }
            case INDIRECT -> {
                // 关联数据class、主映射键、主映射键字段名 不能为null
                if (ObjectUtil.hasNull(relation.getMergeClass(), relation.getMergeMainField(), relation.getMergeMainFieldSqlName()))
                    return Boolean.FALSE;
                switch (operate) {
                    case SELECT -> {
                        return ObjectUtil.hasNull(relation.getMergeSlaveField(), relation.getSlaveField(),
                                relation.getSlaveFieldSqlName(), relation.getReceiveField());
                    }
                    case ADD -> {
                        return ObjectUtil.hasNull(relation.getMergePoClass(), relation.getMergeSlaveField(),
                                relation.getSlaveFieldSqlName());
                    }
                    case DELETE -> {
                    }
                }
            }
        }
        return Boolean.FALSE;
    }

    /**
     * 初始化主数据对象映射关系 | 初始化校验
     *
     * @param slaveRelation 从属关联关系定义对象
     * @param mainDClass    主数据对象Class
     */
    private static <D> void initMainCorrelation(SlaveRelation slaveRelation, Class<D> mainDClass) {
        // 1.校验主数据对象Class是否存在
        if (ObjectUtil.isNull(mainDClass))
            return;
        Field[] fields = ReflectUtil.getFields(mainDClass);
        for (Field field : fields) {
            // 单注解模式
            if (field.isAnnotationPresent(Correlation.class)) {
                Correlation correlation = field.getAnnotation(Correlation.class);
                initMainCorrelation(slaveRelation, field, correlation);
            }
            // 多注解模式
            if (field.isAnnotationPresent(Correlations.class)) {
                Correlations correlations = field.getAnnotation(Correlations.class);
                Correlation[] correlationArr = correlations.value();
                if (ArrayUtil.isNotEmpty(correlations.value())) {
                    for (Correlation correlation : correlationArr) {
                        initMainCorrelation(slaveRelation, field, correlation);
                    }
                }
            }
        }

        // 2.如果主数据主键未定义 -> 取主数据表主键
        if (ObjectUtil.isNull(slaveRelation.getMainField())) {
            Field idField = Arrays.stream(fields).filter(field -> field.getAnnotation(TableId.class) != null).findFirst().orElse(null);
            if (ObjectUtil.isNotNull(idField)) {
                slaveRelation.setMainField(idField);
                slaveRelation.getMainField().setAccessible(Boolean.TRUE);
            }
        }
    }

    /**
     * 初始化主数据对象映射关系 | 主数据初始化
     *
     * @param slaveRelation 从属关联关系定义对象
     * @param field         属性信息对象
     * @param correlation   关联关系定义注解
     */
    private static void initMainCorrelation(SlaveRelation slaveRelation, Field field, Correlation correlation) {
        if (ObjectUtil.isNull(correlation) || StrUtil.notEquals(correlation.groupName(), slaveRelation.getGroupName()))
            return;
        switch (correlation.keyType()) {
            case MAIN -> {
                if (ObjectUtil.isNull(slaveRelation.getMainField())) {
                    slaveRelation.setMainField(field);
                    slaveRelation.getMainField().setAccessible(Boolean.TRUE);
                }
            }
            case RECEIVE -> {
                if (ObjectUtil.isNull(slaveRelation.getReceiveField())) {
                    // 直接关联：校验是否为集合 or 数组
                    Class<?> fieldType = field.getType();
                    switch (slaveRelation.getRelationType()) {
                        case DIRECT -> {
                            if (ClassUtil.isNotNormalClass(fieldType) && ClassUtil.isNotCollection(fieldType))
                                throw new UtilException(StrUtil.format(UtilErrorConstants.MergeError.RECEIVE_DIRECT_TYPE_ERROR.getInfo(), slaveRelation.getGroupName()));
                        }
                        case INDIRECT -> {
                            if (ClassUtil.isNotCollection(fieldType))
                                throw new UtilException(StrUtil.format(UtilErrorConstants.MergeError.RECEIVE_INDIRECT_TYPE_ERROR.getInfo(), slaveRelation.getGroupName()));
                        }
                    }
                    slaveRelation.setReceiveField(field);
                    slaveRelation.getReceiveField().setAccessible(Boolean.TRUE);
                }
            }
            case RECEIVE_ARR -> {
                if (slaveRelation.getRelationType().isIndirect() && ObjectUtil.isNull(slaveRelation.getReceiveArrField())) {
                    // 校验是否为集合 or 数组
                    Class<?> fieldType = field.getType();
                    if (ClassUtil.isNotArray(fieldType) && ClassUtil.isNotCollection(fieldType))
                        throw new UtilException(StrUtil.format(UtilErrorConstants.MergeError.RECEIVE_ARR_TYPE_ERROR.getInfo(), slaveRelation.getGroupName()));
                    slaveRelation.setReceiveArrField(field);
                    slaveRelation.getReceiveArrField().setAccessible(Boolean.TRUE);
                }
            }
        }
    }

    /**
     * 初始化从数据对象映射关系 | 初始化校验
     *
     * @param slaveRelation 从属关联关系定义对象
     */
    private static void initSlaveCorrelation(SlaveRelation slaveRelation) {
        // 1.校验从数据方法类class是否为null
        if (ObjectUtil.isNull(slaveRelation.getSlaveClass()))
            return;
        Class<? extends BaseEntity> slavePClass = SpringUtil.getBean(slaveRelation.getSlaveClass()).getPClass();
        // 2.校验从数据对象class是否为null
        if (ObjectUtil.isNull(slavePClass))
            return;
        Field[] fields = ReflectUtil.getFields(slavePClass);
        for (Field field : fields) {
            // 单注解模式
            if (field.isAnnotationPresent(Correlation.class)) {
                Correlation correlation = field.getAnnotation(Correlation.class);
                initSlaveCorrelation(slaveRelation, field, correlation);
            }
            // 多注解模式
            if (field.isAnnotationPresent(Correlations.class)) {
                Correlations correlations = field.getAnnotation(Correlations.class);
                Correlation[] correlationArr = correlations.value();
                if (ArrayUtil.isNotEmpty(correlations.value())) {
                    for (Correlation correlation : correlationArr) {
                        initSlaveCorrelation(slaveRelation, field, correlation);
                    }
                }
            }
        }

        // 3.如果从数据主键未定义 且为间接关联时 -> 取从数据表主键
        if (slaveRelation.getRelationType().isIndirect() && ObjectUtil.isNull(slaveRelation.getSlaveField())) {
            Field idField = Arrays.stream(fields).filter(field -> field.isAnnotationPresent(TableId.class)).findFirst().orElse(null);
            if (ObjectUtil.isNotNull(idField)) {
                slaveRelation.setSlaveField(idField);
            }
        }

        // 4.初始化从数据主键数据库字段名
        if (ObjectUtil.isNotNull(slaveRelation.getSlaveField())) {
            Field field = slaveRelation.getSlaveField();
            String slaveFieldSqlName;
            if (field.isAnnotationPresent(TableId.class)) {
                TableId idField = field.getAnnotation(TableId.class);
                slaveFieldSqlName = ObjectUtil.isNotNull(idField) && StrUtil.isNotEmpty(idField.value())
                        ? idField.value()
                        : StrUtil.toUnderlineCase(slaveRelation.getSlaveField().getName());
            } else if (field.isAnnotationPresent(TableField.class)) {
                TableField tableField = field.getAnnotation(TableField.class);
                slaveFieldSqlName = StrUtil.isNotEmpty(tableField.value())
                        ? tableField.value()
                        : StrUtil.toUnderlineCase(slaveRelation.getSlaveField().getName());
            } else {
                slaveFieldSqlName = StrUtil.toUnderlineCase(slaveRelation.getSlaveField().getName());
            }
            slaveRelation.getSlaveField().setAccessible(Boolean.TRUE);
            slaveRelation.setSlaveFieldSqlName(slaveFieldSqlName);
        }
    }

    /**
     * 初始化从数据对象映射关系 | 从数据初始化
     *
     * @param slaveRelation 从属关联关系定义对象
     * @param field         属性信息对象
     * @param correlation   关联关系定义注解
     */
    private static void initSlaveCorrelation(SlaveRelation slaveRelation, Field field, Correlation correlation) {
        if (ObjectUtil.isNull(correlation) || StrUtil.notEquals(correlation.groupName(), slaveRelation.getGroupName()))
            return;
        if (correlation.keyType().isSlaveKey()) {
            if (ObjectUtil.isNull(slaveRelation.getSlaveField())) {
                slaveRelation.setSlaveField(field);
                slaveRelation.getSlaveField().setAccessible(Boolean.TRUE);
            }
        }
    }

    /**
     * 初始化中间数据对象映射关系 | 初始化校验
     *
     * @param slaveRelation 从属关联关系定义对象
     */
    private static void initMergeCorrelation(SlaveRelation slaveRelation) {
        // 1.校验中间数据方法类class是否为null
        if (ObjectUtil.isNull(slaveRelation.getMergeClass()))
            return;
        Class<? extends BasisEntity> mergePoClass = TypeUtil.getClazz(slaveRelation.getMergeClass().getGenericSuperclass(), NumberUtil.Zero);
        // 2.校验中间类数据对象class是否为null
        if (ObjectUtil.isNull(mergePoClass))
            return;
        slaveRelation.setMergePoClass(mergePoClass);
        Field[] fields = ReflectUtil.getFields(mergePoClass);
        for (Field field : fields) {
            // 单注解模式
            if (field.isAnnotationPresent(Correlation.class)) {
                Correlation correlation = field.getAnnotation(Correlation.class);
                initMergeCorrelation(slaveRelation, field, correlation);
            }
            // 多注解模式
            if (field.isAnnotationPresent(Correlations.class)) {
                Correlations correlations = field.getAnnotation(Correlations.class);
                Correlation[] correlationArr = correlations.value();
                if (ArrayUtil.isNotEmpty(correlations.value())) {
                    for (Correlation correlation : correlationArr) {
                        initMergeCorrelation(slaveRelation, field, correlation);
                    }
                }
            }
        }
    }

    /**
     * 初始化中间数据对象映射关系 | 中间数据初始化
     *
     * @param slaveRelation 从属关联关系定义对象
     * @param field         属性信息对象
     * @param correlation   关联关系定义注解
     */
    private static void initMergeCorrelation(SlaveRelation slaveRelation, Field field, Correlation correlation) {
        if (ObjectUtil.isNull(correlation) || StrUtil.notEquals(correlation.groupName(), slaveRelation.getGroupName()))
            return;
        switch (correlation.keyType()) {
            case MERGE_MAIN -> {
                if (ObjectUtil.isNull(slaveRelation.getMergeMainField())) {
                    slaveRelation.setMergeMainField(field);
                    slaveRelation.getMergeSlaveField().setAccessible(Boolean.TRUE);
                    TableField tableField = slaveRelation.getMergeMainField().getAnnotation(TableField.class);
                    String fieldName = ObjectUtil.isNotNull(tableField) && StrUtil.isNotEmpty(tableField.value())
                            ? tableField.value()
                            : StrUtil.toUnderlineCase(slaveRelation.getMergeMainField().getName());
                    slaveRelation.setMergeMainFieldSqlName(fieldName);
                }
            }
            case MERGE_SLAVE -> {
                if (ObjectUtil.isNull(slaveRelation.getMergeSlaveField())) {
                    slaveRelation.setMergeSlaveField(field);
                    slaveRelation.getMergeSlaveField().setAccessible(Boolean.TRUE);
                    TableField tableField = slaveRelation.getMergeSlaveField().getAnnotation(TableField.class);
                    String fieldName = ObjectUtil.isNotNull(tableField) && StrUtil.isNotEmpty(tableField.value())
                            ? tableField.value()
                            : StrUtil.toUnderlineCase(slaveRelation.getMergeSlaveField().getName());
                    slaveRelation.setMergeSlaveFieldSqlName(fieldName);
                }
            }
        }
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