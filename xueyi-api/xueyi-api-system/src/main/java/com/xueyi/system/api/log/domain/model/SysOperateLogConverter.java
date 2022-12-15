package com.xueyi.system.api.log.domain.model;

import com.xueyi.common.core.web.entity.model.BaseConverter;
import com.xueyi.system.api.log.domain.dto.SysOperateLogDto;
import com.xueyi.system.api.log.domain.po.SysOperateLogPo;
import com.xueyi.system.api.log.domain.query.SysOperateLogQuery;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * 操作日志 对象映射器
 *
 * @author xueyi
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SysOperateLogConverter extends BaseConverter<SysOperateLogQuery, SysOperateLogDto, SysOperateLogPo> {
}
