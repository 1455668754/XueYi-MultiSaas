package com.xueyi.common.web.correlate.utils;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.xueyi.common.core.exception.UtilException;
import com.xueyi.common.core.utils.core.ClassUtil;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.EnumUtil;
import com.xueyi.common.core.utils.core.MapUtil;
import com.xueyi.common.core.utils.core.NumberUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.SpringUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.redis.constant.RedisConstants;
import com.xueyi.common.redis.service.RedisService;
import com.xueyi.common.web.correlate.contant.CorrelateConstants;
import com.xueyi.common.web.correlate.domain.BaseCorrelate;
import com.xueyi.common.web.correlate.domain.Direct;
import com.xueyi.common.web.correlate.domain.Indirect;
import com.xueyi.common.web.correlate.domain.Remote;
import com.xueyi.common.web.correlate.handle.CorrelateDirectHandle;
import com.xueyi.common.web.correlate.handle.CorrelateIndirectHandle;
import com.xueyi.common.web.correlate.handle.CorrelateRemoteHandle;
import com.xueyi.common.web.correlate.service.CorrelateService;
import lombok.extern.slf4j.Slf4j;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 主从关联工具类
 *
 * @author xueyi
 */
@Slf4j
@SuppressWarnings({"unchecked", "rawtypes"})
public final class CorrelateUtil {

    private static final TransmittableThreadLocal<Deque<List<? extends BaseCorrelate>>> THREAD_LOCAL = new TransmittableThreadLocal<>();

    private static RedisService redisService;

    public static RedisService getRedisService() {
        if (ObjectUtil.isNull(redisService)) {
            redisService = SpringUtil.getBean(RedisService.class);
        }
        return redisService;
    }

    /**
     * 子数据映射关联 | 查询
     *
     * @param dto 数据对象
     * @return 数据对象
     */
    public static <D extends BaseEntity> D subCorrelates(D dto) {
        getCorrelates().stream().filter(relation -> ObjectUtil.equals(CorrelateConstants.SubOperate.SELECT, relation.getOperateType()))
                .forEach(relation -> subCorrelates(dto, relation));
        return dto;
    }

    /**
     * 子数据映射关联 | 查询（批量）
     *
     * @param dtoList 数据对象集合
     * @return 数据对象集合
     */
    public static <D extends BaseEntity> List<D> subCorrelates(List<D> dtoList) {
        getCorrelates().stream().filter(relation -> ObjectUtil.equals(CorrelateConstants.SubOperate.SELECT, relation.getOperateType()))
                .forEach(relation -> subCorrelates(dtoList, relation));
        return dtoList;
    }

    /**
     * 子数据映射关联 | 新增
     *
     * @param dto 数据对象
     * @return 结果
     */
    public static <D extends BaseEntity> int addCorrelates(D dto) {
        return getCorrelates().stream().filter(relation -> ObjectUtil.equals(CorrelateConstants.SubOperate.ADD, relation.getOperateType()))
                .map(relation -> addCorrelates(dto, relation)).reduce(Integer::sum).orElse(NumberUtil.Zero);
    }

    /**
     * 子数据映射关联 | 新增（批量）
     *
     * @param dtoList 数据对象集合
     * @return 结果
     */
    public static <D extends BaseEntity> int addCorrelates(Collection<D> dtoList) {
        return getCorrelates().stream().filter(relation -> ObjectUtil.equals(CorrelateConstants.SubOperate.ADD, relation.getOperateType()))
                .map(relation -> addCorrelates(dtoList, relation)).reduce(Integer::sum).orElse(NumberUtil.Zero);
    }

    /**
     * 子数据映射关联 | 修改
     *
     * @param originDto 源数据对象
     * @param newDto    新数据对象
     */
    public static <D extends BaseEntity> int editCorrelates(D originDto, D newDto) {
        return getCorrelates().stream().filter(relation -> ObjectUtil.equals(CorrelateConstants.SubOperate.EDIT, relation.getOperateType()))
                .map(relation -> editCorrelates(originDto, newDto, relation)).reduce(Integer::sum).orElse(NumberUtil.Zero);
    }

