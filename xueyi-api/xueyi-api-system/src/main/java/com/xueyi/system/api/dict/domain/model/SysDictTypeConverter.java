package com.xueyi.system.api.dict.domain.model;

import com.xueyi.common.core.web.entity.model.BaseConverter;
import com.xueyi.system.api.dict.domain.dto.SysDictTypeDto;
import com.xueyi.system.api.dict.domain.po.SysDictTypePo;
import com.xueyi.system.api.dict.domain.query.SysDictTypeQuery;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * 字典类型 对象映射器
 *
 * @author xueyi
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SysDictTypeConverter extends BaseConverter<SysDictTypeQuery, SysDictTypeDto, SysDictTypePo> {
}
