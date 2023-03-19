package com.xueyi.common.web.utils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xueyi.common.core.annotation.Correlation;
import com.xueyi.common.core.annotation.Correlations;
import com.xueyi.common.core.constant.basic.OperateConstants;
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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 主从关联工具类
 *
 * @author xueyi
 */
@SuppressWarnings("unchecked")
public class MergeUtil {

    /**
     * 子数据映射关联 | 查询
     *
     * @param dto           数据对象
     * @param slaveRelation 从属关联关系定义对象
     * @return 数据对象
     */
    public static <D> D subMerge(D dto, SlaveRelation slaveRelation) {
        if (ObjectUtil.isNull(dto) || ObjectUtil.isNull(slaveRelation))
            return dto;
        initCorrelationField(slaveRelation, SubOperate.SELECT);
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
     * @return 数据对象集合
     */
    public static <D> List<D> subMerge(List<D> dtoList, SlaveRelation slaveRelation) {
        if (CollUtil.isEmpty(dtoList) || ObjectUtil.isNull(slaveRelation))
            return dtoList;
        initCorrelationField(slaveRelation, SubOperate.SELECT);
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
     * @return 结果
     */
    public static <D> int addMerge(D dto, SlaveRelation slaveRelation) {
        if (ObjectUtil.isNull(dto) || ObjectUtil.isNull(slaveRelation))
            return NumberUtil.Zero;
        initCorrelationField(slaveRelation, SubOperate.ADD);
        return switch (slaveRelation.getRelationType()) {
            case DIRECT -> insertDirectObj(dto, slaveRelation);
            case INDIRECT -> insertIndirectObj(dto, slaveRelation);
        };
    }

    /**
     * 子数据映射关联 | 新增（批量）
     *
     * @param dtoList       数据对象集合
     * @param slaveRelation 从属关联关系定义对象
     * @return 结果
     */
    public static <D> int addMerge(Collection<D> dtoList, SlaveRelation slaveRelation) {
        if (CollUtil.isEmpty(dtoList) || ObjectUtil.isNull(slaveRelation))
            return NumberUtil.Zero;
        initCorrelationField(slaveRelation, SubOperate.ADD);
        return switch (slaveRelation.getRelationType()) {
            case DIRECT -> insertDirectList(dtoList, slaveRelation);
            case INDIRECT -> insertIndirectList(dtoList, slaveRelation);
        };
    }

    /**
     * 子数据映射关联 | 修改
     *
     * @param originDto     源数据对象
     * @param newDto        新数据对象
     * @param slaveRelation 从属关联关系定义对象
     */
    public static <D> int editMerge(D originDto, D newDto, SlaveRelation slaveRelation) {
        if (ObjectUtil.isAllEmpty(originDto, newDto) || ObjectUtil.isNull(slaveRelation))
            return NumberUtil.Zero;
        initCorrelationField(slaveRelation, SubOperate.EDIT);
        return switch (slaveRelation.getRelationType()) {
            case DIRECT -> updateDirectObj(originDto, newDto, slaveRelation);
            case INDIRECT -> updateIndirectObj(originDto, newDto, slaveRelation);
        };
    }

    /**
     * 子数据映射关联 | 修改（批量）
     *
     * @param originList    源数据对象集合
     * @param newList       新数据对象集合
     * @param slaveRelation 从属关联关系定义对象
     */
    public static <D> int editMerge(Collection<D> originList, Collection<D> newList, SlaveRelation slaveRelation) {
        if ((CollUtil.isEmpty(originList) && CollUtil.isEmpty(newList)) || ObjectUtil.isNull(slaveRelation))
            return NumberUtil.Zero;
        initCorrelationField(slaveRelation, SubOperate.EDIT);
        return switch (slaveRelation.getRelationType()) {
            case DIRECT -> updateDirectList(originList, newList, slaveRelation);
            case INDIRECT -> updateIndirectList(originList, newList, slaveRelation);
        };
    }

    /**
     * 子数据映射关联 | 删除
     *
     * @param dto           数据对象
     * @param slaveRelation 从属关联关系定义对象
     */
    public static <D> int delMerge(D dto, SlaveRelation slaveRelation) {
        if (ObjectUtil.isNull(dto) || ObjectUtil.isNull(slaveRelation))
            return NumberUtil.Zero;
        initCorrelationField(slaveRelation, SubOperate.DELETE);
        return switch (slaveRelation.getRelationType()) {
            case DIRECT -> deleteDirectObj(dto, slaveRelation);
            case INDIRECT -> deleteIndirectObj(dto, slaveRelation);
        };
    }

    /**
     * 子数据映射关联 | 删除（批量）
     *
     * @param dtoList       数据对象集合
     * @param slaveRelation 从属关联关系定义对象
     */
    public static <D> int delMerge(Collection<D> dtoList, SlaveRelation slaveRelation) {
        if (CollUtil.isEmpty(dtoList) || ObjectUtil.isNull(slaveRelation))
            return NumberUtil.Zero;
        initCorrelationField(slaveRelation, SubOperate.DELETE);
        return switch (slaveRelation.getRelationType()) {
            case DIRECT -> deleteDirectList(dtoList, slaveRelation);
            case INDIRECT -> deleteIndirectList(dtoList, slaveRelation);
        };
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
                    value instanceof String ? StrUtil.SINGLE_QUOTES + value + StrUtil.SINGLE_QUOTES : value, slaveRelation.getLinkageOperate());
            // assemble dto receive key relation
            Class<?> fieldType = slaveRelation.getReceiveField().getType();
            if (ClassUtil.isNormalClass(fieldType)) {
                slaveRelation.getReceiveField().set(dto, SpringUtil.getBean(slaveRelation.getSlaveManagerClass()).selectByField(singleSqlField));
            } else if (ClassUtil.isCollection(fieldType)) {
                slaveRelation.getReceiveField().set(dto, SpringUtil.getBean(slaveRelation.getSlaveManagerClass()).selectListByField(singleSqlField));
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
        SqlField collSqlField = new SqlField(SqlConstants.OperateType.IN, slaveRelation.getSlaveFieldSqlName(), findInSet, slaveRelation.getLinkageOperate());
        List<?> subList = SpringUtil.getBean(slaveRelation.getSlaveManagerClass()).selectListByField(collSqlField);
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
                    value instanceof String ? StrUtil.SINGLE_QUOTES + value + StrUtil.SINGLE_QUOTES : value, slaveRelation.getLinkageOperate());
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
                    setField(dto, slaveRelation.getReceiveArrField(), mergeKeyList.toArray());
                } else if (ClassUtil.isCollection(fieldType)) {
                    setField(dto, slaveRelation.getReceiveArrField(), mergeKeyList);
                }
            }
            if (ObjectUtil.isNull(slaveRelation.getReceiveField()))
                return;
            Set<Object> findInSet = mergeKeyList.stream().map(item ->
                    item instanceof String ? StrUtil.SINGLE_QUOTES + item + StrUtil.SINGLE_QUOTES : item).collect(Collectors.toSet());
            SqlField singleSubSqlField = new SqlField(SqlConstants.OperateType.IN, slaveRelation.getSlaveFieldSqlName(), findInSet, slaveRelation.getLinkageOperate());
            List<?> subList = SpringUtil.getBean(slaveRelation.getSlaveManagerClass()).selectListByField(singleSubSqlField);
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
        SqlField collMergeSqlField = new SqlField(SqlConstants.OperateType.IN, slaveRelation.getMergeMainFieldSqlName(), findInSet, slaveRelation.getLinkageOperate());
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
        SqlField collSqlField = new SqlField(SqlConstants.OperateType.IN, slaveRelation.getSlaveFieldSqlName(), subFindInSet, slaveRelation.getLinkageOperate());
        List<?> mergeSubList = SpringUtil.getBean(slaveRelation.getSlaveManagerClass()).selectListByField(collSqlField);
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
     * 新增关联数据 | 直接关联
     *
     * @param dto           数据对象
     * @param slaveRelation 从属关联关系定义对象
     */
    private static <D, SD extends BaseEntity> int insertDirectObj(D dto, SlaveRelation slaveRelation) {
        Collection<SD> subList = insertDirectBuild(dto, slaveRelation);
        if (CollUtil.isEmpty(subList))
            return NumberUtil.Zero;
        return slaveRelation.getLinkageOperate().getIsAdd()
                ? SpringUtil.getBean(slaveRelation.getSlaveServiceClass()).insertBatch(subList)
                : SpringUtil.getBean(slaveRelation.getSlaveManagerClass()).insertBatch(subList);
    }

