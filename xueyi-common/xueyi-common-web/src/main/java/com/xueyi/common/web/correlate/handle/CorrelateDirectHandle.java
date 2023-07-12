package com.xueyi.common.web.correlate.handle;

import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.core.utils.core.ClassUtil;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.NumberUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.SpringUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.web.correlate.contant.CorrelateConstants;
import com.xueyi.common.web.correlate.domain.Direct;
import com.xueyi.common.web.correlate.domain.SqlField;
import com.xueyi.common.web.correlate.utils.CorrelateUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 主从关联数据处理 | 直接关联
 *
 * @author xueyi
 */
@Slf4j
@SuppressWarnings({"unchecked"})
public final class CorrelateDirectHandle extends CorrelateBaseHandle {

    /**
     * 组装数据对象关联数据 | 直接关联
     *
     * @param dto    数据对象
     * @param direct 直接关联映射对象
     */
    public static <D extends BaseEntity, S extends BaseEntity> void assembleDirectObj(D dto, Direct<D, S> direct) {
        assembleDirectBuild(dto, null, direct);
    }

    /**
     * 组装集合关联数据 | 直接关联
     *
     * @param dtoList 数据对象集合
     * @param direct  直接关联映射对象
     */
    public static <D extends BaseEntity, S extends BaseEntity, Coll extends Collection<D>> void assembleDirectList(Coll dtoList, Direct<D, S> direct) {
        assembleDirectBuild(null, dtoList, direct);
    }

    /**
     * 新增关联数据 | 直接关联
     *
     * @param dto    数据对象
     * @param direct 直接关联映射对象
     * @return 结果
     */
    public static <D extends BaseEntity, S extends BaseEntity> int insertDirectObj(D dto, Direct<D, S> direct) {
        Direct.ORM ormDirect = direct.getOrm();
        Collection<S> subList = insertDirectBuild(dto, ormDirect);
        if (CollUtil.isEmpty(subList)) {
            return NumberUtil.Zero;
        }
        // 子查询进行数据关联操作
        CorrelateUtil.startCorrelates(direct.getRelations());
        return SpringUtil.getBean(ormDirect.getSlaveService()).insertBatch(subList);
    }

    /**
     * 新增关联数据 | 直接关联
     *
     * @param dtoList 数据对象集合
     * @param direct  直接关联映射对象
     * @return 结果
     */
    public static <D extends BaseEntity, S extends BaseEntity, Coll extends Collection<D>> int insertDirectList(Coll dtoList, Direct<D, S> direct) {
        Direct.ORM ormDirect = direct.getOrm();
        List<S> subList = dtoList.stream().map(dto -> (List<S>) insertDirectBuild(dto, ormDirect)).filter(CollUtil::isNotEmpty).flatMap(Collection::stream).toList();
        if (CollUtil.isEmpty(subList)) {
            return NumberUtil.Zero;
        }
        // 子查询进行数据关联操作
        CorrelateUtil.startCorrelates(direct.getRelations());
        return SpringUtil.getBean(ormDirect.getSlaveService()).insertBatch(subList);
    }

    /**
     * 修改关联数据 | 直接关联
     *
     * @param originDto 源数据对象
     * @param newDto    新数据对象
     * @param direct    直接关联映射对象
     * @return 结果
     */
    public static <D extends BaseEntity, S extends BaseEntity> int updateDirectObj(D originDto, D newDto, Direct<D, S> direct) {
        Direct.ORM ormDirect = direct.getOrm();
        List<S> insertList = new ArrayList<>();
        List<S> updateList = new ArrayList<>();
        Set<Object> delKeys = new HashSet<>();
        // 1.组装操作数据
        updateDirectBuild(originDto, newDto, ormDirect, insertList, updateList, delKeys);
        int rows = NumberUtil.Zero;
        // 2.判断是否执行新增
        if (CollUtil.isNotEmpty(insertList)) {
            // 子查询进行数据关联操作
            CorrelateUtil.startCorrelates(direct.getRelations());
            rows += SpringUtil.getBean(ormDirect.getSlaveService()).insertBatch(insertList);
        }
        // 3.判断是否执行修改
        if (CollUtil.isNotEmpty(updateList)) {
            // 子查询进行数据关联操作
            CorrelateUtil.startCorrelates(direct.getRelations());
            rows += SpringUtil.getBean(ormDirect.getSlaveService()).updateBatch(updateList);
        }
        // 4.判断是否执行删除
        if (CollUtil.isNotEmpty(delKeys)) {
            // 子查询进行数据关联操作
            CorrelateUtil.startCorrelates(direct.getRelations());
            rows += SpringUtil.getBean(ormDirect.getSlaveService()).deleteByIds(delKeys);
        }
        // 5.返回操作结果
        return rows;
    }

