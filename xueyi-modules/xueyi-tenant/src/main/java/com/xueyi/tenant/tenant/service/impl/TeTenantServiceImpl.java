package com.xueyi.tenant.tenant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.xueyi.common.core.constant.DictConstants;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.tenant.api.tenant.domain.dto.TeTenantDto;
import com.xueyi.tenant.tenant.domain.model.TeTenantRegister;
import com.xueyi.tenant.tenant.manager.TeTenantManager;
import com.xueyi.tenant.tenant.mapper.TeTenantMapper;
import com.xueyi.tenant.tenant.service.ITeTenantService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    ITeTenantService oneselfService;

    /**
     * 新增租户 | 包含数据初始化
     *
     * @param tenantRegister 租户初始化对象
     * @return 结果
     */
    @Override
    @DSTransactional
    public int insert(TeTenantRegister tenantRegister) {
        int rows = baseManager.insert(tenantRegister.getTenant());
        if (rows > 0) {
            oneselfService.organizeInit(tenantRegister);
            oneselfService.authorityInit(tenantRegister);
        }
        return rows;
    }

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

    /**
     * 租户组织数据初始化
     *
     * @param tenantRegister 租户初始化对象
     * @return 结果
     */
    @Override
    public boolean organizeInit(TeTenantRegister tenantRegister) {
        return true;
    }

    /**
     * 租户权限数据初始化
     *
     * @param tenantRegister 租户初始化对象
     * @return 结果
     */
    @Override
    public boolean authorityInit(TeTenantRegister tenantRegister) {
        return true;
    }
}