    /**
     * 新增关联数据 | 直接关联
     *
     * @param dtoList       数据对象集合
     * @param slaveRelation 从属关联关系定义对象
     */
    private static <D, SD extends BaseEntity> int insertDirectList(Collection<D> dtoList, SlaveRelation slaveRelation) {
        List<SD> list = new ArrayList<>();
        for (D dto : dtoList) {
            List<SD> subList = insertDirectBuild(dto, slaveRelation);
            if (CollUtil.isNotEmpty(subList)) {
                list.addAll(subList);
            }
        }
        if (CollUtil.isEmpty(list))
            return NumberUtil.Zero;
        return slaveRelation.getLinkageOperate().getIsAdd()
                ? SpringUtil.getBean(slaveRelation.getSlaveServiceClass()).insertBatch(list)
                : SpringUtil.getBean(slaveRelation.getSlaveManagerClass()).insertBatch(list);
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
        return SpringUtil.getBean(slaveRelation.getMergeClass()).insertBatch(mergeList);
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
        return SpringUtil.getBean(slaveRelation.getMergeClass()).insertBatch(list);
    }

    /**
     * 新增关联数据 | 直接关联
     *
     * @param originDto     源数据对象
     * @param newDto        新数据对象
     * @param slaveRelation 从属关联关系定义对象
     */
    private static <D, SD extends BaseEntity> int updateDirectObj(D originDto, D newDto, SlaveRelation slaveRelation) {
        List<SD> insertList = new ArrayList<>();
        List<SD> updateList = new ArrayList<>();
        Set<Object> delKeys = new HashSet<>();
        // 1.组装操作数据
        updateDirectBuild(originDto, newDto, slaveRelation, insertList, updateList, delKeys);
        int rows = NumberUtil.Zero;
        // 2.判断是否执行新增
        if (CollUtil.isNotEmpty(insertList))
            rows += slaveRelation.getLinkageOperate().getIsAdd()
                    ? SpringUtil.getBean(slaveRelation.getSlaveServiceClass()).insertBatch(insertList)
                    : SpringUtil.getBean(slaveRelation.getSlaveManagerClass()).insertBatch(insertList);
        // 3.判断是否执行修改
        if (CollUtil.isNotEmpty(updateList))
            rows += slaveRelation.getLinkageOperate().getIsEdit()
                    ? SpringUtil.getBean(slaveRelation.getSlaveServiceClass()).updateBatch(updateList)
                    : SpringUtil.getBean(slaveRelation.getSlaveManagerClass()).updateBatch(updateList);
        // 4.判断是否执行删除
        if (CollUtil.isNotEmpty(delKeys)) {
            if (slaveRelation.getLinkageOperate().getIsDelete()) {
                rows += SpringUtil.getBean(slaveRelation.getSlaveServiceClass()).deleteByIds(delKeys);
            } else {
                Object delMainKey = getFieldObj(newDto, slaveRelation.getMainField());
                SqlField sqlMainField = new SqlField(SqlConstants.OperateType.EQ, slaveRelation.getSlaveFieldSqlName(), delMainKey, slaveRelation.getLinkageOperate());
                SqlField sqlArrField = new SqlField(SqlConstants.OperateType.IN, slaveRelation.getSlaveIdFieldSqlName(), delKeys, slaveRelation.getLinkageOperate());
                rows += SpringUtil.getBean(slaveRelation.getSlaveManagerClass()).deleteByField(sqlMainField, sqlArrField);
            }
        }
        // 5.返回操作结果
        return rows;
    }

