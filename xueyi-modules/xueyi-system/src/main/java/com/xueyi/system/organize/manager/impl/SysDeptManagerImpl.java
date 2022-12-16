package com.xueyi.system.organize.manager.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.web.entity.domain.SlaveRelation;
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

import java.util.ArrayList;
import java.util.List;

import static com.xueyi.system.api.organize.domain.merge.MergeGroup.*;

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
    protected List<SlaveRelation> subRelationInit() {
        return new ArrayList<>(){{
            add(new SlaveRelation(DEPT_POST_GROUP, SysPostManagerImpl.class));
            add(new SlaveRelation(DEPT_ROLE_INDIRECT_GROUP, SysOrganizeRoleMergeMapper.class, SysOrganizeRoleMerge.class, OperateConstants.SubOperateLimit.EX_SEL_OR_ADD_OR_EDIT));
            add(new SlaveRelation(ROLE_DEPT_INDIRECT_GROUP, SysRoleDeptMergeMapper.class, SysRoleDeptMerge.class, OperateConstants.SubOperateLimit.EX_SEL_OR_ADD_OR_EDIT));
        }};
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
