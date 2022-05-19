package com.xueyi.tenant.tenant.manager;

import com.xueyi.common.web.entity.manager.IBaseManager;
import com.xueyi.tenant.api.tenant.domain.dto.TeTenantDto;
import com.xueyi.tenant.api.tenant.domain.query.TeTenantQuery;

/**
 * 租户管理 数据封装层
 *
 * @author xueyi
 */
public interface ITeTenantManager extends IBaseManager<TeTenantQuery, TeTenantDto> {

    /**
     * 校验数据源策略是否被使用
     *
     * @param strategyId 数据源策略id
     * @return 结果
     */
    TeTenantDto checkStrategyExist(Long strategyId);
}