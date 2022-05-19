package com.xueyi.system.authority.mapper;

import com.xueyi.common.datasource.annotation.Isolate;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.api.authority.domain.dto.SysRoleDto;
import com.xueyi.system.api.authority.domain.po.SysRolePo;
import com.xueyi.system.api.authority.domain.query.SysRoleQuery;

/**
 * 岗位管理 数据层
 *
 * @author xueyi
 */
@Isolate
public interface SysRoleMapper extends BaseMapper<SysRoleQuery, SysRoleDto, SysRolePo> {
}
