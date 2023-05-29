package com.xueyi.system.api.authority.domain.model;

import com.xueyi.common.core.web.entity.model.TreeConverter;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.po.SysMenuPo;
import com.xueyi.system.api.authority.domain.query.SysMenuQuery;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * 系统服务 | 权限模块 | 菜单 对象映射器
 *
 * @author xueyi
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SysMenuConverter extends TreeConverter<SysMenuQuery, SysMenuDto, SysMenuPo> {
}