    /**
     * 修改关联数据 | 直接关联
     *
     * @param originList 源数据对象集合
     * @param newList    新数据对象集合
     * @param direct     直接关联映射对象
     * @return 结果
     */
    public static <D extends BaseEntity, S extends BaseEntity, Coll extends Collection<D>> int updateDirectList(Coll originList, Coll newList, Direct<D, S> direct) {
        Direct.ORM ormDirect = direct.getOrm();
        List<S> insertList = new ArrayList<>();
        List<S> updateList = new ArrayList<>();
        Set<Object> delKeys = new HashSet<>();
        // 1.组装操作数据
        Map<Object, D> originMap = CollUtil.isEmpty(originList)
                ? new HashMap<>()
                : originList.stream().collect(
                Collectors.toMap(item -> getFieldObj(item, ormDirect.getMainKeyField()), Function.identity()));

        if (CollUtil.isNotEmpty(newList)) {
            newList.forEach(newDto -> {
                D originDto = getMapObj(originMap, getFieldObj(newDto, ormDirect.getMainKeyField()));
                updateDirectBuild(originDto, newDto, ormDirect, insertList, updateList, delKeys);
            });
        }
        int rows = NumberUtil.Zero;

        // 2.判断是否执行新增
        if (CollUtil.isNotEmpty(insertList)) {
            // 子查询进行数据关联操作
            CorrelateUtil.startCorrelates(direct.getRelations());
            rows += SpringUtil.getBean(ormDirect.getSlaveService()).insertBatch(insertList);
        }
        // 3.判断是否执行修改
        if (CollUtil.isNotEmpty(updateList)) {
            // 子查询进行数据关联操作
            CorrelateUtil.startCorrelates(direct.getRelations());
            rows += SpringUtil.getBean(ormDirect.getSlaveService()).updateBatch(updateList);
        }
        // 4.判断是否执行删除
        if (CollUtil.isNotEmpty(delKeys)) {
            // 子查询进行数据关联操作
            CorrelateUtil.startCorrelates(direct.getRelations());
            rows += SpringUtil.getBean(ormDirect.getSlaveService()).deleteByIds(delKeys);
        }
        // 5.返回操作结果
        return rows;
    }

    /**
     * 删除关联数据 | 直接关联
     *
     * @param dto    数据对象
     * @param direct 直接关联映射对象
     * @return 结果
     */
    public static <D extends BaseEntity, S extends BaseEntity> int deleteDirectObj(D dto, Direct<D, S> direct) {
        Direct.ORM ormDirect = direct.getOrm();
        Object mainKey = getFieldObj(dto, ormDirect.getMainKeyField());
        if (Objects.requireNonNull(ormDirect.getSubDataRow()) == CorrelateConstants.DataRow.SINGLE) {
            SqlField sqlField = new SqlField(SqlConstants.OperateType.EQ, ormDirect.getSlaveKeySqlName(), mainKey);
            List<S> subList = (List<S>) SpringUtil.getBean(ormDirect.getSlaveService()).selectListByField(sqlField);
            if (CollUtil.isNotEmpty(subList)) {
                Set<Object> delKeys = subList.stream().map(S::getId).collect(Collectors.toSet());
                // 子查询进行数据关联操作
                CorrelateUtil.startCorrelates(direct.getRelations());
                return SpringUtil.getBean(ormDirect.getSlaveService()).deleteByIds(delKeys);
            }
        }
        return NumberUtil.Zero;
    }

