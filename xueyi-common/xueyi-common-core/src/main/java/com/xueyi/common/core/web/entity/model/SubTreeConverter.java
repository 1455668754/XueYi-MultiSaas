package com.xueyi.common.core.web.entity.model;

import com.xueyi.common.core.web.entity.base.TreeEntity;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

/**
 * SubTree 基类 对象映射器
 *
 * @param <Q> Query
 * @param <D> Dto
 * @param <P> Po
 * @author xueyi
 */
public interface SubTreeConverter<Q extends P, D extends P, P extends TreeEntity<D>> extends TreeConverter<Q, D, P> {

    @Mapping(target = "subList", ignore = true)
    @Mapping(target = "children", ignore = true)
    D mapperDto(P po);

    @Mapping(target = "subList", ignore = true)
    @Mapping(target = "children", ignore = true)
    List<D> mapperDto(Collection<P> poList);

    @Mapping(target = "subList", ignore = true)
    @Mapping(target = "children", ignore = true)
    P mapperPo(D po);

    @Mapping(target = "subList", ignore = true)
    @Mapping(target = "children", ignore = true)
    List<P> mapperPo(Collection<D> poList);
}
