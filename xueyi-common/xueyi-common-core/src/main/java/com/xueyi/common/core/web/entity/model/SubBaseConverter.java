package com.xueyi.common.core.web.entity.model;

import com.xueyi.common.core.web.entity.base.BaseEntity;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

/**
 * SubBase 基类 对象映射器
 *
 * @param <Q> Query
 * @param <D> Dto
 * @param <P> Po
 * @author xueyi
 */
public interface SubBaseConverter<Q extends P, D extends P, P extends BaseEntity> extends BaseConverter<Q, D, P> {

    @Mapping(target = "subList", ignore = true)
    D mapperDto(P po);

    @Mapping(target = "subList", ignore = true)
    List<D> mapperDto(Collection<P> poList);

    @Mapping(target = "subList", ignore = true)
    P mapperPo(D po);

    @Mapping(target = "subList", ignore = true)
    List<P> mapperPo(Collection<D> poList);
}
