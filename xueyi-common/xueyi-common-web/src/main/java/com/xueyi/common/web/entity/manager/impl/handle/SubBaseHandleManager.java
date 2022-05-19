package com.xueyi.common.web.entity.manager.impl.handle;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.web.entity.base.SubBaseEntity;
import com.xueyi.common.core.web.entity.model.BaseConverter;
import com.xueyi.common.web.entity.manager.impl.BaseManager;
import com.xueyi.common.web.entity.manager.impl.SubTreeManager;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.common.web.entity.mapper.SubBaseMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * 数据封装层处理 操作方法 主子基类通用数据处理
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
public abstract class SubBaseHandleManager<Q extends P, D extends P, P extends SubBaseEntity<SD>, PM extends SubBaseMapper<Q, D, P, SQ, SD, SP>, CT extends BaseConverter<Q, D, P>, SQ extends SP, SD extends SP, SP extends BaseEntity, SPM extends BaseMapper<SQ, SD, SP>, SCT extends BaseConverter<SQ, SD, SP>> extends BaseManager<Q, D, P, PM, CT> {

    @Autowired
    protected SPM subMapper;

    @Autowired
    protected SCT subConverter;

    /** SubDto泛型的类型 */
    @SuppressWarnings("unchecked")
    private Class<SD> SDClass = (Class<SD>) getClazz(5);

    protected Class<SD> getSDClass() {
        return SDClass;
    }

    /**
     * 查询 设置主子表中子表外键值
     *
     * @param queryWrapper 条件构造器
     * @param d            数据对象
     * @see SubTreeManager#selectByIdExtra(Serializable)
     */
    protected void querySetForeignKey(LambdaQueryWrapper<SP> queryWrapper, D d) {
        setForeignKey(queryWrapper, null, d, null);
    }

    /**
     * 查询 设置主子表中子表外键值
     *
     * @param queryWrapper 条件构造器
     * @param foreignKey   子表外键值
     * @see SubTreeManager#checkExistSub(Serializable)
     * @see SubTreeManager#checkExistNormalSub(Serializable)
     */
    protected void querySetForeignKey(LambdaQueryWrapper<SP> queryWrapper, Serializable foreignKey) {
        setForeignKey(queryWrapper, null, null, foreignKey);
    }

    /**
     * 插入/删除 设置主子表中子表外键值
     *
     * @param updateWrapper 条件构造器
     * @param d             数据对象
     * @see SubTreeManager#updateSubStatus(Serializable, String)
     * @see SubTreeManager#deleteSub(Serializable)
     */
    protected void updateSetForeignKey(LambdaUpdateWrapper<SP> updateWrapper, D d) {
        setForeignKey(null, updateWrapper, d, null);
    }

    /**
     * 插入/删除 设置主子表中子表外键值
     *
     * @param updateWrapper 条件构造器
     * @param foreignKey    子表外键值
     * @see SubTreeManager#updateSubStatus(Serializable, String)
     * @see SubTreeManager#deleteSub(Serializable)
     */
    protected void updateSetForeignKey(LambdaUpdateWrapper<SP> updateWrapper, Serializable foreignKey) {
        setForeignKey(null, updateWrapper, null, foreignKey);
    }

    /**
     * 插入/删除 设置主子表中子表外键值
     *
     * @param queryWrapper  条件构造器
     * @param updateWrapper 条件构造器
     * @param d             数据对象
     * @param foreignKey    子表外键值
     */
    protected abstract void setForeignKey(LambdaQueryWrapper<SP> queryWrapper, LambdaUpdateWrapper<SP> updateWrapper, D d, Serializable foreignKey);
}
