package com.xueyi.system.api.log.domain.model;

import com.xueyi.common.core.web.entity.model.BaseConverter;
import com.xueyi.system.api.log.domain.dto.SysLoginLogDto;
import com.xueyi.system.api.log.domain.po.SysLoginLogPo;
import com.xueyi.system.api.log.domain.query.SysLoginLogQuery;
import org.mapstruct.Mapper;

/**
 * 访问日志 对象映射器
 *
 * @author xueyi
 */
@Mapper(componentModel = "spring")
public interface SysLoginLogConverter extends BaseConverter<SysLoginLogQuery, SysLoginLogDto, SysLoginLogPo> {
}
