package com.xueyi.common.web.entity.service;

import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.web.entity.base.SubTreeEntity;

/**
 * 服务层 主子树型通用数据处理
 *
 * @param <Q>  Query
 * @param <D>  Dto
 * @param <SQ> SubQuery
 * @param <SD> SubDto
 * @author xueyi
 */
public interface ISubTreeService<Q extends SubTreeEntity<D, SD>, D extends SubTreeEntity<D, SD>, SQ extends BaseEntity, SD extends BaseEntity> extends ITreeService<Q, D>, ISubService<Q, D, SQ, SD> {
}
