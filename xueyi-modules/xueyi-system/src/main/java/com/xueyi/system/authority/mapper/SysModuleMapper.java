package com.xueyi.system.authority.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.mapper.SubBaseMapper;
import com.xueyi.system.api.domain.authority.dto.SysMenuDto;
import com.xueyi.system.api.domain.authority.dto.SysModuleDto;

import static com.xueyi.common.core.constant.TenantConstants.MASTER;

/**
 * 角色管理 数据层
 *
 * @author xueyi
 */
@DS(MASTER)
public interface SysModuleMapper extends SubBaseMapper<SysModuleDto, SysMenuDto> {
}
