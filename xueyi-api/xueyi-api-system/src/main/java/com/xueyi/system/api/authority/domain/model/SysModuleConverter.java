package com.xueyi.system.api.authority.domain.model;

import com.xueyi.common.core.web.entity.model.SubBaseConverter;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.api.authority.domain.po.SysModulePo;
import com.xueyi.system.api.authority.domain.query.SysModuleQuery;
import org.mapstruct.Mapper;

/**
 * 模块 对象映射器
 *
 * @author xueyi
 */
@Mapper(componentModel = "spring")
public interface SysModuleConverter extends SubBaseConverter<SysModuleQuery, SysModuleDto, SysModulePo> {
}
