package com.xueyi.tenant.tenant.manager.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.web.entity.manager.impl.BaseManager;
import com.xueyi.tenant.api.tenant.domain.dto.TeTenantDto;
import com.xueyi.tenant.api.tenant.domain.model.TeTenantConverter;
import com.xueyi.tenant.api.tenant.domain.po.TeTenantPo;
import com.xueyi.tenant.api.tenant.domain.query.TeTenantQuery;
import com.xueyi.tenant.tenant.manager.ITeTenantManager;
import com.xueyi.tenant.tenant.mapper.TeTenantMapper;
import org.springframework.stereotype.Component;

/**
 * 租户管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class TeTenantManager extends BaseManager<TeTenantQuery, TeTenantDto, TeTenantPo, TeTenantMapper, TeTenantConverter> implements ITeTenantManager {

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
}