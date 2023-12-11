package com.xueyi.tenant.tenant.service;

import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.tenant.api.tenant.domain.dto.TeTenantDto;
import com.xueyi.tenant.api.tenant.domain.query.TeTenantQuery;
import com.xueyi.tenant.tenant.domain.dto.TeTenantRegister;

/**
 * 租户服务 | 租户模块 | 租户管理 服务层
 *
 * @author xueyi
 */
public interface ITeTenantService extends IBaseService<TeTenantQuery, TeTenantDto> {

    /**
     * 新增租户 | 包含数据初始化
     *
     * @param tenantRegister 租户初始化对象
     * @return 结果
     */
    int insert(TeTenantRegister tenantRegister);

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

    /**
     * 租户组织数据初始化
     *
     * @param tenantRegister 租户初始化对象
     */
    void organizeInit(TeTenantRegister tenantRegister);
    /**
     * 租户域名唯一校验
     *
     * @param url 租户绑定域名
     */
    boolean checkDomainUnique(String url,Long id);
}