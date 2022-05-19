package com.xueyi.job.api.domain.model;

import com.xueyi.common.core.web.entity.model.BaseConverter;
import com.xueyi.job.api.domain.dto.SysJobLogDto;
import com.xueyi.job.api.domain.po.SysJobLogPo;
import com.xueyi.job.api.domain.query.SysJobLogQuery;
import org.mapstruct.Mapper;

/**
 * 调度日志 对象映射器
 *
 * @author xueyi
 */
@Mapper(componentModel = "spring")
public interface SysJobLogConverter extends BaseConverter<SysJobLogQuery, SysJobLogDto, SysJobLogPo> {
}
