package com.xueyi.common.web.entity.mapper;

import com.xueyi.common.core.web.entity.base.BaseEntity;

/**
 * 数据层 基类通用数据处理
 *
 * @param <Q> Query
 * @param <D> Dto
 * @param <P> Po
 * @author xueyi
 */
public interface BaseMapper<Q extends P, D extends P, P extends BaseEntity> extends BasicMapper<P> {
}
