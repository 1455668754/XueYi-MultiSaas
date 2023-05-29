package com.xueyi.system.api.authority.domain.model;

import com.xueyi.common.core.web.entity.model.BaseConverter;
import com.xueyi.system.api.authority.domain.dto.SysRoleDto;
import com.xueyi.system.api.authority.domain.po.SysRolePo;
import com.xueyi.system.api.authority.domain.query.SysRoleQuery;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * 系统服务 | 权限模块 | 角色 对象映射器
 *
 * @author xueyi
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SysRoleConverter extends BaseConverter<SysRoleQuery, SysRoleDto, SysRolePo> {
}
