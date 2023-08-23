package com.xueyi.common.web.entity.service.impl.handle;

import com.xueyi.common.core.utils.core.ArrayUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.web.correlate.contant.CorrelateConstants;
import com.xueyi.common.web.correlate.service.CorrelateService;
import com.xueyi.common.web.correlate.utils.CorrelateUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务层 操作方法 基类实现通用关联处理
 *
 * @param <D> Dto
 * @param <C> Correlate
 * @author xueyi
 */
public abstract class BaseCorrelateHandle<D extends BaseEntity, C extends Enum<? extends Enum<?>> & CorrelateService> {

    /**
     * 设置请求关联映射
     */
    @SafeVarargs
    protected final void startCorrelates(C... correlates) {
        if (ArrayUtil.isNotEmpty(correlates)) {
            Arrays.stream(correlates).filter(ObjectUtil::isNotNull).forEach(CorrelateUtil::startCorrelates);
        }
    }

    /**
     * 清理关联映射的线程变量
     */
    protected final void clearCorrelates() {
        CorrelateUtil.clearCorrelates();
    }

    /**
     * 数据映射关联 | 查询
     *
     * @param dto 数据对象
     * @return 数据对象
     */
    @SafeVarargs
    protected final D subCorrelates(D dto, C... correlates) {
        startCorrelates(correlates);
        return CorrelateUtil.subCorrelates(dto);
    }

    /**
     * 数据映射关联 | 查询（批量）
     *
     * @param dtoList 数据对象集合
     * @return 数据对象集合
     */
    @SafeVarargs
    protected final <Coll extends Collection<D>> Coll subCorrelates(Coll dtoList, C... correlates) {
        startCorrelates(correlates);
        return CorrelateUtil.subCorrelates(dtoList);
    }

    /**
     * 数据映射关联 | 新增
     *
     * @param dto 数据对象
     * @return 结果
     */
    @SafeVarargs
    protected final int addCorrelates(D dto, C... correlates) {
        startCorrelates(correlates);
        return CorrelateUtil.addCorrelates(dto);
    }

    /**
     * 数据映射关联 | 新增（批量）
     *
     * @param dtoList 数据对象集合
     * @return 结果
     */
    @SafeVarargs
    protected final <Coll extends Collection<D>> int addCorrelates(Coll dtoList, C... correlates) {
        startCorrelates(correlates);
        return CorrelateUtil.addCorrelates(dtoList);
    }

    /**
     * 数据映射关联 | 修改
     *
     * @param newDto 新数据对象
     * @return 结果
     */
    @SafeVarargs
    protected final int editCorrelates(D newDto, C... correlates) {
        startCorrelates(correlates);
        return CorrelateUtil.editCorrelates(newDto);
    }

    /**
     * 数据映射关联 | 修改
     *
     * @param originDto 源数据对象
     * @param newDto    新数据对象
     * @return 结果
     */
    @SafeVarargs
    protected final int editCorrelates(D originDto, D newDto, C... correlates) {
        subCorrelates(originDto, correlates);
        startCorrelates(correlates);
        return CorrelateUtil.editCorrelates(originDto, newDto);
    }

    /**
     * 数据映射关联 | 修改（批量）
     *
     * @param newList 新数据对象集合
     * @return 结果
     */
    @SafeVarargs
    protected final <Coll extends Collection<D>> int editCorrelates(Coll newList, C... correlates) {
        startCorrelates(correlates);
        return CorrelateUtil.editCorrelates(newList);
    }

    /**
     * 数据映射关联 | 修改（批量）
     *
     * @param originList 源数据对象集合
     * @param newList    新数据对象集合
     * @return 结果
     */
    @SafeVarargs
    protected final <Coll extends Collection<D>> int editCorrelates(Coll originList, Coll newList, C... correlates) {
        subCorrelates(originList, correlates);
        startCorrelates(correlates);
        return CorrelateUtil.editCorrelates(originList, newList);
    }

    /**
     * 数据映射关联 | 删除
     *
     * @param dto 数据对象
     * @return 结果
     */
    @SafeVarargs
    protected final int delCorrelates(D dto, C... correlates) {
        startCorrelates(correlates);
        return CorrelateUtil.delCorrelates(dto);
    }

    /**
     * 数据映射关联 | 删除（批量）
     *
     * @param dtoList 数据对象集合
     * @return 结果
     */
    @SafeVarargs
    protected final <Coll extends Collection<D>> int delCorrelates(Coll dtoList, C... correlates) {
        startCorrelates(correlates);
        return CorrelateUtil.delCorrelates(dtoList);
    }

    /**
     * 默认方法关联配置定义
     */
    protected Map<CorrelateConstants.ServiceType, C> defaultCorrelate() {
        return new HashMap<>();
    }

    /**
     * 获取操作类型默认关联控制
     *
     * @param serviceType 操作类型
     * @return 默认关联控制
     */
    abstract C getBasicCorrelate(CorrelateConstants.ServiceType serviceType);
}
