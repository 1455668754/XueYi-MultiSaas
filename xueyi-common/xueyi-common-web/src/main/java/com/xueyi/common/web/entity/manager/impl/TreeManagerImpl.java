package com.xueyi.common.web.entity.manager.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.entity.base.TreeEntity;
import com.xueyi.common.core.web.entity.model.BaseConverter;
import com.xueyi.common.web.entity.manager.ITreeManager;
import com.xueyi.common.web.entity.manager.impl.handle.TreeHandleManagerImpl;
import com.xueyi.common.web.entity.mapper.TreeMapper;

import java.io.Serializable;
import java.util.List;

import static com.xueyi.common.core.constant.basic.SqlConstants.*;

/**
 * 数据封装层处理 树型通用数据处理
 *
 * @param <Q>  Query
 * @param <D>  Dto
 * @param <P>  Po
 * @param <PM> PoMapper
 * @author xueyi
 */
public class TreeManagerImpl<Q extends P, D extends P, P extends TreeEntity<D>, PM extends TreeMapper<Q, D, P>, CT extends BaseConverter<Q, D, P>> extends TreeHandleManagerImpl<Q, D, P, PM, CT> implements ITreeManager<Q, D> {

    /**
     * 根据Id查询本节点及其所有祖籍节点
     *
     * @param id Id
     * @return 本节点及其所有祖籍节点数据对象集合
     */
    @Override
    public List<D> selectAncestorsListById(Serializable id) {
        P d = baseMapper.selectById(id);
        if (ObjectUtil.isNull(d) || StrUtil.isBlank(d.getAncestors()))
            return null;
        List<P> poList = baseMapper.selectList(
                Wrappers.<P>query().lambda()
                        .eq(P::getId, id)
                        .or().in(P::getId, StrUtil.splitTrim(d.getAncestors(), ",")));
        return baseConverter.mapperDto(poList);
    }

    /**
     * 根据Id查询本节点及其所有子节点
     *
     * @param id Id
     * @return 本节点及其所有子节点数据对象集合
     */
    @Override
    public List<D> selectChildListById(Serializable id) {
        List<P> poList = baseMapper.selectList(
                Wrappers.<P>query().lambda()
                        .eq(P::getId, id)
                        .or().apply(ANCESTORS_FIND, id));
        return baseConverter.mapperDto(poList);
    }

    /**
     * 修改子节点的状态
     *
     * @param dto 数据对象
     * @return 结果
     */
    @Override
    public int updateChildrenStatus(D dto) {
        String ancestors = dto.getOldAncestors() + StrUtil.COMMA + dto.getId();
        return baseMapper.update(null,
                Wrappers.<P>update().lambda()
                        .set(P::getStatus, dto.getStatus())
                        .likeRight(P::getAncestors, ancestors));
    }

    /**
     * 修改其子节点的祖籍
     *
     * @param dto 数据对象
     * @return 结果
     */
    @Override
    public int updateChildrenAncestors(D dto) {
        String newAncestors = dto.getAncestors() + StrUtil.COMMA + dto.getId();
        String oldAncestors = dto.getOldAncestors() + StrUtil.COMMA + dto.getId();
        int levelChange = dto.getLevel() - dto.getOldLevel();
        return baseMapper.update(null,
                Wrappers.<P>update().lambda()
                        .setSql(StrUtil.format(ANCESTORS_PART_UPDATE, SqlConstants.Entity.ANCESTORS.getCode(), SqlConstants.Entity.ANCESTORS.getCode(), 1, oldAncestors.length(), newAncestors))
                        .setSql(StrUtil.format(TREE_LEVEL_UPDATE, SqlConstants.Entity.LEVEL.getCode(), SqlConstants.Entity.LEVEL.getCode(), levelChange))
                        .likeRight(P::getAncestors, oldAncestors));
    }

    /**
     * 修改子节点的祖籍和状态
     *
     * @param dto 数据对象
     * @return 结果
     */
    @Override
    public int updateChildren(D dto) {
        String newAncestors = dto.getAncestors() + StrUtil.COMMA + dto.getId();
        String oldAncestors = dto.getOldAncestors() + StrUtil.COMMA + dto.getId();
        int levelChange = dto.getLevel() - dto.getOldLevel();
        return baseMapper.update(null,
                Wrappers.<P>update().lambda()
                        .set(P::getStatus, dto.getStatus())
                        .setSql(StrUtil.format(ANCESTORS_PART_UPDATE, SqlConstants.Entity.ANCESTORS.getCode(), SqlConstants.Entity.ANCESTORS.getCode(), 1, oldAncestors.length(), newAncestors))
                        .setSql(StrUtil.format(TREE_LEVEL_UPDATE, SqlConstants.Entity.LEVEL.getCode(), SqlConstants.Entity.LEVEL.getCode(), levelChange))
                        .likeRight(P::getAncestors, oldAncestors));
    }

    /**
     * 根据Id删除其子节点
     *
     * @param id Id
     * @return 结果
     */
    @Override
    public int deleteChildren(Serializable id) {
        return baseMapper.delete(
                Wrappers.<P>update().lambda()
                        .eq(P::getId, id)
                        .apply(ANCESTORS_FIND, id));
    }

    /**
     * 校验是否为父级的子级
     *
     * @param id       Id
     * @param parentId 父级Id
     * @return 结果 | true/false 是/否
     */
    @Override
    public D checkIsChild(Serializable id, Serializable parentId) {
        P po = baseMapper.selectOne(
                Wrappers.<P>query().lambda()
                        .eq(P::getId, id)
                        .apply(ANCESTORS_FIND, parentId)
                        .last(SqlConstants.LIMIT_ONE));
        return baseConverter.mapperDto(po);
    }

    /**
     * 校验是否存在子节点
     *
     * @param id Id
     * @return 结果 | true/false 有/无
     */
    @Override
    public D checkHasChild(Serializable id) {
        P po = baseMapper.selectOne(
                Wrappers.<P>query().lambda()
                        .apply(ANCESTORS_FIND, id)
                        .last(SqlConstants.LIMIT_ONE));
        return baseConverter.mapperDto(po);
    }

    /**
     * 校验是否有启用(正常状态)的子节点
     *
     * @param id Id
     * @return 结果 | true/false 有/无
     */
    @Override
    public D checkHasNormalChild(Serializable id) {
        P po = baseMapper.selectOne(
                Wrappers.<P>query().lambda()
                        .eq(P::getStatus, BaseConstants.Status.NORMAL.getCode())
                        .apply(ANCESTORS_FIND, id)
                        .last(SqlConstants.LIMIT_ONE));
        return baseConverter.mapperDto(po);
    }

    /**
     * 校验名称是否唯一
     *
     * @param id       Id
     * @param parentId 父级Id
     * @param name     名称
     * @return 数据对象
     */
    @Override
    public D checkNameUnique(Serializable id, Serializable parentId, String name) {
        P po = baseMapper.selectOne(
                Wrappers.<P>query().lambda()
                        .ne(P::getId, id)
                        .eq(P::getParentId, parentId)
                        .eq(P::getName, name)
                        .last(SqlConstants.LIMIT_ONE));
        return baseConverter.mapperDto(po);
    }
}
