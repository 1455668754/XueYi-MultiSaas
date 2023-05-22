package com.xueyi.tenant.source.mapper;

import com.xueyi.common.datasource.annotation.Master;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.tenant.api.source.domain.dto.TeStrategyDto;
import com.xueyi.tenant.api.source.domain.po.TeStrategyPo;
import com.xueyi.tenant.api.source.domain.query.TeStrategyQuery;

/**
 * 数据源策略管理 数据层
 *
 * @author xueyi
 */
@Master
public interface TeStrategyMapper extends BaseMapper<TeStrategyQuery, TeStrategyDto, TeStrategyPo> {
}