    /**
     * 删除关联数据 | 直接关联
     *
     * @param dtoList 数据对象集合
     * @param direct  直接关联映射对象
     * @return 结果
     */
    public static <D extends BaseEntity, S extends BaseEntity, Coll extends Collection<D>> int deleteDirectList(Coll dtoList, Direct<D, S> direct) {
        Direct.ORM ormDirect = direct.getOrm();
        Set<Object> mainKeys = dtoList.stream().map(item -> getFieldObj(item, ormDirect.getMainKeyField())).collect(Collectors.toSet());
        if (Objects.requireNonNull(ormDirect.getSubDataRow()) == CorrelateConstants.DataRow.SINGLE) {
            SqlField sqlField = new SqlField(SqlConstants.OperateType.IN, ormDirect.getSlaveKeySqlName(), mainKeys);
            List<S> subList = (List<S>) SpringUtil.getBean(ormDirect.getSlaveService()).selectListByField(sqlField);
            if (CollUtil.isNotEmpty(subList)) {
                Set<Object> delKeys = subList.stream().map(S::getId).collect(Collectors.toSet());
                // 子查询进行数据关联操作
                CorrelateUtil.startCorrelates(direct.getRelations());
                return SpringUtil.getBean(ormDirect.getSlaveService()).deleteByIds(delKeys);
            }
        }
        return NumberUtil.Zero;
    }

    /**
     * 查询关联数据 | 数据组装 | 直接关联
     *
     * @param dto     数据对象
     * @param dtoList 数据对象集合
     * @param direct  直接关联映射对象
     */
    private static <D extends BaseEntity, S extends BaseEntity, Coll extends Collection<D>> void assembleDirectBuild(D dto, Coll dtoList, Direct<D, S> direct) {
        Direct.ORM ormDirect = direct.getOrm();
        Set<Object> findInSet = ObjectUtil.isNotNull(dto)
                ? getFieldKeys(dto, ormDirect, ormDirect.getMainKeyField())
                : getFieldKeys(dtoList, ormDirect, ormDirect.getMainKeyField());
        if (CollUtil.isEmpty(findInSet)) {
            return;
        }
        SqlField sqlField = new SqlField(SqlConstants.OperateType.IN, ormDirect.getSlaveKeySqlName(), findInSet);
        // 子查询进行数据关联操作
        CorrelateUtil.startCorrelates(direct.getRelations());
        List<S> subList = (List<S>) SpringUtil.getBean(ormDirect.getSlaveService()).selectListByField(sqlField);
        if (ObjectUtil.isNotNull(dto)) {
            setSubField(dto, subList, ormDirect.getSubDataRow(), ormDirect.getMergeType(), ormDirect.getMainKeyField(), ormDirect.getSlaveKeyField(), ormDirect.getSubInfoField());
        } else if (CollUtil.isNotEmpty(dtoList)) {
            setSubField(dtoList, subList, ormDirect.getSubDataRow(), ormDirect.getMergeType(), ormDirect.getMainKeyField(), ormDirect.getSlaveKeyField(), ormDirect.getSubInfoField());
        }
    }

    /**
     * 新增关联数据 | 数据组装 | 直接关联
     *
     * @param dto       数据对象
     * @param ormDirect 直接关联数据映射对象
     * @return 数据对象集合
     */
    private static <D extends BaseEntity, S extends BaseEntity> List<S> insertDirectBuild(D dto, Direct.ORM ormDirect) {
        Object mainKey = getFieldObj(dto, ormDirect.getMainKeyField());
        Object subObj = getFieldObj(dto, ormDirect.getSubInfoField());
        if (ObjectUtil.isNull(subObj)) {
            return new ArrayList<>();
        }
        List<S> subList = new ArrayList<>();
        switch (ormDirect.getSubDataRow()) {
            case SINGLE -> {
                setField(subObj, ormDirect.getSlaveKeyField(), mainKey);
                subList.add((S) subObj);
            }
            case LIST -> {
                List<S> addList = ((Collection<S>) subObj).stream().peek(item -> setField(item, ormDirect.getSlaveKeyField(), mainKey)).toList();
                subList.addAll(addList);
            }
        }
        return subList;
    }

