package com.xueyi.common.web.entity.manager.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.web.entity.model.BaseConverter;
import com.xueyi.common.web.entity.manager.IBaseManager;
import com.xueyi.common.web.entity.manager.impl.handle.BaseHandleManager;
import com.xueyi.common.web.entity.mapper.BaseMapper;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 数据封装层处理 基类通用数据处理
 *
 * @param <Q>  Query
 * @param <D>  Dto
 * @param <P>  Po
 * @param <PM> PoMapper
 * @author xueyi
 */
public class BaseManager<Q extends P, D extends P, P extends BaseEntity, PM extends BaseMapper<Q, D, P>, CT extends BaseConverter<Q, D, P>> extends BaseHandleManager<Q, D, P, PM, CT> implements IBaseManager<Q, D> {

    /**
     * 查询数据对象列表
     *
     * @param query 数据查询对象
     * @return 数据对象集合
     */
    @Override
    public List<D> selectList(Q query) {
        LambdaQueryWrapper<P> queryWrapper = new LambdaQueryWrapper<>(query);
        SelectListQuery(BaseConstants.SelectType.NORMAL, queryWrapper, query);
        List<P> poList = baseMapper.selectList(queryWrapper);
        return mapperDto(poList);
    }

    /**
     * 查询数据对象列表 | 附加数据
     *
     * @param query 数据查询对象
     * @return 数据对象集合
     */
    @Override
    public List<D> selectListExtra(Q query) {
        LambdaQueryWrapper<P> queryWrapper = new LambdaQueryWrapper<>(query);
        SelectListQuery(BaseConstants.SelectType.EXTRA, queryWrapper, query);
        List<P> poList = baseMapper.selectList(queryWrapper);
        return mapperDto(poList);
    }

    /**
     * 根据Id集合查询数据对象列表
     *
     * @param idList Id集合
     * @return 数据对象集合
     */
    @Override
    public List<D> selectListByIds(Collection<? extends Serializable> idList) {
        List<P> poList = baseMapper.selectBatchIds(idList);
        return mapperDto(poList);
    }

    /**
     * 根据Id查询单条数据对象
     *
     * @param id Id
     * @return 数据对象
     */
    @Override
    public D selectById(Serializable id) {
        P po = baseMapper.selectById(id);
        return mapperDto(po);
    }

    /**
     * 根据Id查询单条数据对象 | 附加数据
     *
     * @param id Id
     * @return 数据对象
     */
    @Override
    public D selectByIdExtra(Serializable id) {
        P po = baseMapper.selectById(id);
        return mapperDto(po);
    }

    /**
     * 新增数据对象
     *
     * @param d 数据对象
     * @return 结果
     */
    @Override
    public int insert(D d) {
        return baseMapper.insert(d);
    }

    /**
     * 新增数据对象（批量）
     *
     * @param entityList 数据对象集合
     * @return 结果
     */
    @Override
    @DSTransactional
    public int insertBatch(Collection<D> entityList) {
        return baseMapper.insertBatch(mapperPo(entityList));
    }

    /**
     * 修改数据对象
     *
     * @param d 数据对象
     * @return 结果
     */
    @Override
    public int update(D d) {
        return baseMapper.updateById(d);
    }

    /**
     * 修改数据对象（批量）
     *
     * @param entityList 数据对象集合
     * @return 结果
     */
    @Override
    @DSTransactional
    public int updateBatch(Collection<D> entityList) {
        return baseMapper.updateBatch(mapperPo(entityList));
    }

    /**
     * 修改状态
     *
     * @param id     Id
     * @param status 状态
     * @return 结果
     */
    @Override
    public int updateStatus(Serializable id, String status) {
        return baseMapper.update(null,
                Wrappers.<P>update().lambda()
                        .set(P::getStatus, status)
                        .eq(P::getId, id));
    }

    /**
     * 根据Id删除数据对象
     *
     * @param id Id
     * @return 结果
     */
    @Override
    public int deleteById(Serializable id) {
        return baseMapper.deleteById(id);
    }

    /**
     * 根据Id集合批量删除数据对象
     *
     * @param idList Id集合
     * @return 结果
     */
    @Override
    public int deleteByIds(Collection<? extends Serializable> idList) {
        return baseMapper.deleteBatchIds(idList);
    }

    /**
     * 校验名称是否唯一
     *
     * @param id   Id
     * @param name 名称
     * @return 数据对象
     */
    @Override
    public D checkNameUnique(Serializable id, String name) {
        P po = baseMapper.selectOne(
                Wrappers.<P>query().lambda()
                        .ne(P::getId, id)
                        .eq(P::getName, name)
                        .last(SqlConstants.LIMIT_ONE));
        return mapperDto(po);
    }
}
