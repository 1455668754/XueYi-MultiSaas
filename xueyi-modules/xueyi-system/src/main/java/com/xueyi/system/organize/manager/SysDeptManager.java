package com.xueyi.system.organize.manager;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.SqlConstants;
import com.xueyi.common.web.entity.manager.SubTreeManager;
import com.xueyi.system.api.domain.organize.dto.SysDeptDto;
import com.xueyi.system.api.domain.organize.dto.SysPostDto;
import com.xueyi.system.organize.mapper.SysDeptMapper;
import com.xueyi.system.organize.mapper.SysPostMapper;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 部门管理 数据封装层
 *
 * @author xueyi
 */
@Component
public class SysDeptManager extends SubTreeManager<SysDeptDto, SysDeptMapper, SysPostDto, SysPostMapper> {

    /**
     * 设置主子表中子表外键值
     */
    @Override
    protected void setForeignKey(LambdaQueryWrapper<SysPostDto> queryWrapper, LambdaUpdateWrapper<SysPostDto> updateWrapper, SysDeptDto dept, Serializable id) {
        Serializable deptId = ObjectUtil.isNotNull(dept) ? dept.getId() : id;
        if (ObjectUtil.isNotNull(queryWrapper))
            queryWrapper.eq(SysPostDto::getDeptId, deptId);
        else
            updateWrapper.eq(SysPostDto::getDeptId, deptId);
    }

    /**
     * 校验部门编码是否唯一
     *
     * @param Id   部门Id
     * @param code 部门编码
     * @return 部门对象
     */
    public SysDeptDto checkDeptCodeUnique(Long Id, String code) {
        return baseMapper.selectOne(
                Wrappers.<SysDeptDto>query().lambda()
                        .ne(SysDeptDto::getId, Id)
                        .eq(SysDeptDto::getCode, code)
                        .last(SqlConstants.LIMIT_ONE));
    }
}
