package com.xueyi.tenant.manager;

import com.xueyi.common.web.entity.manager.BaseManager;
import com.xueyi.tenant.domain.dto.TeTenantDto;
import com.xueyi.tenant.mapper.TeTenantMapper;
import org.springframework.stereotype.Component;

/**
 * 租户管理 数据封装层
 *
 * @author xueyi
 */
@Component
public class TeTenantManager extends BaseManager<TeTenantDto, TeTenantMapper> {

}
