package com.xueyi.system.organize.mapper;

import com.xueyi.common.datasource.annotation.Isolate;
import com.xueyi.common.web.entity.mapper.TreeMapper;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.po.SysDeptPo;
import com.xueyi.system.api.organize.domain.query.SysDeptQuery;

/**
 * 系统服务 | 组织模块 | 部门管理 数据层
 *
 * @author xueyi
 */
@Isolate
public interface SysDeptMapper extends TreeMapper<SysDeptQuery, SysDeptDto, SysDeptPo> {
}
