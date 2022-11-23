package com.xueyi.job.api.domain.model;

import com.xueyi.common.core.web.entity.model.BaseConverter;
import com.xueyi.job.api.domain.dto.SysJobDto;
import com.xueyi.job.api.domain.po.SysJobPo;
import com.xueyi.job.api.domain.query.SysJobQuery;
import org.mapstruct.Mapper;

/**
 * 调度任务 对象映射器
 *
 * @author xueyi
 */
@Mapper(componentModel = "spring")
public interface SysJobConverter extends BaseConverter<SysJobQuery, SysJobDto, SysJobPo> {
}
