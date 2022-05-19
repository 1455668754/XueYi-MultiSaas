package com.xueyi.tenant.api.source.domain.model;

import com.xueyi.common.core.web.entity.model.BaseConverter;
import com.xueyi.tenant.api.source.domain.dto.TeSourceDto;
import com.xueyi.tenant.api.source.domain.po.TeSourcePo;
import com.xueyi.tenant.api.source.domain.query.TeSourceQuery;
import org.mapstruct.Mapper;

/**
 * 数据源 对象映射器
 *
 * @author xueyi
 */
@Mapper(componentModel = "spring")
public interface TeSourceConverter extends BaseConverter<TeSourceQuery, TeSourceDto, TeSourcePo> {
}