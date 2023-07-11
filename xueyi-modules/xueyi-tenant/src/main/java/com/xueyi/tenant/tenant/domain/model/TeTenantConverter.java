package com.xueyi.tenant.tenant.domain.model;

import com.xueyi.common.core.web.entity.model.BaseConverter;
import com.xueyi.tenant.api.tenant.domain.dto.TeTenantDto;
import com.xueyi.tenant.api.tenant.domain.po.TeTenantPo;
import com.xueyi.tenant.api.tenant.domain.query.TeTenantQuery;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * 租户服务 | 租户模块 | 租户 对象映射器
 *
 * @author xueyi
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TeTenantConverter extends BaseConverter<TeTenantQuery, TeTenantDto, TeTenantPo> {
}