package com.xueyi.tenant.tenant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.DictConstants;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.tenant.api.tenant.domain.dto.TeTenantDto;
import com.xueyi.tenant.tenant.manager.TeTenantManager;
import com.xueyi.tenant.tenant.mapper.TeTenantMapper;
import com.xueyi.tenant.tenant.service.ITeTenantService;
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

    /**
     * 校验源策略是否被使用
     *
     * @param strategyId 数据源策略id
     * @return 结果 | true/false 存在/不存在
     */
    @Override
    public boolean checkStrategyExist(Long strategyId) {
        return ObjectUtil.isNotNull(baseManager.checkStrategyExist(strategyId));
    }

    /**
     * 校验租户是否为默认租户
     *
     * @param id 租户id
     * @return 结果 | true/false 是/不是
     */
    @Override
    public boolean checkIsDefault(Long id) {
        TeTenantDto tenant = baseManager.selectById(id);
        return ObjectUtil.isNotNull(tenant) && StrUtil.equals(tenant.getIsDefault(), DictConstants.DicYesNo.YES.getCode());
    }
}