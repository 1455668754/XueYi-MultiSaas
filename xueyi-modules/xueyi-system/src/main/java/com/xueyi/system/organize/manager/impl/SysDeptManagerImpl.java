package com.xueyi.system.organize.manager.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.web.entity.domain.SubRelation;
import com.xueyi.common.web.entity.manager.impl.TreeManagerImpl;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.model.SysDeptConverter;
import com.xueyi.system.api.organize.domain.po.SysDeptPo;
import com.xueyi.system.api.organize.domain.query.SysDeptQuery;
import com.xueyi.system.organize.domain.merge.SysOrganizeRoleMerge;
import com.xueyi.system.organize.domain.merge.SysRoleDeptMerge;
import com.xueyi.system.organize.manager.ISysDeptManager;
import com.xueyi.system.organize.mapper.SysDeptMapper;
import com.xueyi.system.organize.mapper.merge.SysOrganizeRoleMergeMapper;
import com.xueyi.system.organize.mapper.merge.SysRoleDeptMergeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.xueyi.system.api.constant.MergeConstants.DEPT_POST_GROUP;

/**
 * 部门管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class SysDeptManagerImpl extends TreeManagerImpl<SysDeptQuery, SysDeptDto, SysDeptPo, SysDeptMapper, SysDeptConverter> implements ISysDeptManager {

    @Autowired
    SysOrganizeRoleMergeMapper organizeRoleMergeMapper;

    @Autowired
    SysRoleDeptMergeMapper roleDeptMergeMapper;

    /**
     * 初始化子类关联
     *
     * @return 关系对象集合
     */
    protected List<SubRelation> subRelationInit() {
        return new ArrayList<SubRelation>(){{
            add(new SubRelation(SysPostManagerImpl.class, DEPT_POST_GROUP));
        }};
    }

    /**
     * 根据Id删除部门对象
     *
     * @param id Id
     * @return 结果
     */
    @Override
    @DSTransactional
    public int deleteById(Serializable id) {
        int row = baseMapper.deleteById(id);
        if (row > 0) {
            organizeRoleMergeMapper.delete(
                    Wrappers.<SysOrganizeRoleMerge>update().lambda()
                            .eq(SysOrganizeRoleMerge::getDeptId, id));
            roleDeptMergeMapper.delete(
                    Wrappers.<SysRoleDeptMerge>update().lambda()
                            .eq(SysRoleDeptMerge::getDeptId, id));
        }
        return row;
    }

    /**
     * 根据Id集合批量删除部门对象
     *
     * @param idList Id集合
     * @return 结果
     */
    @Override
    @DSTransactional
    public int deleteByIds(Collection<? extends Serializable> idList) {
        int rows = baseMapper.deleteBatchIds(idList);
        if (rows > 0) {
            organizeRoleMergeMapper.delete(
                    Wrappers.<SysOrganizeRoleMerge>update().lambda()
                            .in(SysOrganizeRoleMerge::getDeptId, idList));
            roleDeptMergeMapper.delete(
                    Wrappers.<SysRoleDeptMerge>update().lambda()
                            .in(SysRoleDeptMerge::getDeptId, idList));
        }
        return rows;
    }

    /**
     * 校验部门编码是否唯一
     *
     * @param Id   部门Id
     * @param code 部门编码
     * @return 部门对象
     */
    @Override
    public SysDeptDto checkDeptCodeUnique(Long Id, String code) {
        SysDeptPo dept = baseMapper.selectOne(
                Wrappers.<SysDeptPo>query().lambda()
                        .ne(SysDeptPo::getId, Id)
                        .eq(SysDeptPo::getCode, code)
                        .last(SqlConstants.LIMIT_ONE));
        return baseConverter.mapperDto(dept);
    }

}