    /**
     * 新增关联数据 | 直接关联
     *
     * @param originList    源数据对象集合
     * @param newList       新数据对象集合
     * @param slaveRelation 从属关联关系定义对象
     */
    private static <D, SD extends BaseEntity> int updateDirectList(Collection<D> originList, Collection<D> newList, SlaveRelation slaveRelation) {
        List<SD> insertList = new ArrayList<>();
        List<SD> updateList = new ArrayList<>();
        Set<Object> delKeys = new HashSet<>();
        // 1.组装操作数据
        Map<Object, D> originMap = CollUtil.isEmpty(originList)
                ? new HashMap<>()
                : originList.stream().collect(
                Collectors.toMap(item -> getFieldObj(item, slaveRelation.getMainIdField()), Function.identity()));

        if (CollUtil.isNotEmpty(newList)) {
            newList.forEach(newDto -> {
                D originDto = originMap.get(getFieldObj(newDto, slaveRelation.getMainIdField()));
                updateDirectBuild(originDto, newDto, slaveRelation, insertList, updateList, delKeys);
            });
        }
        int rows = NumberUtil.Zero;

        // 2.判断是否执行新增
        if (CollUtil.isNotEmpty(insertList))
            rows += slaveRelation.getLinkageOperate().getIsAdd()
                    ? SpringUtil.getBean(slaveRelation.getSlaveServiceClass()).insertBatch(insertList)
                    : SpringUtil.getBean(slaveRelation.getSlaveManagerClass()).insertBatch(insertList);
        // 3.判断是否执行修改
        if (CollUtil.isNotEmpty(updateList))
            rows += slaveRelation.getLinkageOperate().getIsEdit()
                    ? SpringUtil.getBean(slaveRelation.getSlaveServiceClass()).updateBatch(updateList)
                    : SpringUtil.getBean(slaveRelation.getSlaveManagerClass()).updateBatch(updateList);
        // 4.判断是否执行删除
        if (CollUtil.isNotEmpty(delKeys)) {
            if (slaveRelation.getLinkageOperate().getIsDelete()) {
                rows += SpringUtil.getBean(slaveRelation.getSlaveServiceClass()).deleteByIds(delKeys);
            } else {
                Set<Object> delMainKeys = CollUtil.isNotEmpty(newList) ? newList.stream().map(item -> getFieldObj(item, slaveRelation.getMainField())).collect(Collectors.toSet()) : new HashSet<>();
                SqlField sqlMainField = new SqlField(SqlConstants.OperateType.IN, slaveRelation.getSlaveFieldSqlName(), delMainKeys, slaveRelation.getLinkageOperate());
                SqlField sqlArrField = new SqlField(SqlConstants.OperateType.IN, slaveRelation.getSlaveIdFieldSqlName(), delKeys, slaveRelation.getLinkageOperate());
                rows += SpringUtil.getBean(slaveRelation.getSlaveManagerClass()).deleteByField(sqlMainField, sqlArrField);
            }
        }
        // 5.返回操作结果
        return rows;
    }

