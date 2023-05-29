package com.xueyi.system.api.authority.domain.model;

import com.xueyi.common.core.web.entity.model.BaseConverter;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.api.authority.domain.po.SysModulePo;
import com.xueyi.system.api.authority.domain.query.SysModuleQuery;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * 系统服务 | 权限模块 | 模块 对象映射器
 *
 * @author xueyi
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SysModuleConverter extends BaseConverter<SysModuleQuery, SysModuleDto, SysModulePo> {
}