    /**
     * 子数据映射关联 | 修改（批量）
     *
     * @param originList 源数据对象集合
     * @param newList    新数据对象集合
     */
    public static <D extends BaseEntity> int editCorrelates(Collection<D> originList, Collection<D> newList) {
        return getCorrelates().stream().filter(relation -> ObjectUtil.equals(CorrelateConstants.SubOperate.EDIT, relation.getOperateType()))
                .map(relation -> editCorrelates(originList, newList, relation)).reduce(Integer::sum).orElse(NumberUtil.Zero);
    }

    /**
     * 子数据映射关联 | 删除
     *
     * @param dto 数据对象
     */
    public static <D extends BaseEntity> int delCorrelates(D dto) {
        return getCorrelates().stream().filter(relation -> ObjectUtil.equals(CorrelateConstants.SubOperate.DELETE, relation.getOperateType()))
                .map(relation -> delCorrelates(dto, relation)).reduce(Integer::sum).orElse(NumberUtil.Zero);
    }

    /**
     * 子数据映射关联 | 删除（批量）
     *
     * @param dtoList 数据对象集合
     */
    public static <D extends BaseEntity> int delCorrelates(Collection<D> dtoList) {
        return getCorrelates().stream().filter(relation -> ObjectUtil.equals(CorrelateConstants.SubOperate.DELETE, relation.getOperateType()))
                .map(relation -> delCorrelates(dtoList, relation)).reduce(Integer::sum).orElse(NumberUtil.Zero);
    }

    /**
     * 子数据映射关联 | 查询
     *
     * @param dto      数据对象
     * @param relation 从属关联关系定义对象
     * @return 数据对象
     */
    private static <D extends BaseEntity, Correlate extends BaseCorrelate> D subCorrelates(D dto, Correlate relation) {
        if (proofCorrelation(dto, relation, CorrelateConstants.SubOperate.SELECT)) {
            if (relation instanceof Direct direct) {
                CorrelateDirectHandle.assembleDirectObj(dto, direct);
            } else if (relation instanceof Indirect indirect) {
                CorrelateIndirectHandle.assembleIndirectObj(dto, indirect);
            } else if (relation instanceof Remote remote) {
                CorrelateRemoteHandle.assembleRemoteObj(dto, remote);
            } else {
                throw new UtilException("select method is not found");
            }
        }
        return dto;
    }

    /**
     * 子数据映射关联 | 查询（批量）
     *
     * @param dtoList  数据对象集合
     * @param relation 从属关联关系定义对象
     * @return 数据对象集合
     */
    private static <D extends BaseEntity, Correlate extends BaseCorrelate> List<D> subCorrelates(List<D> dtoList, Correlate relation) {
        if (proofCorrelation(dtoList, relation, CorrelateConstants.SubOperate.SELECT)) {
            if (relation instanceof Direct direct) {
                CorrelateDirectHandle.assembleDirectList(dtoList, direct);
            } else if (relation instanceof Indirect indirect) {
                CorrelateIndirectHandle.assembleIndirectList(dtoList, indirect);
            } else if (relation instanceof Remote remote) {
                CorrelateRemoteHandle.assembleRemoteList(dtoList, remote);
            } else {
                throw new UtilException("select method is not found");
            }
        }
        return dtoList;
    }

    /**
     * 子数据映射关联 | 新增
     *
     * @param dto      数据对象
     * @param relation 从属关联关系定义对象
     * @return 结果
     */
    private static <D extends BaseEntity, Correlate extends BaseCorrelate> int addCorrelates(D dto, Correlate relation) {
        if (proofCorrelation(dto, relation, CorrelateConstants.SubOperate.ADD)) {
            if (relation instanceof Direct direct) {
                return CorrelateDirectHandle.insertDirectObj(dto, direct);
            } else if (relation instanceof Indirect indirect) {
                return CorrelateIndirectHandle.insertIndirectObj(dto, indirect);
            } else {
                throw new UtilException("select method is not found");
            }
        }
        return NumberUtil.Zero;
    }

