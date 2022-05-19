package com.xueyi.common.web.entity.mapper;

import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.web.entity.base.SubTreeEntity;

/**
 * 数据层 主子树型通用数据处理
 *
 * @param <Q>  Query
 * @param <D>  Dto
 * @param <P>  Po
 * @param <SQ> SubQuery
 * @param <SD> SubDto
 * @param <SP> SubPo
 * @author xueyi
 */
public interface SubTreeMapper<Q extends P, D extends P, P extends SubTreeEntity<D, SD>, SQ extends SP, SD extends SP, SP extends BaseEntity> extends TreeMapper<Q, D, P> {
}
