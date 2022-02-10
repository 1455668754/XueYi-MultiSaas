package com.xueyi.tenant.tenant.service;

import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.tenant.api.tenant.domain.dto.TeTenantDto;

/**
 * 租户管理 服务层
 *
 * @author xueyi
 */
public interface ITeTenantService extends IBaseService<TeTenantDto> {

    /**
     * 校验数据源策略是否被使用
     *
     * @param strategyId 数据源策略id
     * @return 结果 | true/false 存在/不存在
     */
    boolean checkStrategyExist(Long strategyId);

    /**
     * 校验租户是否为默认租户
     *
     * @param id 租户id
     * @return 结果 | true/false 是/不是
     */
    boolean checkIsDefault(Long id);
}