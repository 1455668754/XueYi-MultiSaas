package com.xueyi.common.web.entity.manager;

import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.web.entity.base.SubBaseEntity;

/**
 * 数据封装层 主子基类通用数据处理
 *
 * @param <Q>  Query
 * @param <D>  Dto
 * @param <SQ> SubQuery
 * @param <SD> SubDto
 * @author xueyi
 */
public interface ISubBaseManager<Q extends SubBaseEntity<SD>, D extends SubBaseEntity<SD>, SQ extends BaseEntity, SD extends BaseEntity> extends ISubManager<Q, D, SQ, SD>, IBaseManager<Q, D> {
}
