package com.xueyi.common.core.web.entity.model;

import com.xueyi.common.core.web.entity.base.BaseEntity;

import java.util.Collection;
import java.util.List;

/**
 * Base 基类 对象映射器
 *
 * @param <Q> Query
 * @param <D> Dto
 * @param <P> Po
 * @author xueyi
 */
public interface BaseConverter<Q extends P, D extends P, P extends BaseEntity> {

    D mapperDto(P po);

    List<D> mapperDto(Collection<P> poList);


    P mapperPo(D dto);

    List<P> mapperPo(Collection<D> dtoList);
}
