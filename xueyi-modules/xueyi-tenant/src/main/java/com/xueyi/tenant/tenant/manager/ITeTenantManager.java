package com.xueyi.tenant.tenant.manager;

import com.xueyi.common.web.entity.manager.IBaseManager;
import com.xueyi.tenant.api.tenant.domain.dto.TeTenantDto;
import com.xueyi.tenant.api.tenant.domain.query.TeTenantQuery;

/**
 * 租户服务 | 租户模块 | 租户管理 数据封装层
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

    /**
     * 检查域名是否存在
     *
     * @param domainName 企业自定义域名
     * @return 租户信息对象
     */
    TeTenantDto checkDomain(String domainName);
}