    /**
     * 子数据映射关联 | 新增（批量）
     *
     * @param dtoList  数据对象集合
     * @param relation 从属关联关系定义对象
     * @return 结果
     */
    private static <D extends BaseEntity, Correlate extends BaseCorrelate> int addCorrelates(Collection<D> dtoList, Correlate relation) {
        if (proofCorrelation(dtoList, relation, CorrelateConstants.SubOperate.ADD)) {
            if (relation instanceof Direct direct) {
                return CorrelateDirectHandle.insertDirectList(dtoList, direct);
            } else if (relation instanceof Indirect indirect) {
                return CorrelateIndirectHandle.insertIndirectList(dtoList, indirect);
            } else {
                throw new UtilException("select method is not found");
            }
        }
        return NumberUtil.Zero;
    }

    /**
     * 子数据映射关联 | 修改
     *
     * @param originDto 源数据对象
     * @param newDto    新数据对象
     * @param relation  从属关联关系定义对象
     */
    private static <D extends BaseEntity, Correlate extends BaseCorrelate> int editCorrelates(D originDto, D newDto, Correlate relation) {
        if (proofCorrelation(originDto, newDto, null, null, relation, CorrelateConstants.SubOperate.EDIT)) {
            if (relation instanceof Direct direct) {
                return CorrelateDirectHandle.updateDirectObj(originDto, newDto, direct);
            } else if (relation instanceof Indirect indirect) {
                return CorrelateIndirectHandle.updateIndirectObj(originDto, newDto, indirect);
            } else {
                throw new UtilException("select method is not found");
            }
        }
        return NumberUtil.Zero;
    }

    /**
     * 子数据映射关联 | 修改（批量）
     *
     * @param originList 源数据对象集合
     * @param newList    新数据对象集合
     * @param relation   从属关联关系定义对象
     */
    private static <D extends BaseEntity, Correlate extends BaseCorrelate> int editCorrelates(Collection<D> originList, Collection<D> newList, Correlate relation) {
        if (proofCorrelation(null, null, originList, newList, relation, CorrelateConstants.SubOperate.EDIT)) {
            if (relation instanceof Direct direct) {
                return CorrelateDirectHandle.updateDirectList(originList, newList, direct);
            } else if (relation instanceof Indirect indirect) {
                return CorrelateIndirectHandle.updateIndirectList(originList, newList, indirect);
            } else {
                throw new UtilException("select method is not found");
            }
        }
        return NumberUtil.Zero;
    }

    /**
     * 子数据映射关联 | 删除
     *
     * @param dto      数据对象
     * @param relation 从属关联关系定义对象
     */
    private static <D extends BaseEntity, Correlate extends BaseCorrelate> int delCorrelates(D dto, Correlate relation) {
        if (proofCorrelation(dto, relation, CorrelateConstants.SubOperate.DELETE)) {
            if (relation instanceof Direct direct) {
                return CorrelateDirectHandle.deleteDirectObj(dto, direct);
            } else if (relation instanceof Indirect indirect) {
                return CorrelateIndirectHandle.deleteIndirectObj(dto, indirect);
            } else {
                throw new UtilException("select method is not found");
            }
        }
        return NumberUtil.Zero;
    }

    /**
     * 子数据映射关联 | 删除（批量）
     *
     * @param dtoList  数据对象集合
     * @param relation 从属关联关系定义对象
     */
    private static <D extends BaseEntity, Correlate extends BaseCorrelate> int delCorrelates(Collection<D> dtoList, Correlate relation) {
        if (proofCorrelation(dtoList, relation, CorrelateConstants.SubOperate.DELETE)) {
            if (relation instanceof Direct direct) {
                return CorrelateDirectHandle.deleteDirectList(dtoList, direct);
            } else if (relation instanceof Indirect indirect) {
                return CorrelateIndirectHandle.deleteIndirectList(dtoList, indirect);
            } else {
                throw new UtilException("select method is not found");
            }
        }
        return NumberUtil.Zero;
    }

    /**
     * 校验关联配置
     *
     * @param relation 从属关联关系定义对象
     */
    private static <D extends BaseEntity, Correlate extends BaseCorrelate> Boolean proofCorrelation(D dto, Correlate relation, CorrelateConstants.SubOperate operate) {
        return switch (operate) {
            case SELECT, DELETE -> proofCorrelation(dto, null, null, null, relation, operate);
            case ADD -> proofCorrelation(null, dto, null, null, relation, operate);
            case EDIT -> throw new UtilException("无法到达的分支！");
        };
    }

