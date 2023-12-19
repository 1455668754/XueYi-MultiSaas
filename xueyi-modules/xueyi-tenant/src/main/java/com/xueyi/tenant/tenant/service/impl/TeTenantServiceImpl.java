package com.xueyi.tenant.tenant.service.impl;

import com.xueyi.common.cache.constant.CacheConstants;
import com.xueyi.common.core.constant.basic.DictConstants;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.result.R;
import com.xueyi.common.security.utils.SecurityUserUtils;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.system.api.organize.feign.RemoteDeptService;
import com.xueyi.system.api.organize.feign.RemotePostService;
import com.xueyi.system.api.organize.feign.RemoteUserService;
import com.xueyi.tenant.api.source.domain.dto.TeStrategyDto;
import com.xueyi.tenant.api.tenant.domain.dto.TeTenantDto;
import com.xueyi.tenant.api.tenant.domain.query.TeTenantQuery;
import com.xueyi.tenant.source.service.ITeStrategyService;
import com.xueyi.tenant.tenant.domain.correlate.TeTenantCorrelate;
import com.xueyi.tenant.tenant.domain.dto.TeTenantRegister;
import com.xueyi.tenant.tenant.manager.impl.TeTenantManagerImpl;
import com.xueyi.tenant.tenant.service.ITeTenantService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.xueyi.common.core.constant.basic.BaseConstants.TOP_ID;

/**
 * 租户服务 | 租户模块 | 租户管理 服务层处理
 *
 * @author xueyi
 */
@Service
public class TeTenantServiceImpl extends BaseServiceImpl<TeTenantQuery, TeTenantDto, TeTenantCorrelate, TeTenantManagerImpl> implements ITeTenantService {

    @Lazy
    @Autowired
    ITeTenantService oneselfService;

    @Autowired
    private ITeStrategyService strategyService;

    @Autowired
    private RemoteDeptService deptService;

    @Autowired
    private RemotePostService postService;

    @Autowired
    private RemoteUserService userService;

    /**
     * 缓存主键命名定义
     */
    @Override
    public CacheConstants.CacheType getCacheKey() {
        return CacheConstants.CacheType.TE_TENANT_KEY;
    }

    /**
     * 新增租户 | 包含数据初始化
     *
     * @param tenantRegister 租户初始化对象
     * @return 结果
     */
    @Override
    @Transactional
    @GlobalTransactional
    public int insert(TeTenantRegister tenantRegister) {
        int rows = baseManager.insert(tenantRegister.getTenant());
        if (rows > 0) {
            TeStrategyDto strategy = strategyService.selectById(tenantRegister.getTenant().getStrategyId());
            tenantRegister.setSourceName(strategy.getSourceSlave());
            oneselfService.organizeInit(tenantRegister);
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
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void organizeInit(TeTenantRegister tenantRegister) {
        Long enterpriseId = tenantRegister.getTenant().getId();
        String sourceName = tenantRegister.getSourceName();
        tenantRegister.getDept().setParentId(TOP_ID);
        R<SysDeptDto> deptR = deptService.addInner(tenantRegister.getDept(), enterpriseId, sourceName);
        if (deptR.isFail()) {
            AjaxResult.warn("新增失败，请检查！");
        }
        tenantRegister.getPost().setDeptId(deptR.getData().getId());
        R<SysPostDto> postR = postService.addInner(tenantRegister.getPost(), enterpriseId, sourceName);
        if (postR.isFail()) {
            AjaxResult.warn("新增失败，请检查！");
        }
        tenantRegister.getUser().setPostIds(new Long[]{postR.getData().getId()});
        tenantRegister.getUser().setUserType(SecurityConstants.UserType.ADMIN.getCode());
        tenantRegister.getUser().setPassword(SecurityUserUtils.encryptPassword(tenantRegister.getUser().getPassword()));
        R<SysUserDto> userR = userService.addInner(tenantRegister.getUser(), enterpriseId, sourceName);
        if (userR.isFail()) {
            AjaxResult.warn("新增失败，请检查！");
        }
    }

    /**
     * 校验租户URL是否已存在
     * @param url  校验租户URL
     * @return 结果 | true/false
     */
    @Override
    public boolean checkDomainUnique(String url, Long id) {
      TeTenantDto tenant = baseManager.checkDomain(url);
        if (ObjectUtil.isNotNull(id)&&ObjectUtil.isNotNull(tenant)) {
            if (id.equals(tenant.getId()))
                return false;
        }
        return ObjectUtil.isNotNull(tenant);
    }
}