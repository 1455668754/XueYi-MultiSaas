package com.xueyi.system.organize.manager.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.web.entity.manager.impl.TreeManagerImpl;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.model.SysDeptConverter;
import com.xueyi.system.api.organize.domain.po.SysDeptPo;
import com.xueyi.system.api.organize.domain.query.SysDeptQuery;
import com.xueyi.system.organize.manager.ISysDeptManager;
import com.xueyi.system.organize.mapper.SysDeptMapper;
import org.springframework.stereotype.Component;

/**
 * 系统服务 | 组织模块 | 部门管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class SysDeptManagerImpl extends TreeManagerImpl<SysDeptQuery, SysDeptDto, SysDeptPo, SysDeptMapper, SysDeptConverter> implements ISysDeptManager {

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
        return mapperDto(dept);
    }
}
