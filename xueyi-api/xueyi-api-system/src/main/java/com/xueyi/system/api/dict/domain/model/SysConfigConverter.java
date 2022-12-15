package com.xueyi.system.api.dict.domain.model;

import com.xueyi.common.core.web.entity.model.BaseConverter;
import com.xueyi.system.api.dict.domain.dto.SysConfigDto;
import com.xueyi.system.api.dict.domain.po.SysConfigPo;
import com.xueyi.system.api.dict.domain.query.SysConfigQuery;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * 参数配置 对象映射器
 *
 * @author xueyi
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SysConfigConverter extends BaseConverter<SysConfigQuery, SysConfigDto, SysConfigPo> {
}
