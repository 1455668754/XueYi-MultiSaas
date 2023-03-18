package com.xueyi.common.web.entity.manager.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.web.entity.model.BaseConverter;
import com.xueyi.common.web.entity.domain.SqlField;
import com.xueyi.common.web.entity.manager.IBaseManager;
import com.xueyi.common.web.entity.manager.impl.handle.BaseHandleManagerImpl;
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
public class BaseManagerImpl<Q extends P, D extends P, P extends BaseEntity, PM extends BaseMapper<Q, D, P>, CT extends BaseConverter<Q, D, P>> extends BaseHandleManagerImpl<Q, D, P, PM, CT> implements IBaseManager<Q, D> {

    /**
     * 查询数据对象列表
     *
     * @param query 数据查询对象
     * @return 数据对象集合
     */
    @Override
    public List<D> selectList(Q query) {
        List<P> poList = baseMapper.selectList(selectListQuery(query));
        return subMerge(mapperDto(poList));
    }

    /**
     * 查询数据对象列表 | 组装子数据映射关联
     *
     * @param query 数据查询对象
     * @return 数据对象集合
     */
    @Override
    public List<D> selectListMerge(Q query) {
        return subMerge(selectList(query));
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
        return subMerge(mapperDto(poList), OperateConstants.DataRow.SINGLE);
    }

    /**
     * 根据Id集合查询数据对象列表 | 组装子数据映射关联
     *
     * @param idList Id集合
     * @return 数据对象集合
     */
    @Override
    public List<D> selectListByIdsMerge(Collection<? extends Serializable> idList) {
        return subMerge(selectListByIds(idList), OperateConstants.DataRow.SINGLE);
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
     * 根据Id查询单条数据对象 | 组装子数据映射关联
     *
     * @param id Id
     * @return 数据对象
     */
    @Override
    public D selectByIdMerge(Serializable id){
        return subMerge(selectById(id));
    }

    /**
     * 新增数据对象
     *
     * @param dto 数据对象
     * @return 结果
     */
    @Override
    public int insert(D dto) {
        return baseMapper.insert(dto);
    }

    /**
     * 新增子数据映射关联
     *
     * @param dto 数据对象
     * @return 结果
     */
    @Override
    public int insertMerge(D dto) {
        return addMerge(dto);
    }

    /**
     * 新增数据对象（批量）
     *
     * @param dtoList 数据对象集合
     * @return 结果
     */
    @Override
    @DSTransactional
    @SuppressWarnings("unchecked")
    public int insertBatch(Collection<D> dtoList) {
        return baseMapper.insertBatch((Collection<P>) dtoList);
    }

    /**
     * 新增子数据映射关联（批量）
     *
     * @param dtoList 数据对象集合
     * @return 结果
     */
    @Override
    public int insertMerge(Collection<D> dtoList) {
        return addMerge(dtoList);
    }

    /**
     * 修改数据对象
     *
     * @param dto 数据对象
     * @return 结果
     */
    @Override
    public int update(D dto) {
        return baseMapper.updateById(dto);
    }

    /**
     * 修改子数据映射关联
     *
     * @param originDto 源数据对象
     * @param newDto    新数据对象
     * @return 结果
     */
    @Override
    public int updateMerge(D originDto, D newDto) {
        return editMerge(originDto, newDto);
    }

    /**
     * 修改数据对象（批量）
     *
     * @param dtoList 数据对象集合
     * @return 结果
     */
    @Override
    @DSTransactional
    @SuppressWarnings("unchecked")
    public int updateBatch(Collection<D> dtoList) {
        return baseMapper.updateBatch((Collection<P>) dtoList);
    }

    /**
     * 修改子数据映射关联（批量）
     *
     * @param originList 源数据对象集合
     * @param newList    新数据对象集合
     * @return 结果
     */
    @Override
    public int updateMerge(Collection<D> originList, Collection<D> newList) {
        return editMerge(originList, newList);
    }

    /**
     * 修改状态
     *
     * @param dto 数据对象
     * @return 结果
     */
    @Override
    public int updateStatus(D dto) {
        return baseMapper.update(null,
                Wrappers.<P>update().lambda()
                        .set(P::getStatus, dto.getStatus())
                        .eq(P::getId, dto.getId()));
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
     * 删除子数据映射关联
     *
     * @param dto 数据对象
     * @return 结果
     */
    @Override
    public int deleteMerge(D dto) {
        return delMerge(dto);
    }

    /**
     * 根据Id集合删除数据对象（批量）
     *
     * @param idList Id集合
     * @return 结果
     */
    @Override
    public int deleteByIds(Collection<? extends Serializable> idList) {
        return baseMapper.deleteBatchIds(idList);
    }

    /**
     * 删除子数据映射关联（批量）
     *
     * @param dtoList 数据对象集合
     * @return 结果
     */
    @Override
    public int deleteMerge(Collection<D> dtoList) {
        return delMerge(dtoList);
    }

    /**
     * 根据动态SQL控制对象查询数据对象集合
     *
     * @param field 动态SQL控制对象
     * @return 数据对象集合
     */
    @Override
    public List<D> selectListByField(SqlField... field) {
        return mapperDto(baseMapper.selectListByField(field));
    }

    /**
     * 根据动态SQL控制对象查询数据对象
     *
     * @param field 动态SQL控制对象
     * @return 数据对象
     */
    @Override
    public D selectByField(SqlField... field) {
        return mapperDto(baseMapper.selectByField(field));
    }

    /**
     * 批量插入数据
     *
     * @param fieldColl 数据对象集合
     * @return 结果
     */
    @Override
    public int insertByField(Collection<?> fieldColl) {
        return baseMapper.insertByField(fieldColl);
    }

    /**
     * 批量更新数据
     *
     * @param fieldColl 数据对象集合
     * @return 结果
     */
    @Override
    public int updateByField(Collection<?> fieldColl) {
        return baseMapper.updateByField(fieldColl);
    }

    /**
     * 根据动态SQL控制对象更新数据对象
     *
     * @param field 动态SQL控制对象
     * @return 结果
     */
    @Override
    public int updateByField(SqlField... field) {
        return baseMapper.updateByField(field);
    }

    /**
     * 根据动态SQL控制对象删除数据对象
     *
     * @param field 动态SQL控制对象
     * @return 结果
     */
    @Override
    public int deleteByField(SqlField... field) {
        return baseMapper.deleteByField(field);
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
