package com.xueyi.tenant.api.tenant.domain.model;

import com.xueyi.common.core.web.entity.model.BaseConverter;
import com.xueyi.tenant.api.tenant.domain.dto.TeTenantDto;
import com.xueyi.tenant.api.tenant.domain.po.TeTenantPo;
import com.xueyi.tenant.api.tenant.domain.query.TeTenantQuery;
import org.mapstruct.Mapper;

/**
 * 租户 对象映射器
 *
 * @author xueyi
 */
@Mapper(componentModel = "spring")
public interface TeTenantConverter extends BaseConverter<TeTenantQuery, TeTenantDto, TeTenantPo> {
}