    /**
     * 校验关联配置
     *
     * @param relation 从属关联关系定义对象
     */
    private static <D extends BaseEntity, Correlate extends BaseCorrelate> Boolean proofCorrelation(Collection<D> dtoList, Correlate relation, CorrelateConstants.SubOperate operate) {
        return switch (operate) {
            case SELECT, DELETE -> proofCorrelation(null, null, dtoList, null, relation, operate);
            case ADD -> proofCorrelation(null, null, null, dtoList, relation, operate);
            case EDIT -> throw new UtilException("无法到达的分支！");
        };
    }

    /**
     * 校验关联配置
     *
     * @param relation 从属关联关系定义对象
     */
    private static <D extends BaseEntity, Correlate extends BaseCorrelate> Boolean proofCorrelation(D originDto, D newDto, Collection<D> originList, Collection<D> newList, Correlate relation, CorrelateConstants.SubOperate operate) {
        if (ObjectUtil.isNull(relation)) {
            return Boolean.FALSE;
        }

        // 校验是否已进行过数据校验
        if (ObjectUtil.equals(Boolean.FALSE, relation.getIsPassed())) {
            // 校验数据字段是否合规 - 如果未通过，则代表人为配置出错，检查代码
            checkOperateLegal(relation, operate);
        }

        switch (operate) {
            case SELECT, DELETE -> {
                if ((ObjectUtil.isNull(originDto) && CollUtil.isEmpty(originList))) {
                    return Boolean.FALSE;
                }
            }
            case ADD -> {
                if ((ObjectUtil.isNull(newDto) && CollUtil.isEmpty(newList))) {
                    return Boolean.FALSE;
                }
            }
            case EDIT -> {
                if (ObjectUtil.isNull(originDto) && ObjectUtil.isNull(newDto) && CollUtil.isEmpty(originList) && CollUtil.isEmpty(newList)) {
                    return Boolean.FALSE;
                }
                if ((ObjectUtil.isNull(originDto) && ObjectUtil.isNotNull(newDto)) || (ObjectUtil.isNotNull(originDto) && ObjectUtil.isNull(newDto))) {
                    throw new UtilException("update must be originDto and newDto all exist");
                }
                if ((CollUtil.isEmpty(originList) && CollUtil.isNotEmpty(newList)) || (CollUtil.isNotEmpty(originList) && CollUtil.isEmpty(newList))) {
                    throw new UtilException("update must be originList and newList all exist");
                } else if (CollUtil.isNotEmpty(originList) && CollUtil.isNotEmpty(newList) && ObjectUtil.notEqual(originList.size(), newList.size())) {
                    throw new UtilException("update must be originList and newList all exist and size be equal");
                }
            }
        }

        return Boolean.TRUE;
    }

    /**
     * 校验数据字段是否合规
     *
     * @param correlate 从属关联关系定义对象
     * @param operate   操作类型
     */
    private static <Correlate extends BaseCorrelate> void checkOperateLegal(Correlate correlate, CorrelateConstants.SubOperate operate) {
        if (ObjectUtil.notEqual(operate, correlate.getOperateType())) {
            logReturn(StrUtil.format("groupName: {}, operateType must be equal", correlate.getGroupName()));
        }
        checkORMLegal(correlate, correlate.getOrm());
    }

