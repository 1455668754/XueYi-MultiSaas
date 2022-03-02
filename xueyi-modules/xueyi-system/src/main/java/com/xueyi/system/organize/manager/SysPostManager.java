package com.xueyi.system.organize.manager;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.web.entity.manager.BaseManager;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.organize.mapper.SysPostMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 岗位管理 数据封装层
 *
 * @author xueyi
 */
@Component
public class SysPostManager extends BaseManager<SysPostDto, SysPostMapper> {

    /**
     * 用户登录校验 | 根据部门Ids获取归属岗位对象集合
     *
     * @param deptIds 部门Ids
     * @return 岗位对象集合
     */
    public List<SysPostDto> selectListByDeptIds(Collection<Long> deptIds) {
        if (CollUtil.isEmpty(deptIds))
            return new ArrayList<>();
        return baseMapper.selectList(
                Wrappers.<SysPostDto>query().lambda()
                        .in(SysPostDto::getDeptId, deptIds));
    }

    /**
     * 校验岗位编码是否唯一
     *
     * @param Id   岗位Id
     * @param code 岗位编码
     * @return 岗位对象
     */
    public SysPostDto checkPostCodeUnique(Long Id, String code) {
        return baseMapper.selectOne(
                Wrappers.<SysPostDto>query().lambda()
                        .ne(SysPostDto::getId, Id)
                        .eq(SysPostDto::getCode, code)
                        .last(SqlConstants.LIMIT_ONE));
    }

}
