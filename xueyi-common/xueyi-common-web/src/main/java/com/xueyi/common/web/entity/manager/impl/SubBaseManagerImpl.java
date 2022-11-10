package com.xueyi.common.web.entity.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.web.entity.base.SubBaseEntity;
import com.xueyi.common.core.web.entity.model.BaseConverter;
import com.xueyi.common.web.entity.manager.ISubBaseManager;
import com.xueyi.common.web.entity.manager.impl.handle.SubBaseHandleManagerImpl;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.common.web.entity.mapper.SubBaseMapper;

import java.io.Serializable;
import java.util.List;

/**
 * 数据封装层处理 主子基类通用数据处理
 *
 * @param <Q>   Query
 * @param <D>   Dto
 * @param <P>   Po
 * @param <PM>  PoMapper
 * @param <SQ>  SubQuery
 * @param <SD>  SubDto
 * @param <SP>  SubPo
 * @param <SPM> SubPoMapper
 * @author xueyi
 */
public abstract class SubBaseManagerImpl<Q extends P, D extends P, P extends SubBaseEntity<SD>, PM extends SubBaseMapper<Q, D, P, SQ, SD, SP>, CT extends BaseConverter<Q, D, P>, SQ extends SP, SD extends SP, SP extends BaseEntity, SPM extends BaseMapper<SQ, SD, SP>, SCT extends BaseConverter<SQ, SD, SP>> extends SubBaseHandleManagerImpl<Q, D, P, PM, CT, SQ, SD, SP, SPM, SCT> implements ISubBaseManager<Q, D, SQ, SD> {

    /**
     * 根据Id查询单条数据对象 | 包含子数据
     *
     * @param query 数据查询对象
     * @return 数据对象集合
     */
    // 待优化 联表更新后
    @Override
    public List<D> selectListExtra(Q query) {
        LambdaQueryWrapper<P> queryWrapper = new LambdaQueryWrapper<>(query);
        SelectListQuery(BaseConstants.SelectType.EXTRA, queryWrapper, query);
        List<P> poList = baseMapper.selectList(queryWrapper);
        List<D> dtoList = mapperDto(poList);
        dtoList.forEach(item -> {
            LambdaQueryWrapper<SP> subQueryWrapper = new LambdaQueryWrapper<>();
            querySetForeignKey(subQueryWrapper, item);
            item.setSubList(subConverter.mapperDto(subMapper.selectList(subQueryWrapper)));
        });
        return dtoList;
    }

    /**
     * 根据Id查询单条数据对象 | 包含子数据
     *
     * @param id Id
     * @return 数据对象
     */
    @Override
    public D selectByIdExtra(Serializable id) {
        P po = baseMapper.selectById(id);
        D dto = mapperDto(po);
        LambdaQueryWrapper<SP> subQueryWrapper = new LambdaQueryWrapper<>();
        querySetForeignKey(subQueryWrapper, dto);
        dto.setSubList(subConverter.mapperDto(subMapper.selectList(subQueryWrapper)));
        return dto;
    }

    /**
     * 根据外键查询子数据对象集合 | 子数据
     *
     * @param foreignKey 外键
     * @return 子数据对象集合
     */
    @Override
    public List<SD> selectSubByForeignKey(Serializable foreignKey) {
        LambdaQueryWrapper<SP> subQueryWrapper = new LambdaQueryWrapper<>();
        querySetForeignKey(subQueryWrapper, foreignKey);
        return subConverter.mapperDto(subMapper.selectList(subQueryWrapper));
    }

    /**
     * 根据Id修改其归属数据的状态
     *
     * @param foreignKey 子表外键值
     * @param status     状态
     * @return 结果
     */
    @Override
    public int updateSubStatus(Serializable foreignKey, String status) {
        LambdaUpdateWrapper<SP> updateWrapper = new LambdaUpdateWrapper<>();
        updateSetForeignKey(updateWrapper, foreignKey);
        updateWrapper
                .set(SP::getStatus, status);
        return subMapper.delete(updateWrapper);
    }

    /**
     * 根据Id删除其归属数据
     *
     * @param foreignKey 子表外键值
     * @return 结果
     */
    @Override
    public int deleteSub(Serializable foreignKey) {
        LambdaUpdateWrapper<SP> deleteWrapper = new LambdaUpdateWrapper<>();
        updateSetForeignKey(deleteWrapper, foreignKey);
        return subMapper.delete(deleteWrapper);
    }

    /**
     * 校验是否存在子数据
     *
     * @param foreignKey 子表外键值
     * @return 子数据对象
     */
    @Override
    public SD checkExistSub(Serializable foreignKey) {
        LambdaQueryWrapper<SP> queryWrapper = new LambdaQueryWrapper<>();
        querySetForeignKey(queryWrapper, foreignKey);
        queryWrapper
                .last(SqlConstants.LIMIT_ONE);
        return subConverter.mapperDto(subMapper.selectOne(queryWrapper));
    }

    /**
     * 校验是否存在启用（正常状态）的子数据
     *
     * @param foreignKey 子表外键值
     * @return 子数据对象
     */
    @Override
    public SD checkExistNormalSub(Serializable foreignKey) {
        LambdaQueryWrapper<SP> queryWrapper = new LambdaQueryWrapper<>();
        querySetForeignKey(queryWrapper, foreignKey);
        queryWrapper
                .eq(SP::getStatus, BaseConstants.Status.NORMAL.getCode())
                .last(SqlConstants.LIMIT_ONE);
        return subConverter.mapperDto(subMapper.selectOne(queryWrapper));
    }

}