    /**
     * 新增关联数据 | 间接关联
     *
     * @param originDto     源数据对象
     * @param newDto        新数据对象
     * @param slaveRelation 从属关联关系定义对象
     */
    private static <D, MP extends BasisEntity> int updateIndirectObj(D originDto, D newDto, SlaveRelation slaveRelation) {
        List<MP> insertList = new ArrayList<>();
        Set<Object> delKeys = new HashSet<>();
        // 1.组装操作数据
        updateIndirectBuild(originDto, newDto, slaveRelation, insertList, delKeys);
        int rows = NumberUtil.Zero;
        // 2.判断是否执行新增
        if (CollUtil.isNotEmpty(insertList))
            rows += SpringUtil.getBean(slaveRelation.getMergeClass()).insertBatch(insertList);
        // 3.判断是否执行删除
        if (CollUtil.isNotEmpty(delKeys)) {
            Object delMainKey = getFieldObj(newDto, slaveRelation.getMainField());
            SqlField sqlMainField = new SqlField(SqlConstants.OperateType.EQ, slaveRelation.getMergeMainFieldSqlName(), delMainKey, slaveRelation.getLinkageOperate());
            SqlField sqlArrField = new SqlField(SqlConstants.OperateType.IN, slaveRelation.getMergeSlaveFieldSqlName(), delKeys, slaveRelation.getLinkageOperate());
            rows += SpringUtil.getBean(slaveRelation.getMergeClass()).deleteByField(sqlMainField, sqlArrField);
        }
        // 4.返回操作结果
        return rows;
    }

    /**
     * 新增关联数据 | 间接关联
     *
     * @param originList    源数据对象集合
     * @param newList       新数据对象集合
     * @param slaveRelation 从属关联关系定义对象
     */
    private static <D, MP extends BasisEntity> int updateIndirectList(Collection<D> originList, Collection<D> newList, SlaveRelation slaveRelation) {
        List<MP> insertList = new ArrayList<>();
        Set<Object> delKeys = new HashSet<>();
        // 1.组装操作数据
        Map<Object, D> originMap = CollUtil.isEmpty(originList)
                ? new HashMap<>()
                : originList.stream().collect(
                Collectors.toMap(item -> getFieldObj(item, slaveRelation.getMainIdField()), Function.identity()));
        if (CollUtil.isNotEmpty(newList)) {
            newList.forEach(newDto -> {
                D originDto = originMap.get(getFieldObj(newDto, slaveRelation.getMainIdField()));
                updateIndirectBuild(originDto, newDto, slaveRelation, insertList, delKeys);
            });
        }
        int rows = NumberUtil.Zero;
        // 2.判断是否执行新增
        if (CollUtil.isNotEmpty(insertList))
            rows += SpringUtil.getBean(slaveRelation.getMergeClass()).insertBatch(insertList);
        // 3.判断是否执行删除
        if (CollUtil.isNotEmpty(delKeys)) {
            Set<Object> delMainKeys = CollUtil.isNotEmpty(newList) ? newList.stream().map(item -> getFieldObj(item, slaveRelation.getMainField())).collect(Collectors.toSet()) : new HashSet<>();
            SqlField sqlMainField = new SqlField(SqlConstants.OperateType.IN, slaveRelation.getMergeMainFieldSqlName(), delMainKeys, slaveRelation.getLinkageOperate());
            SqlField sqlArrField = new SqlField(SqlConstants.OperateType.IN, slaveRelation.getMergeSlaveFieldSqlName(), delKeys, slaveRelation.getLinkageOperate());
            rows += SpringUtil.getBean(slaveRelation.getMergeClass()).deleteByField(sqlMainField, sqlArrField);
        }
        // 4.返回操作结果
        return rows;
    }

