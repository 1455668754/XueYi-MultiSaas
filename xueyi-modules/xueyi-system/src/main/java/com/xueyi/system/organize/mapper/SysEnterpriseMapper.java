package com.xueyi.system.organize.mapper;

import com.xueyi.common.datasource.annotation.Master;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import com.xueyi.system.api.organize.domain.po.SysEnterprisePo;
import com.xueyi.system.api.organize.domain.query.SysEnterpriseQuery;

/**
 * 系统服务 | 组织模块 | 企业管理 数据层
 *
 * @author xueyi
 */
@Master
public interface SysEnterpriseMapper extends BaseMapper<SysEnterpriseQuery, SysEnterpriseDto, SysEnterprisePo> {
}
