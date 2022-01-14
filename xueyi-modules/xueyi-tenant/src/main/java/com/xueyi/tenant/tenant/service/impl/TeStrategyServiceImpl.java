package com.xueyi.tenant.tenant.service.impl;

import com.xueyi.tenant.api.tenant.domain.dto.TeStrategyDto;
import com.xueyi.tenant.tenant.service.ITeStrategyService;
import com.xueyi.tenant.tenant.manager.TeStrategyManager;
import com.xueyi.tenant.tenant.mapper.TeStrategyMapper;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.stereotype.Service;

import static com.xueyi.common.core.constant.TenantConstants.MASTER;

/**
 * 数据源策略管理 服务层处理
 *
 * @author xueyi
 */
@Service
@DS(MASTER)
public class TeStrategyServiceImpl extends BaseServiceImpl<TeStrategyDto, TeStrategyManager, TeStrategyMapper> implements ITeStrategyService {
}