    /**
     * 新增关联数据 | 直接关联
     *
     * @param dto           数据对象
     * @param slaveRelation 从属关联关系定义对象
     */
    private static <D> int deleteDirectObj(D dto, SlaveRelation slaveRelation) {
        if (slaveRelation.getLinkageOperate().getIsDelete()) {
            Set<Object> delKeys = new HashSet<>();
            deleteDirectBuild(dto, slaveRelation, delKeys);
            if (CollUtil.isNotEmpty(delKeys)) {
                return SpringUtil.getBean(slaveRelation.getSlaveServiceClass()).deleteByIds(delKeys);
            }
        } else {
            Object delKey = getFieldObj(dto, slaveRelation.getMainField());
            if (ObjectUtil.isNotEmpty(delKey)) {
                SqlField sqlField = new SqlField(SqlConstants.OperateType.EQ, slaveRelation.getSlaveFieldSqlName(), delKey, slaveRelation.getLinkageOperate());
                return SpringUtil.getBean(slaveRelation.getSlaveManagerClass()).deleteByField(sqlField);
            }
        }
        return NumberUtil.Zero;
    }

    /**
     * 新增关联数据 | 直接关联
     *
     * @param dtoList       数据对象集合
     * @param slaveRelation 从属关联关系定义对象
     */
    private static <D> int deleteDirectList(Collection<D> dtoList, SlaveRelation slaveRelation) {
        if (slaveRelation.getLinkageOperate().getIsDelete()) {
            Set<Object> delKeys = new HashSet<>();
            if (CollUtil.isNotEmpty(dtoList)) {
                dtoList.forEach(dto -> deleteDirectBuild(dto, slaveRelation, delKeys));
            }
            if (CollUtil.isNotEmpty(delKeys)) {
                return SpringUtil.getBean(slaveRelation.getSlaveServiceClass()).deleteByIds(delKeys);
            }
        } else {
            Set<Object> delKeys = dtoList.stream().map(item -> getFieldObj(item, slaveRelation.getMainField())).collect(Collectors.toSet());
            if (CollUtil.isNotEmpty(delKeys)) {
                SqlField sqlField = new SqlField(SqlConstants.OperateType.IN, slaveRelation.getSlaveFieldSqlName(), delKeys, slaveRelation.getLinkageOperate());
                return SpringUtil.getBean(slaveRelation.getSlaveManagerClass()).deleteByField(sqlField);
            }
        }
        return NumberUtil.Zero;
    }

    /**
     * 新增关联数据 | 间接关联
     *
     * @param dto           数据对象
     * @param slaveRelation 从属关联关系定义对象
     */
    private static <D> int deleteIndirectObj(D dto, SlaveRelation slaveRelation) {
        Object delKey = getFieldObj(dto, slaveRelation.getMainField());
        SqlField sqlField = new SqlField(SqlConstants.OperateType.EQ, slaveRelation.getMergeMainFieldSqlName(), delKey, slaveRelation.getLinkageOperate());
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
        SqlField sqlField = new SqlField(SqlConstants.OperateType.IN, slaveRelation.getMergeMainFieldSqlName(), delKeys, slaveRelation.getLinkageOperate());
        return SpringUtil.getBean(slaveRelation.getMergeClass()).deleteByField(sqlField);
    }

    /**
     * 新增关联数据 | 数据组装 | 直接关联
     *
     * @param dto           数据对象
     * @param slaveRelation 从属关联关系定义对象
     * @return 数据对象集合
     */
    private static <D, SD extends BaseEntity> List<SD> insertDirectBuild(D dto, SlaveRelation slaveRelation) {
        Object mainKey = getFieldObj(dto, slaveRelation.getMainField());
        Object subObj = getFieldObj(dto, slaveRelation.getReceiveField());
        List<SD> subList = new ArrayList<>();
        Optional.ofNullable(subObj).ifPresent((obj) -> {
            Class<?> fieldType = slaveRelation.getReceiveField().getType();
            if (ClassUtil.isNormalClass(fieldType)) {
                setField(obj, slaveRelation.getSlaveField(), mainKey);
                subList.add((SD) obj);
            } else if (ClassUtil.isCollection(fieldType)) {
                Collection<Object> objColl = (Collection<Object>) obj;
                if (CollUtil.isNotEmpty(objColl)) {
                    subList.addAll(objColl.stream().map(item -> {
                        setField(item, slaveRelation.getSlaveField(), mainKey);
                        return (SD) item;
                    }).toList());
                }
            }
        });
        return subList;
    }