    /**
     * 校验关联映射是否合规
     *
     * @param correlate 从属关联关系定义对象
     * @param orm       关联映射执行对象
     */
    private static <Correlate extends BaseCorrelate, ORM extends BaseCorrelate.ORM> void checkORMLegal(Correlate correlate, ORM orm) {
        // 核心数据空校验
        if (ObjectUtil.isNull(orm)) {
            logReturn(StrUtil.format("groupName: {}, orm can not be null", correlate.getGroupName()));
        } else if (ObjectUtil.isNull(orm.getMainKeyField()) || (ClassUtil.isNotSimpleType(orm.getMainKeyField().getType()) && ClassUtil.isNotCollection(orm.getMainKeyField().getType()))) {
            logReturn(StrUtil.format("groupName: {}, mainKeyField can not be null or not be (basicType | collection)", correlate.getGroupName()));
        } else if (ObjectUtil.isNull(orm.getSlaveKeyField()) || ClassUtil.isNotSimpleType(orm.getSlaveKeyField().getType())) {
            logReturn(StrUtil.format("groupName: {}, slaveKeyField can not be null or not BasicType", correlate.getGroupName()));
        }

        if (ObjectUtil.isNotNull(orm.getSubInfoField())) {
            Class<?> subInfoClazz = orm.getSubInfoField().getType();
            if (ClassUtil.isNormalClass(subInfoClazz)) {
                orm.setSubDataRow(CorrelateConstants.DataRow.SINGLE);
            } else if (ClassUtil.isCollection(subInfoClazz)) {
                orm.setSubDataRow(CorrelateConstants.DataRow.LIST);
            } else {
                logReturn(StrUtil.format("groupName: {}, subInfoField class not in compliance with regulations, must be NormalClass or Collection", correlate.getGroupName()));
            }
        }

        // 关联数据差异化校验
        if (orm instanceof Direct.ORM ormDirect) {
            if (ObjectUtil.isNull(ormDirect.getSlaveService())) {
                logReturn(StrUtil.format("groupName: {}, slaveService can not be null", correlate.getGroupName()));
            }
            ormDirect.setMergeType(ClassUtil.isBasicType(orm.getMainKeyField().getType()) ? CorrelateConstants.MergeType.DIRECT : CorrelateConstants.MergeType.INDIRECT);
            if (ObjectUtil.isNotNull(ormDirect.getSubDataRow()) && ObjectUtil.equals(CorrelateConstants.DataRow.SINGLE, ormDirect.getSubDataRow()) && ormDirect.getMergeType().isIndirect()) {
                logReturn(StrUtil.format("groupName: {}, subInfoField is single, but mainKeyField is Collection", correlate.getGroupName()));
            }
        } else if (orm instanceof Indirect.ORM ormIndirect) {
            if (ObjectUtil.isNull(ormIndirect.getSlaveService())) {
                logReturn(StrUtil.format("groupName: {}, slaveService can not be null", correlate.getGroupName()));
            } else if (ObjectUtil.isNull(ormIndirect.getMergeMapper())) {
                logReturn(StrUtil.format("groupName: {}, mergeMapper can not be null", correlate.getGroupName()));
            } else if (ObjectUtil.isNull(ormIndirect.getMergeMainField()) || ClassUtil.isNotSimpleType(ormIndirect.getMergeMainField().getType())) {
                logReturn(StrUtil.format("groupName: {}, mergeMainField can not be null or not BasicType", correlate.getGroupName()));
            } else if (ObjectUtil.isNull(ormIndirect.getMergeSlaveField()) || ClassUtil.isNotSimpleType(ormIndirect.getMergeSlaveField().getType())) {
                logReturn(StrUtil.format("groupName: {}, mergeSlaveField can not be null or not BasicType", correlate.getGroupName()));
            } else if (ObjectUtil.isNull(ormIndirect.getSubKeyField())) {
                logReturn(StrUtil.format("groupName: {}, subKeyField can not be null", correlate.getGroupName()));
            } else if (ObjectUtil.isNull(ormIndirect.getSubInfoField())) {
                logReturn(StrUtil.format("groupName: {}, subInfoField can not be null", correlate.getGroupName()));
            } else if (ClassUtil.isNotSimpleType(ormIndirect.getMainKeyField().getType())) {
                logReturn(StrUtil.format("groupName: {}, mainKeyField must be basicType", correlate.getGroupName()));
            }
            ormIndirect.setMergeMainSqlName(CorrelateUtil.getFieldSqlName(ormIndirect.getMergeMainField()));
            ormIndirect.setMergeSlaveSqlName(CorrelateUtil.getFieldSqlName(ormIndirect.getMergeSlaveField()));

            // 从数据关联校验
            if (ObjectUtil.isNotNull(ormIndirect.getSubKeyField())) {
                Class<?> subKeyClazz = ormIndirect.getSubKeyField().getType();
                CorrelateConstants.DataRow subKey = null;
                if (ClassUtil.isBasicType(subKeyClazz)) {
                    subKey = CorrelateConstants.DataRow.SINGLE;
                } else if (ClassUtil.isCollection(subKeyClazz)) {
                    subKey = CorrelateConstants.DataRow.LIST;
                } else {
                    logReturn(StrUtil.format("groupName: {}, subKeyField class not in compliance with regulations, must be Collection or Primitive", correlate.getGroupName()));
                }
                if (ObjectUtil.isNotNull(ormIndirect.getSubDataRow())) {
                    if (ObjectUtil.notEqual(ormIndirect.getSubDataRow(), subKey)) {
                        logReturn(StrUtil.format("groupName: {}, subKeyField and subInfoField dataRow not equal", correlate.getGroupName()));
                    }
                } else {
                    ormIndirect.setSubDataRow(subKey);
                }
            }
        } else if (orm instanceof Remote.ORM ormRemote) {
            if (ObjectUtil.isNull(ormRemote.getRemoteService())) {
                logReturn(StrUtil.format("groupName: {}, remoteService can not be null", correlate.getGroupName()));
            }
            ormRemote.setMergeType(ClassUtil.isBasicType(orm.getMainKeyField().getType()) ? CorrelateConstants.MergeType.DIRECT : CorrelateConstants.MergeType.INDIRECT);
            if (ObjectUtil.isNotNull(ormRemote.getSubDataRow()) && ObjectUtil.equals(CorrelateConstants.DataRow.SINGLE, ormRemote.getSubDataRow()) && ormRemote.getMergeType().isIndirect()) {
                logReturn(StrUtil.format("groupName: {}, subInfoField is single, but mainKeyField is Collection", correlate.getGroupName()));
            }
        }

        // 数据初始化
        orm.setMainKeySqlName(CorrelateUtil.getFieldSqlName(orm.getMainKeyField()));
        orm.setSlaveKeySqlName(CorrelateUtil.getFieldSqlName(orm.getSlaveKeyField()));

//        // 操作类型校验
//        switch (correlate.getOperateType()) {
//            case SELECT -> {
//                if (orm instanceof Direct.ORM ormDirect) {
//                    if (ObjectUtil.isNull(orm.getSubInfoField())) {
//                        logReturn(StrUtil.format("groupName: {}, subInfoField can not be null", correlate.getGroupName()));
//                    }
//                }else if(orm instanceof Indirect.ORM ormIndirect){
//
//                }
//            }
//            case ADD -> {
//
//            }
//            case EDIT -> {
//
//            }
//            case DELETE -> {
//
//            }
//        }
    }

