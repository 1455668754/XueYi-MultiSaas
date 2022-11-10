package com.xueyi.system.organize.manager.impl;

import com.xueyi.common.core.utils.core.ObjectUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.web.entity.manager.impl.SubTreeManagerImpl;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.api.organize.domain.model.SysDeptConverter;
import com.xueyi.system.api.organize.domain.model.SysPostConverter;
import com.xueyi.system.api.organize.domain.po.SysDeptPo;
import com.xueyi.system.api.organize.domain.po.SysPostPo;
import com.xueyi.system.api.organize.domain.query.SysDeptQuery;
import com.xueyi.system.api.organize.domain.query.SysPostQuery;
import com.xueyi.system.organize.domain.merge.SysOrganizeRoleMerge;
import com.xueyi.system.organize.domain.merge.SysRoleDeptMerge;
import com.xueyi.system.organize.manager.ISysDeptManager;
import com.xueyi.system.organize.mapper.SysDeptMapper;
import com.xueyi.system.organize.mapper.SysPostMapper;
import com.xueyi.system.organize.mapper.merge.SysOrganizeRoleMergeMapper;
import com.xueyi.system.organize.mapper.merge.SysRoleDeptMergeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;

/**
 * 部门管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class SysDeptManagerImpl extends SubTreeManagerImpl<SysDeptQuery, SysDeptDto, SysDeptPo, SysDeptMapper, SysDeptConverter, SysPostQuery, SysPostDto, SysPostPo, SysPostMapper, SysPostConverter> implements ISysDeptManager {

    @Autowired
    SysOrganizeRoleMergeMapper organizeRoleMergeMapper;

    @Autowired
    SysRoleDeptMergeMapper roleDeptMergeMapper;

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

    /**
     * 设置主子表中子表外键值
     */
    @Override
    protected void setForeignKey(LambdaQueryWrapper<SysPostPo> queryWrapper, LambdaUpdateWrapper<SysPostPo> updateWrapper, SysDeptDto dept, Serializable key) {
        Serializable deptId = ObjectUtil.isNotNull(dept) ? dept.getId() : key;
        if (ObjectUtil.isNotNull(queryWrapper))
            queryWrapper.eq(SysPostPo::getDeptId, deptId);
        else
            updateWrapper.eq(SysPostPo::getDeptId, deptId);
    }
}