    /**
     * 新增关联数据 | 数据组装 | 间接关联
     *
     * @param dto           数据对象
     * @param slaveRelation 从属关联关系定义对象
     * @return 数据对象集合
     */
    private static <D, MP extends BasisEntity> List<MP> insertIndirectBuild(D dto, SlaveRelation slaveRelation) {
        Object mainKey = getFieldObj(dto, slaveRelation.getMainField());
        Object mergeObj = getFieldObj(dto, slaveRelation.getReceiveArrField());

        List<MP> mergeList = new ArrayList<>();
        Optional.ofNullable(mergeObj).ifPresent((obj) -> {
            Class<?> fieldType = slaveRelation.getReceiveArrField().getType();
            if (ClassUtil.isCollection(fieldType)) {
                Collection<Object> objColl = (Collection<Object>) obj;
                mergeList.addAll(objColl.stream().distinct().map(item -> {
                    Object mergePo = createObj(slaveRelation.getMergePoClass());
                    setField(mergePo, slaveRelation.getMergeMainField(), mainKey);
                    setField(mergePo, slaveRelation.getMergeSlaveField(), item);
                    return (MP) mergePo;
                }).toList());
            } else if (ClassUtil.isArray(fieldType)) {
                Object[] objArr = (Object[]) obj;
                mergeList.addAll(Arrays.stream(objArr).distinct().map(item -> {
                    Object mergePo = createObj(slaveRelation.getMergePoClass());
                    setField(mergePo, slaveRelation.getMergeMainField(), mainKey);
                    setField(mergePo, slaveRelation.getMergeSlaveField(), item);
                    return (MP) mergePo;
                }).toList());
            }
        });

        return mergeList;
    }

    /**
     * 修改关联数据 | 数据组装 | 直接关联
     *
     * @param originDto     源数据对象
     * @param newDto        新数据对象
     * @param slaveRelation 从属关联关系定义对象
     * @param insertList    待新增数据对象集合
     * @param updateList    待修改数据对象集合
     * @param delKeys       待删除键值集合
     */
    private static <D, SD extends BaseEntity> void updateDirectBuild(D originDto, D newDto, SlaveRelation slaveRelation, List<SD> insertList, List<SD> updateList, Set<Object> delKeys) {
        Object originSubObj = getFieldObj(originDto, slaveRelation.getReceiveField());
        Object newSubObj = getFieldObj(newDto, slaveRelation.getReceiveField());

        Object mainKey = getFieldObj(newDto, slaveRelation.getMainField());

        Class<?> fieldType = slaveRelation.getReceiveField().getType();
        if (ClassUtil.isNormalClass(fieldType)) {
            if (ObjectUtil.isNotNull(newSubObj)) {
                if (ObjectUtil.isNotNull(originSubObj)) {
                    Object originSlaveId = getFieldObj(originDto, slaveRelation.getSlaveIdField());
                    Object newSlaveId = getFieldObj(newDto, slaveRelation.getSlaveIdField());
                    if (ObjectUtil.equals(originSlaveId, newSlaveId)) {
                        updateList.add((SD) newDto);
                    } else {
                        delKeys.add(originSlaveId);
                        setField(newDto, slaveRelation.getSlaveField(), mainKey);
                        insertList.add((SD) newDto);
                    }
                } else {
                    setField(newDto, slaveRelation.getSlaveField(), mainKey);
                    insertList.add((SD) newDto);
                }
            } else if (ObjectUtil.isNotNull(originSubObj)) {
                Object originSlaveId = getFieldObj(originDto, slaveRelation.getSlaveIdField());
                delKeys.add(originSlaveId);
            }
        } else if (ClassUtil.isCollection(fieldType)) {
            Collection<Object> originSubColl = (Collection<Object>) originSubObj;
            Collection<Object> newSubColl = (Collection<Object>) newSubObj;
            Map<Object, Object> originSubMap = CollUtil.isEmpty(originSubColl)
                    ? new HashMap<>()
                    : originSubColl.stream().filter(ObjectUtil::isNotNull)
                    .map(item -> getFieldObj(item, slaveRelation.getSlaveIdField())).filter(ObjectUtil::isNotNull)
                    .collect(Collectors.toMap(Function.identity(), Function.identity(), (v1, v2) -> v1));

            Map<Object, Object> newSubMap = CollUtil.isEmpty(newSubColl)
                    ? new HashMap<>()
                    : newSubColl.stream().filter(ObjectUtil::isNotNull)
                    .map(item -> {
                        Object slaveId = getFieldObj(item, slaveRelation.getSlaveIdField());
                        if (originSubMap.containsKey(slaveId)) {
                            updateList.add((SD) item);
                        } else {
                            setField(item, slaveRelation.getSlaveField(), mainKey);
                            insertList.add((SD) item);
                        }
                        return slaveId;
                    }).filter(ObjectUtil::isNotNull)
                    .collect(Collectors.toMap(Function.identity(), Function.identity(), (v1, v2) -> v1));
            if (CollUtil.isNotEmpty(originSubColl)) {
                originSubColl.stream().filter(ObjectUtil::isNotNull).forEach(item -> {
                    Object slaveId = getFieldObj(item, slaveRelation.getSlaveIdField());
                    if (!newSubMap.containsKey(slaveId))
                        delKeys.add(slaveId);
                });
            }
        }
    }

