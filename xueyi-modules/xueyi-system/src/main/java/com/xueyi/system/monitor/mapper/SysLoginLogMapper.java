package com.xueyi.system.monitor.mapper;

import com.xueyi.common.datasource.annotation.Isolate;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.api.log.domain.dto.SysLoginLogDto;
import com.xueyi.system.api.log.domain.po.SysLoginLogPo;
import com.xueyi.system.api.log.domain.query.SysLoginLogQuery;

/**
 * 系统服务 | 监控模块 | 访问日志管理 数据层
 *
 * @author xueyi
 */
@Isolate
public interface SysLoginLogMapper extends BaseMapper<SysLoginLogQuery, SysLoginLogDto, SysLoginLogPo> {
}
