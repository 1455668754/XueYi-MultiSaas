package com.xueyi.system.organize.manager.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.web.entity.domain.SlaveRelation;
import com.xueyi.common.web.entity.manager.impl.BaseManagerImpl;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.api.organize.domain.model.SysPostConverter;
import com.xueyi.system.api.organize.domain.po.SysPostPo;
import com.xueyi.system.api.organize.domain.query.SysPostQuery;
import com.xueyi.system.organize.domain.merge.SysOrganizeRoleMerge;
import com.xueyi.system.organize.domain.merge.SysRolePostMerge;
import com.xueyi.system.organize.domain.merge.SysUserPostMerge;
import com.xueyi.system.organize.manager.ISysPostManager;
import com.xueyi.system.organize.mapper.SysPostMapper;
import com.xueyi.system.organize.mapper.merge.SysOrganizeRoleMergeMapper;
import com.xueyi.system.organize.mapper.merge.SysRolePostMergeMapper;
import com.xueyi.system.organize.mapper.merge.SysUserPostMergeMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.xueyi.system.api.organize.domain.merge.MergeGroup.*;

/**
 * 岗位管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class SysPostManagerImpl extends BaseManagerImpl<SysPostQuery, SysPostDto, SysPostPo, SysPostMapper, SysPostConverter> implements ISysPostManager {

    /**
     * 初始化从属关联关系
     *
     * @return 关系对象集合
     */
    protected List<SlaveRelation> subRelationInit() {
        return new ArrayList<>(){{
            add(new SlaveRelation(POST_OrganizeRoleMerge_GROUP, SysOrganizeRoleMergeMapper.class, SysOrganizeRoleMerge.class, OperateConstants.SubOperateLimit.EX_SEL_OR_ADD_OR_EDIT));
            add(new SlaveRelation(POST_SysRolePostMerge_GROUP, SysRolePostMergeMapper.class, SysRolePostMerge.class, OperateConstants.SubOperateLimit.EX_SEL_OR_ADD_OR_EDIT));
            add(new SlaveRelation(POST_SysUserPostMerge_GROUP, SysUserPostMergeMapper.class, SysUserPostMerge.class, OperateConstants.SubOperateLimit.EX_SEL_OR_ADD_OR_EDIT));
        }};
    }

    /**
     * 用户登录校验 | 根据部门Ids获取归属岗位对象集合
     *
     * @param deptIds 部门Ids
     * @return 岗位对象集合
     */
    @Override
    public List<SysPostDto> selectListByDeptIds(Collection<Long> deptIds) {
        if (CollUtil.isEmpty(deptIds))
            return new ArrayList<>();
        List<SysPostPo> postList = baseMapper.selectList(
                Wrappers.<SysPostPo>query().lambda()
                        .in(SysPostPo::getDeptId, deptIds));
        return mapperDto(postList);
    }

    /**
     * 校验岗位编码是否唯一
     *
     * @param Id   岗位Id
     * @param code 岗位编码
     * @return 岗位对象
     */
    @Override
    public SysPostDto checkPostCodeUnique(Long Id, String code) {
        SysPostPo post = baseMapper.selectOne(
                Wrappers.<SysPostPo>query().lambda()
                        .ne(SysPostPo::getId, Id)
                        .eq(SysPostPo::getCode, code)
                        .last(SqlConstants.LIMIT_ONE));
        return mapperDto(post);
    }
}