    /**
     * 修改关联数据 | 数据组装 | 间接关联
     *
     * @param originDto     源数据对象
     * @param newDto        新数据对象
     * @param slaveRelation 从属关联关系定义对象
     * @param insertList    待新增数据对象集合
     * @param delKeys       待删除键值集合
     */
    private static <D, MP extends BasisEntity> void updateIndirectBuild(D originDto, D newDto, SlaveRelation slaveRelation, List<MP> insertList, Set<Object> delKeys) {
        Field receiveArrField = slaveRelation.getReceiveArrField();
        Set<Object> originSet = new HashSet<>();
        Set<Object> newSet = new HashSet<>();
        Object originArr = getFieldObj(originDto, receiveArrField);
        Object newArr = getFieldObj(newDto, receiveArrField);
        if (ObjectUtil.isNotNull(originArr)) {
            if (ClassUtil.isArray(receiveArrField.getType())) {
                originSet.addAll(Arrays.asList((Object[]) originArr));
            } else if (ClassUtil.isNotCollection(receiveArrField.getType())) {
                originSet.addAll((Collection<?>) originArr);
            }
        }
        if (ObjectUtil.isNotNull(newArr)) {
            if (ClassUtil.isArray(receiveArrField.getType())) {
                newSet.addAll(Arrays.asList((Object[]) newArr));
            } else if (ClassUtil.isNotCollection(receiveArrField.getType())) {
                newSet.addAll((Collection<?>) newArr);
            }
        }
        // 1.获取待删除从数据键值
        Set<Object> delSet = new HashSet<>(originSet);
        delSet.removeAll(newSet);
        if (CollUtil.isNotEmpty(delSet))
            delKeys.addAll(delSet);
        // 2.获取待新增从数据键值
        Set<Object> insertSet = new HashSet<>(newSet);
        insertSet.removeAll(originSet);
        // 3.组装待新增从数据对象
        if (CollUtil.isNotEmpty(insertSet)) {
            Object mainKey = getFieldObj(newDto, slaveRelation.getMainField());
            List<MP> mergeList = insertSet.stream().distinct().map(item -> {
                Object mergePo = createObj(slaveRelation.getMergePoClass());
                setField(mergePo, slaveRelation.getMergeMainField(), mainKey);
                setField(mergePo, slaveRelation.getMergeSlaveField(), item);
                return (MP) mergePo;
            }).toList();
            if (CollUtil.isNotEmpty(mergeList))
                insertList.addAll(mergeList);
        }
    }

    /**
     * 删除关联数据 | 数据组装 | 直接关联
     *
     * @param originDto     源数据对象
     * @param slaveRelation 从属关联关系定义对象
     * @param delKeys       待删除键值集合
     */
    private static <D> void deleteDirectBuild(D originDto, SlaveRelation slaveRelation, Set<Object> delKeys) {
        Object subObj = getFieldObj(originDto, slaveRelation.getReceiveField());
        Class<?> fieldType = slaveRelation.getReceiveField().getType();
        if (ClassUtil.isNormalClass(fieldType)) {
            if (ObjectUtil.isNotNull(subObj)) {
                Object originSlaveId = getFieldObj(originDto, slaveRelation.getSlaveIdField());
                delKeys.add(originSlaveId);
            }
        } else if (ClassUtil.isCollection(fieldType)) {
            Collection<Object> subColl = (Collection<Object>) subObj;
            if (CollUtil.isNotEmpty(subColl)) {
                subColl.stream().filter(ObjectUtil::isNotNull).forEach(item -> delKeys.add(getFieldObj(item, slaveRelation.getSlaveIdField())));
            }
        }
    }

    /**
     * 初始化数据字段关系 | 初始化校验
     *
     * @param slaveRelation 从属关联关系定义对象
     */
    private static void initCorrelationField(SlaveRelation slaveRelation, SubOperate operate) {
        // 1.校验数据字段是否合规
        if (checkOperateLegal(slaveRelation, operate))
            return;
        // 2.初始化主数据对象映射关系
        initMainCorrelation(slaveRelation);
        // 3.初始化子数据对象映射关系
        initSlaveCorrelation(slaveRelation);
        // 4.初始化中间数据对象映射关系
        initMergeCorrelation(slaveRelation);
        // 5.二次校验 - 如果依旧未通过，则代表人为配置出错，检查代码
        if (!checkOperateLegal(slaveRelation, operate))
            throw new UtilException(StrUtil.format(UtilErrorConstants.MergeError.CORRELATION_ERROR.getInfo(), slaveRelation.getGroupName()));
    }

