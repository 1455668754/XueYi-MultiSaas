package com.xueyi.common.web.entity.manager;

import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.web.entity.base.SubTreeEntity;

/**
 * 数据封装层 主子树型通用数据处理
 *
 * @param <Q>  Query
 * @param <D>  Dto
 * @param <SQ> SubQuery
 * @param <SD> SubDto
 * @author xueyi
 */
public interface ISubTreeManager<Q extends SubTreeEntity<D, SD>, D extends SubTreeEntity<D, SD>, SQ extends BaseEntity, SD extends BaseEntity> extends ISubManager<Q, D, SQ, SD>, ITreeManager<Q, D> {
}
