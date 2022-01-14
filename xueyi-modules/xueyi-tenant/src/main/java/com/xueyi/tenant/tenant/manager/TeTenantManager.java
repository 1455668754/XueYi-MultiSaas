package com.xueyi.tenant.tenant.manager;

import com.xueyi.tenant.api.tenant.domain.dto.TeTenantDto;
import com.xueyi.tenant.tenant.mapper.TeTenantMapper;
import com.xueyi.common.web.entity.manager.BaseManager;
import org.springframework.stereotype.Component;

/**
 * 租户管理 数据封装层
 *
 * @author xueyi
 */
@Component
public class TeTenantManager extends BaseManager<TeTenantDto, TeTenantMapper> {
}