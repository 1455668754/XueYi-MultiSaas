package com.xueyi.job.mapper;

import com.xueyi.common.datasource.annotation.Isolate;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.job.api.domain.dto.SysJobLogDto;
import com.xueyi.job.api.domain.po.SysJobLogPo;
import com.xueyi.job.api.domain.query.SysJobLogQuery;


/**
 * 调度日志管理 数据层
 *
 * @author xueyi
 */
@Isolate
public interface SysJobLogMapper extends BaseMapper<SysJobLogQuery, SysJobLogDto, SysJobLogPo> {
}
