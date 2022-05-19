package com.xueyi.gen.domain.model;

import com.xueyi.common.core.web.entity.model.BaseConverter;
import com.xueyi.gen.domain.dto.GenTableColumnDto;
import com.xueyi.gen.domain.po.GenTableColumnPo;
import com.xueyi.gen.domain.query.GenTableColumnQuery;
import org.mapstruct.Mapper;

/**
 * 业务字段 对象映射器
 *
 * @author xueyi
 */
@Mapper(componentModel = "spring")
public interface GenTableColumnConverter extends BaseConverter<GenTableColumnQuery, GenTableColumnDto, GenTableColumnPo> {
}