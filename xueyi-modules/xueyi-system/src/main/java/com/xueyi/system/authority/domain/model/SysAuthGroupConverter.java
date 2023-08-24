package com.xueyi.system.authority.domain.model;

import com.xueyi.common.core.web.entity.model.BaseConverter;
import com.xueyi.system.authority.domain.dto.SysAuthGroupDto;
import com.xueyi.system.authority.domain.po.SysAuthGroupPo;
import com.xueyi.system.authority.domain.query.SysAuthGroupQuery;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * 系统服务 | 权限模块 | 企业权限组 对象映射器
 *
 * @author xueyi
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SysAuthGroupConverter extends BaseConverter<SysAuthGroupQuery, SysAuthGroupDto, SysAuthGroupPo> {
}
