package com.xueyi.tenant.api.tenant.domain.model;

import com.xueyi.common.core.web.entity.model.BaseConverter;
import com.xueyi.tenant.api.tenant.domain.dto.TeStrategyDto;
import com.xueyi.tenant.api.tenant.domain.po.TeStrategyPo;
import com.xueyi.tenant.api.tenant.domain.query.TeStrategyQuery;
import org.mapstruct.Mapper;

/**
 * 数据源策略 对象映射器
 *
 * @author xueyi
 */
@Mapper(componentModel = "spring")
public interface TeStrategyConverter extends BaseConverter<TeStrategyQuery, TeStrategyDto, TeStrategyPo> {
}