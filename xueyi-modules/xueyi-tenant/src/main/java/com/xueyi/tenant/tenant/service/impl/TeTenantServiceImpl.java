package com.xueyi.tenant.tenant.service.impl;

import com.xueyi.tenant.api.tenant.domain.dto.TeTenantDto;
import com.xueyi.tenant.tenant.service.ITeTenantService;
import com.xueyi.tenant.tenant.manager.TeTenantManager;
import com.xueyi.tenant.tenant.mapper.TeTenantMapper;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.stereotype.Service;

import static com.xueyi.common.core.constant.TenantConstants.MASTER;

/**
 * 租户管理 服务层处理
 *
 * @author xueyi
 */
@Service
@DS(MASTER)
public class TeTenantServiceImpl extends BaseServiceImpl<TeTenantDto, TeTenantManager, TeTenantMapper> implements ITeTenantService {
}