package com.xueyi.system.dict.domain.model;

import com.xueyi.common.core.web.entity.model.BaseConverter;
import com.xueyi.system.api.dict.domain.dto.SysImExDto;
import com.xueyi.system.api.dict.domain.po.SysImExPo;
import com.xueyi.system.api.dict.domain.query.SysImExQuery;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * 导入导出配置 对象映射器
 *
 * @author xueyi
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SysImExConverter extends BaseConverter<SysImExQuery, SysImExDto, SysImExPo> {
}