    /**
     * 校验数据字段是否合规 | 初始化校验
     *
     * @param relation 从属关联关系定义对象
     * @param operate  操作类型
     */
    private static boolean checkOperateLegal(SlaveRelation relation, SubOperate operate) {
        // 主数据的主键不能为null
        if (ObjectUtil.hasNull(relation.getMainField(), relation.getMainDtoClass()))
            return Boolean.FALSE;
        switch (relation.getRelationType()) {
            case DIRECT -> {
                // 主数据的从数据class不能为null
                if (ObjectUtil.isNull(relation.getSlaveManagerClass()))
                    return Boolean.FALSE;
                switch (operate) {
                    case SELECT -> {
                        return ObjectUtil.isAllNotEmpty(relation.getSlaveField(), relation.getSlaveFieldSqlName(), relation.getReceiveField());
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
                        return ObjectUtil.isAllNotEmpty(relation.getSlaveManagerClass(), relation.getMergeSlaveField(), relation.getSlaveField(),
                                relation.getSlaveFieldSqlName(), relation.getReceiveField());
                    }
                    case ADD -> {
                        return ObjectUtil.isAllNotEmpty(relation.getReceiveArrField(), relation.getMergePoClass(), relation.getMergeSlaveField(),
                                relation.getSlaveFieldSqlName());
                    }
                    case EDIT -> {
                        return ObjectUtil.isAllNotEmpty(relation.getMainIdField(), relation.getReceiveArrField(), relation.getMergePoClass(), relation.getMergeSlaveField(),
                                relation.getSlaveFieldSqlName());
                    }
                    case DELETE -> {
                    }
                }
            }
        }
        return Boolean.TRUE;
    }

    /**
     * 初始化主数据对象映射关系 | 初始化校验
     *
     * @param slaveRelation 从属关联关系定义对象
     */
    private static void initMainCorrelation(SlaveRelation slaveRelation) {
        // 1.校验主数据对象Class是否存在
        if (ObjectUtil.isNull(slaveRelation.getMainDtoClass()))
            return;
        Field[] fields = ReflectUtil.getFields(slaveRelation.getMainDtoClass());
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

        Field idField = Arrays.stream(fields).filter(field -> field.getAnnotation(TableId.class) != null).findFirst().orElse(null);
        if (ObjectUtil.isNotNull(idField)) {
            // 2.赋值主键字段
            slaveRelation.setMainIdField(idField);
            // 3.如果主数据主键未定义 -> 取主数据表主键
            if (ObjectUtil.isNull(slaveRelation.getMainField())) {
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
        if (ObjectUtil.isNull(slaveRelation.getSlaveManagerClass()))
            return;
        Class<? extends BaseEntity> slavePClass = SpringUtil.getBean(slaveRelation.getSlaveManagerClass()).getPClass();
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

        // 3.初始化从数据表主键
        if (ObjectUtil.isNull(slaveRelation.getSlaveIdField())) {
            Field idField = Arrays.stream(fields).filter(field -> field.isAnnotationPresent(TableId.class)).findFirst().orElse(null);
            if (ObjectUtil.isNotNull(idField)) {

                TableId tableIdField = idField.getAnnotation(TableId.class);
                String slaveIdFieldSqlName = ObjectUtil.isNotNull(tableIdField) && StrUtil.isNotEmpty(tableIdField.value())
                        ? tableIdField.value()
                        : StrUtil.toUnderlineCase(idField.getName());
                slaveRelation.setSlaveIdField(idField);
                slaveRelation.getSlaveIdField().setAccessible(Boolean.TRUE);
                slaveRelation.setSlaveIdFieldSqlName(slaveIdFieldSqlName);
            }
        }

        // 4.如果从数据主键未定义 -> 取从数据表主键
        if (ObjectUtil.isNull(slaveRelation.getSlaveField())) {
            if (ObjectUtil.isNotNull(slaveRelation.getSlaveIdField())) {
                slaveRelation.setSlaveField(slaveRelation.getSlaveIdField());
            }
        }

        // 5.初始化从数据主键数据库字段名
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
        if (ObjectUtil.equals(correlation.keyType(), OperateConstants.SubKeyType.SLAVE)) {
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
        // 1.校验中间类数据对象class是否为null
        if (ObjectUtil.isNull(slaveRelation.getMergePoClass()))
            return;
        ParameterizedType mergeClass = TypeUtil.toParameterizedType(slaveRelation.getMergeClass());
        if (ObjectUtil.isNotNull(mergeClass) || ArrayUtil.isEmpty(mergeClass.getActualTypeArguments())) {
            Type type = mergeClass.getActualTypeArguments()[NumberUtil.Zero];
            boolean isNotEqual = true;
            if (type instanceof Class<?> clazz) {
                isNotEqual = ClassUtil.notEqual(clazz, slaveRelation.getMergePoClass());
            }
            if (isNotEqual) {
                throw new UtilException(StrUtil.format(UtilErrorConstants.MergeError.MERGE_PO_CLASS_EQUAL.getInfo(), slaveRelation.getGroupName()));
            }
        }
        Field[] fields = ReflectUtil.getFields(slaveRelation.getMergePoClass());
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
                    slaveRelation.getMergeMainField().setAccessible(Boolean.TRUE);
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
    private static void setField(Object item, Field field, Collection<?> coll) {
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
    private static <D> D createObj(Class<D> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}