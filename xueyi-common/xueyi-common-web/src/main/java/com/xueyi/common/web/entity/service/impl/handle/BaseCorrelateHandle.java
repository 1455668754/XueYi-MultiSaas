package com.xueyi.common.web.entity.service.impl.handle;

import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.web.correlate.service.CorrelateService;
import com.xueyi.common.web.correlate.utils.CorrelateUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务层 操作方法 基类实现通用关联处理
 *
 * @param <D> Dto
 * @param <C> Correlate
 * @author xueyi
 */
public interface BaseCorrelateHandle<D extends BaseEntity, C extends Enum<? extends Enum<?>> & CorrelateService> {

    /**
     * 默认方法关联配置定义
     */
    default Map<OperateConstants.ServiceType, C> getBasicCorrelate() {
        return new HashMap<>();
    }

    /**
     * 设置请求关联映射
     */
    default void startCorrelates(C correlateEnum) {
        CorrelateUtil.startCorrelates(correlateEnum);
    }

    /**
     * 清理关联映射的线程变量
     */
    default void clearCorrelates() {
        CorrelateUtil.clearCorrelates();
    }

    /**
     * 数据映射关联 | 查询
     *
     * @param dto 数据对象
     * @return 数据对象
     */
    default D subCorrelates(D dto) {
        return CorrelateUtil.subCorrelates(dto);
    }

    /**
     * 数据映射关联 | 查询（批量）
     *
     * @param dtoList 数据对象集合
     * @return 数据对象集合
     */
    default List<D> subCorrelates(List<D> dtoList) {
        return CorrelateUtil.subCorrelates(dtoList);
    }

    /**
     * 数据映射关联 | 新增
     *
     * @param dto 数据对象
     * @return 结果
     */
    default int addCorrelates(D dto) {
        return CorrelateUtil.addCorrelates(dto);
    }

    /**
     * 数据映射关联 | 新增（批量）
     *
     * @param dtoList 数据对象集合
     * @return 结果
     */
    default int addCorrelates(Collection<D> dtoList) {
        return CorrelateUtil.addCorrelates(dtoList);
    }

    /**
     * 数据映射关联 | 修改
     *
     * @param originDto 源数据对象
     * @param newDto    新数据对象
     * @return 结果
     */
    default int editCorrelates(D originDto, D newDto) {
        return CorrelateUtil.editCorrelates(originDto, newDto);
    }

    /**
     * 数据映射关联 | 修改（批量）
     *
     * @param originList 源数据对象集合
     * @param newList    新数据对象集合
     * @return 结果
     */
    default int editCorrelates(Collection<D> originList, Collection<D> newList) {
        return CorrelateUtil.editCorrelates(originList, newList);
    }

    /**
     * 数据映射关联 | 删除
     *
     * @param dto 数据对象
     * @return 结果
     */
    default int delCorrelates(D dto) {
        return CorrelateUtil.delCorrelates(dto);
    }

    /**
     * 数据映射关联 | 删除（批量）
     *
     * @param dtoList 数据对象集合
     * @return 结果
     */
    default int delCorrelates(Collection<D> dtoList) {
        return CorrelateUtil.delCorrelates(dtoList);
    }
}
