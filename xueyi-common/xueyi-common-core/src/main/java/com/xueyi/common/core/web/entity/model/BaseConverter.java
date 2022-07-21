package com.xueyi.common.core.web.entity.model;

import com.github.pagehelper.Page;
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

    /**
     * 类型转换 | 持久化对象 -> 数据传输对象
     *
     * @param po 持久化对象
     * @return dto
     */
    D mapperDto(P po);

    /**
     * 类型转换 | 持久化对象集合 -> 数据传输对象集合
     *
     * @param poList 持久化对象集合
     * @return dtoList
     */
    List<D> mapperDto(Collection<P> poList);

    /**
     * 类型转换 | 持久化对象集合 -> 数据传输分页对象集合
     *
     * @param poList 持久化对象集合
     * @return dtoList
     */
    Page<D> mapperPageDto(Collection<P> poList);

    /**
     * 类型转换 | 数据传输对象 -> 持久化对象
     *
     * @param dto 数据传输对象
     * @return po
     */
    P mapperPo(D dto);

    /**
     * 类型转换 | 数据传输对象集合 -> 持久化对象集合
     *
     * @param dtoList 数据传输对象集合
     * @return poList
     */
    List<P> mapperPo(Collection<D> dtoList);

    /**
     * 类型转换 | 数据传输对象集合 -> 持久化分页对象集合
     *
     * @param dtoList 数据传输对象集合
     * @return poList
     */
    Page<P> mapperPagePo(Collection<D> dtoList);
}
