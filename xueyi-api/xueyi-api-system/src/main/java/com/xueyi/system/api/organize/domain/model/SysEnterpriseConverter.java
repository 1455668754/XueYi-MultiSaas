package com.xueyi.system.api.organize.domain.model;

import com.xueyi.common.core.web.entity.model.BaseConverter;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import com.xueyi.system.api.organize.domain.po.SysEnterprisePo;
import com.xueyi.system.api.organize.domain.query.SysEnterpriseQuery;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * 企业 对象映射器
 *
 * @author xueyi
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SysEnterpriseConverter extends BaseConverter<SysEnterpriseQuery, SysEnterpriseDto, SysEnterprisePo> {
}
