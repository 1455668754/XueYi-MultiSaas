package com.xueyi.tenant.source.mapper;

import com.xueyi.common.datasource.annotation.Master;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.tenant.api.source.domain.dto.TeSourceDto;
import com.xueyi.tenant.api.source.domain.po.TeSourcePo;
import com.xueyi.tenant.api.source.domain.query.TeSourceQuery;

/**
 * 租户服务 | 策略模块 | 数据源管理 数据层
 *
 * @author xueyi
 */
@Master
public interface TeSourceMapper extends BaseMapper<TeSourceQuery, TeSourceDto, TeSourcePo> {
}