    /**
     * 校验数据字段是否合规 | 自循环
     *
     * @param correlate 从属关联关系定义对象
     * @param index     序列行
     */
    private static <Correlate extends BaseCorrelate> void checkOperateLegal(Correlate correlate, String groupName, AtomicInteger index) {
        correlate.setGroupName(StrUtil.format("{}-{}", groupName, index.getAndIncrement()));
        // 校验数据字段是否合规 - 如果未通过，则代表人为配置出错，检查代码
        checkOperateLegal(correlate, correlate.getOperateType());
        if (CollUtil.isNotEmpty(correlate.getRelations())) {
            correlate.getRelations().forEach(item -> checkOperateLegal((Correlate) item, groupName, index));
        }
        correlate.setIsPassed(Boolean.TRUE);
    }

    /**
     * 输出错误日志
     *
     * @param msg 日志信息
     */
    private static void logReturn(String msg) {
        throw new UtilException(msg);
    }

    /**
     * 记录从属关联关系
     *
     * @param correlateEnum 关联枚举
     */
    public static <Correlate extends BaseCorrelate> void startCorrelates(Enum<? extends Enum<?>> correlateEnum) {
        String cacheName = RedisConstants.CacheKey.SYS_CORRELATE_KEY.getCacheName(correlateEnum.getClass().getName(), correlateEnum.name());
        List<Correlate> correlates = getRedisService().getCacheObject(cacheName);
        if (ObjectUtil.isNull(correlates)) {
            if (ClassUtil.isNotAssignable(CorrelateService.class, correlateEnum.getDeclaringClass())) {
                throw new UtilException("枚举必须为CorrelateService子类");
            }
            Map<String, Object> correlatesMap = EnumUtil.getNameFieldMap(correlateEnum.getDeclaringClass(), "correlates");
            if (MapUtil.isEmpty(correlatesMap)) {
                throw new UtilException("枚举必须存在correlates属性!");
            }
            Object obj = correlatesMap.get(correlateEnum.name());
            if (ObjectUtil.isNull(obj)) {
                throw new UtilException("枚举correlate属性不能为null!");
            } else if (obj instanceof List<?> correlateList) {
                correlates = (List<Correlate>) correlateList;
            } else {
                throw new UtilException("未匹配到指定枚举值!");
            }
            AtomicInteger index = new AtomicInteger(NumberUtil.Zero);
            correlates.forEach(item -> checkOperateLegal(item, correlateEnum.name(), index));
        }
        startCorrelates(correlates);
    }

