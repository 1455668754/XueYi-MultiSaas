package com.xueyi.system.authority.mapper;

import com.xueyi.common.datasource.annotation.Master;
import com.xueyi.common.web.entity.mapper.TreeMapper;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.po.SysMenuPo;
import com.xueyi.system.api.authority.domain.query.SysMenuQuery;

/**
 * 菜单管理 数据层
 *
 * @author xueyi
 */
@Master
public interface SysMenuMapper extends TreeMapper<SysMenuQuery, SysMenuDto, SysMenuPo> {
}
