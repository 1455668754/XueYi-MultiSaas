package com.xueyi.tenant.tenant.manager.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.web.entity.manager.impl.BaseManagerImpl;
import com.xueyi.tenant.api.tenant.domain.dto.TeTenantDto;
import com.xueyi.tenant.api.tenant.domain.po.TeTenantPo;
import com.xueyi.tenant.api.tenant.domain.query.TeTenantQuery;
import com.xueyi.tenant.tenant.domain.model.TeTenantConverter;
import com.xueyi.tenant.tenant.manager.ITeTenantManager;
import com.xueyi.tenant.tenant.mapper.TeTenantMapper;
import org.springframework.stereotype.Component;

/**
 * 租户服务 | 租户模块 | 租户管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class TeTenantManagerImpl extends BaseManagerImpl<TeTenantQuery, TeTenantDto, TeTenantPo, TeTenantMapper, TeTenantConverter> implements ITeTenantManager {

    /**
     * 校验数据源策略是否被使用
     *
     * @param strategyId 数据源策略id
     * @return 结果
     */
    @Override
    public TeTenantDto checkStrategyExist(Long strategyId) {
        TeTenantPo tenant = baseMapper.selectOne(
                Wrappers.<TeTenantPo>query().lambda()
                        .eq(TeTenantPo::getStrategyId, strategyId)
                        .last(SqlConstants.LIMIT_ONE));
        return baseConverter.mapperDto(tenant);
    }

    /**
     * 检查域名是否存在
     *
     * @param domainName 企业自定义域名
     * @return 租户信息对象
     */
    @Override
    public TeTenantDto checkDomain(String domainName) {
        TeTenantPo tenant = baseMapper.selectOne(
                Wrappers.<TeTenantPo>query().lambda()
                        .eq(TeTenantPo::getDomainName, domainName)
                        .last(SqlConstants.LIMIT_ONE));
        return baseConverter.mapperDto(tenant);
    }
}