    /**
     * 记录从属关联关系
     *
     * @param relations 从属关联关系定义对象集合
     */
    public static <Correlate extends BaseCorrelate> void startCorrelates(List<Correlate> relations) {
        if (CollUtil.isEmpty(relations)) {
            return;
        }
        Deque<List<? extends BaseCorrelate>> deque = THREAD_LOCAL.get();
        if (deque == null) {
            deque = new ArrayDeque<>();
        }
        deque.add(relations);
        THREAD_LOCAL.set(deque);
    }

    /**
     * 获取从属关联关系
     *
     * @return 从属关联关系定义对象集合
     */
    public static <Correlate extends BaseCorrelate> List<Correlate> getCorrelates() {
        List<Correlate> relations = (List<Correlate>) Optional.ofNullable(THREAD_LOCAL.get()).map(Deque::poll).orElse(new ArrayList<>());
        return CollUtil.isNotEmpty(relations) ? relations : new ArrayList<>();
    }

    /**
     * 清除从属关联关系
     */
    public static void clearCorrelates() {
        THREAD_LOCAL.remove();
    }

    /**
     * 获取实体类的字段对象
     *
     * @param fieldFun 字段SFunction方法
     * @return 字段对象
     */
    public static <T> Class getClass(SFunction<T, ?> fieldFun) {
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

        try {
            return Class.forName(serializedLambda.getImplClass().replace(StrUtil.SLASH, StrUtil.DOT));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取实体类的字段对象
     *
     * @param fieldFun 字段SFunction方法
     * @return 字段对象
     */
    public static <T> Field getField(SFunction<T, ?> fieldFun) {
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
        try {
            Field field = Class.forName(serializedLambda.getImplClass().replace(StrUtil.SLASH, StrUtil.DOT)).getDeclaredField(fieldName);
            field.setAccessible(Boolean.TRUE);
            return field;
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取字段数据库名称
     *
     * @param fieldFun 字段SFunction方法
     * @return 数据库名称
     */
    public static <T> String getFieldSqlName(SFunction<T, ?> fieldFun) {
        Field field = getField(fieldFun);
        return getFieldSqlName(field);
    }

    /**
     * 获取字段数据库名称
     *
     * @param field 字段对象
     * @return 数据库名称
     */
    public static String getFieldSqlName(Field field) {
        if (field.isAnnotationPresent(TableId.class)) {
            TableId idField = field.getAnnotation(TableId.class);
            return ObjectUtil.isNotNull(idField) && StrUtil.isNotEmpty(idField.value())
                    ? idField.value()
                    : StrUtil.toUnderlineCase(field.getName());
        } else if (field.isAnnotationPresent(TableField.class)) {
            TableField tableField = field.getAnnotation(TableField.class);
            return StrUtil.isNotEmpty(tableField.value())
                    ? tableField.value()
                    : StrUtil.toUnderlineCase(field.getName());
        } else {
            return StrUtil.toUnderlineCase(field.getName());
        }
    }
}