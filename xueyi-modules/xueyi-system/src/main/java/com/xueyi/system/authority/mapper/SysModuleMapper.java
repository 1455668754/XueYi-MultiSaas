package com.xueyi.system.authority.mapper;

import com.xueyi.common.datasource.annotation.Master;
import com.xueyi.common.web.entity.mapper.SubBaseMapper;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.api.authority.domain.po.SysMenuPo;
import com.xueyi.system.api.authority.domain.po.SysModulePo;
import com.xueyi.system.api.authority.domain.query.SysMenuQuery;
import com.xueyi.system.api.authority.domain.query.SysModuleQuery;

/**
 * 角色管理 数据层
 *
 * @author xueyi
 */
@Master
public interface SysModuleMapper extends SubBaseMapper<SysModuleQuery, SysModuleDto, SysModulePo, SysMenuQuery, SysMenuDto, SysMenuPo> {
}
