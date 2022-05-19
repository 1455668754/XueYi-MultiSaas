package com.xueyi.gen.domain.model;

import com.xueyi.common.core.web.entity.model.SubBaseConverter;
import com.xueyi.gen.domain.dto.GenTableDto;
import com.xueyi.gen.domain.po.GenTablePo;
import com.xueyi.gen.domain.query.GenTableQuery;
import org.mapstruct.Mapper;

/**
 * 业务 对象映射器
 *
 * @author xueyi
 */
@Mapper(componentModel = "spring")
public interface GenTableConverter extends SubBaseConverter<GenTableQuery, GenTableDto, GenTablePo> {
}