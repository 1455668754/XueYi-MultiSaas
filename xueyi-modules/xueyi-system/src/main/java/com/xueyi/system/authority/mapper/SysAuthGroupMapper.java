package com.xueyi.system.authority.mapper;

import com.xueyi.system.authority.domain.query.SysAuthGroupQuery;
import com.xueyi.system.authority.domain.dto.SysAuthGroupDto;
import com.xueyi.system.authority.domain.po.SysAuthGroupPo;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.common.datasource.annotation.Master;

/**
 * 系统服务 | 权限模块 | 租户权限组管理 数据层
 *
 * @author xueyi
 */
@Master
public interface SysAuthGroupMapper extends BaseMapper<SysAuthGroupQuery, SysAuthGroupDto, SysAuthGroupPo> {
}