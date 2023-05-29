package com.xueyi.system.dict.mapper;

import com.xueyi.common.datasource.annotation.Master;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.api.dict.domain.dto.SysConfigDto;
import com.xueyi.system.api.dict.domain.po.SysConfigPo;
import com.xueyi.system.api.dict.domain.query.SysConfigQuery;

/**
 * 系统服务 | 字典模块 | 参数管理 数据层
 *
 * @author xueyi
 */
@Master
public interface SysConfigMapper extends BaseMapper<SysConfigQuery, SysConfigDto, SysConfigPo> {
}