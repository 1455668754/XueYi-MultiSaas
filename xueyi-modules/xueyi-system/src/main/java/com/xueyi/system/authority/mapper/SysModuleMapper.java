package com.xueyi.system.authority.mapper;

import com.xueyi.common.datasource.annotation.Master;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.api.authority.domain.po.SysModulePo;
import com.xueyi.system.api.authority.domain.query.SysModuleQuery;

/**
 * 系统服务 | 权限模块 | 角色管理 数据层
 *
 * @author xueyi
 */
@Master
public interface SysModuleMapper extends BaseMapper<SysModuleQuery, SysModuleDto, SysModulePo> {
}
