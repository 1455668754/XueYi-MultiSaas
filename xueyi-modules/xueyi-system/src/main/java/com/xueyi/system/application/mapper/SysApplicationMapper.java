package com.xueyi.system.application.mapper;

import com.xueyi.common.datasource.annotation.Master;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.api.application.domain.dto.SysApplicationDto;
import com.xueyi.system.api.application.domain.po.SysApplicationPo;
import com.xueyi.system.api.application.domain.query.SysApplicationQuery;

/**
 * 系统服务 | 应用模块 | 应用管理 数据层
 *
 * @author xueyi
 */
@Master
public interface SysApplicationMapper extends BaseMapper<SysApplicationQuery, SysApplicationDto, SysApplicationPo> {
}