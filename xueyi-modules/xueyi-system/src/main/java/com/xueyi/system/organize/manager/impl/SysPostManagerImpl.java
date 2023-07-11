package com.xueyi.system.organize.manager.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.web.entity.manager.impl.BaseManagerImpl;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.api.organize.domain.po.SysPostPo;
import com.xueyi.system.api.organize.domain.query.SysPostQuery;
import com.xueyi.system.organize.domain.model.SysPostConverter;
import com.xueyi.system.organize.manager.ISysPostManager;
import com.xueyi.system.organize.mapper.SysPostMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 系统服务 | 组织模块 | 岗位管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class SysPostManagerImpl extends BaseManagerImpl<SysPostQuery, SysPostDto, SysPostPo, SysPostMapper, SysPostConverter> implements ISysPostManager {

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
