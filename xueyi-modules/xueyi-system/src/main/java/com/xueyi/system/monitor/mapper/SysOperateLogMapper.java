package com.xueyi.system.monitor.mapper;

import com.xueyi.common.datasource.annotation.Isolate;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.api.log.domain.dto.SysOperateLogDto;
import com.xueyi.system.api.log.domain.po.SysOperateLogPo;
import com.xueyi.system.api.log.domain.query.SysOperateLogQuery;

/**
 * 操作日志管理 数据层
 *
 * @author xueyi
 */
@Isolate
public interface SysOperateLogMapper extends BaseMapper<SysOperateLogQuery, SysOperateLogDto, SysOperateLogPo> {
}
