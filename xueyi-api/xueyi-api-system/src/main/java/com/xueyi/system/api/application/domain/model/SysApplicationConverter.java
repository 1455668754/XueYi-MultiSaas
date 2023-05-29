package com.xueyi.system.api.application.domain.model;

import com.xueyi.common.core.web.entity.model.BaseConverter;
import com.xueyi.system.api.application.domain.dto.SysApplicationDto;
import com.xueyi.system.api.application.domain.po.SysApplicationPo;
import com.xueyi.system.api.application.domain.query.SysApplicationQuery;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * 系统服务 | 应用模块 | 应用 对象映射器
 *
 * @author xueyi
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SysApplicationConverter extends BaseConverter<SysApplicationQuery, SysApplicationDto, SysApplicationPo> {
}