    /**
     * 修改关联数据 | 数据组装 | 直接关联
     *
     * @param originDto  源数据对象
     * @param newDto     新数据对象
     * @param ormDirect  直接关联数据映射对象
     * @param insertList 待新增数据对象集合
     * @param updateList 待修改数据对象集合
     * @param delKeys    待删除键值集合
     */
    private static <D extends BaseEntity, S extends BaseEntity> void updateDirectBuild(D originDto, D newDto, Direct.ORM ormDirect, List<S> insertList, List<S> updateList, Set<Object> delKeys) {
        Object originSub = getFieldObj(originDto, ormDirect.getSubInfoField());
        Object newSub = getFieldObj(newDto, ormDirect.getSubInfoField());

        Object mainKey = getFieldObj(newDto, ormDirect.getMainKeyField());

        switch (ormDirect.getSubDataRow()) {
            case SINGLE -> {
                S newSubObj = (S) newSub;
                S originSubObj = (S) originSub;
                if (ObjectUtil.isNotNull(newSubObj)) {
                    if (ObjectUtil.isNotNull(originSubObj)) {
                        if (ObjectUtil.equals(originSubObj.getId(), newSubObj.getId())) {
                            updateList.add(newSubObj);
                        } else {
                            delKeys.add(originSubObj.getId());
                            setField(newSubObj, ormDirect.getSlaveKeyField(), mainKey);
                            insertList.add(newSubObj);
                        }
                    } else {
                        setField(newSubObj, ormDirect.getSlaveKeyField(), mainKey);
                        insertList.add(newSubObj);
                    }
                } else if (ObjectUtil.isNotNull(originSubObj)) {
                    delKeys.add(originSubObj.getId());
                }
            }
            case LIST -> {
                Collection<S> originSubColl = (Collection<S>) originSub;
                Collection<S> newSubColl = (Collection<S>) newSub;
                Map<Object, Object> originSubMap = CollUtil.isEmpty(originSubColl)
                        ? new HashMap<>()
                        : originSubColl.stream().filter(ObjectUtil::isNotNull)
                        .map(S::getId).filter(ObjectUtil::isNotNull)
                        .collect(Collectors.toMap(Function.identity(), Function.identity(), (v1, v2) -> v1));

                Map<Object, Object> newSubMap = CollUtil.isEmpty(newSubColl)
                        ? new HashMap<>()
                        : newSubColl.stream().filter(ObjectUtil::isNotNull)
                        .map(item -> {
                            if (originSubMap.containsKey(item.getId())) {
                                updateList.add(item);
                            } else {
                                setField(item, ormDirect.getSlaveKeyField(), mainKey);
                                insertList.add(item);
                            }
                            return item.getId();
                        }).filter(ObjectUtil::isNotNull)
                        .collect(Collectors.toMap(Function.identity(), Function.identity(), (v1, v2) -> v1));
                if (CollUtil.isNotEmpty(originSubColl)) {
                    Set<Object> keys = originSubColl.stream().filter(ObjectUtil::isNotNull).map(S::getId)
                            .filter(slaveId -> !newSubMap.containsKey(slaveId)).collect(Collectors.toSet());
                    delKeys.addAll(keys);
                }
            }
        }
    }

    /**
     * 校验关联映射是否合规
     *
     * @param direct 直接关联映射对象
     */
    public static <D extends BaseEntity, S extends BaseEntity> void checkORMLegal(Direct<D, S> direct) {
        Direct.ORM ormDirect = direct.getOrm();
        if (ObjectUtil.isNull(ormDirect.getSlaveService())) {
            logReturn(StrUtil.format("groupName: {}, slaveService can not be null", direct.getGroupName()));
        }
        ormDirect.setMergeType(ClassUtil.isBasicType(ormDirect.getMainKeyField().getType()) ? CorrelateConstants.MergeType.DIRECT : CorrelateConstants.MergeType.INDIRECT);
        if (ObjectUtil.isNotNull(ormDirect.getSubDataRow()) && ObjectUtil.equals(CorrelateConstants.DataRow.SINGLE, ormDirect.getSubDataRow()) && ormDirect.getMergeType().isIndirect()) {
            logReturn(StrUtil.format("groupName: {}, subInfoField is single, but mainKeyField is Collection", direct.getGroupName()));
        } else if (ObjectUtil.isNull(ormDirect.getSlaveKeyField()) || ClassUtil.isNotSimpleType(ormDirect.getSlaveKeyField().getType())) {
            logReturn(StrUtil.format("groupName: {}, slaveKeyField can not be null or not BasicType", direct.getGroupName()));
        } else if (ObjectUtil.isNull(ormDirect.getSubDataRow())) {
            if (ormDirect.getMergeType().isDirect()) {
                ormDirect.setSubDataRow(CorrelateConstants.DataRow.SINGLE);
            } else {
                ormDirect.setSubDataRow(CorrelateConstants.DataRow.LIST);
            }
        }
    }
}
