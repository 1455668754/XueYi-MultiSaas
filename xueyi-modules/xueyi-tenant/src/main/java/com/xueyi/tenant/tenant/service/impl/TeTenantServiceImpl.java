package com.xueyi.tenant.tenant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.DictConstants;
import com.xueyi.common.core.constant.SecurityConstants;
import com.xueyi.common.core.domain.R;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.system.api.organize.feign.RemoteDeptService;
import com.xueyi.system.api.organize.feign.RemotePostService;
import com.xueyi.system.api.organize.feign.RemoteUserService;
import com.xueyi.tenant.api.tenant.domain.dto.TeStrategyDto;
import com.xueyi.tenant.api.tenant.domain.dto.TeTenantDto;
import com.xueyi.tenant.tenant.domain.model.TeTenantRegister;
import com.xueyi.tenant.tenant.manager.TeTenantManager;
import com.xueyi.tenant.tenant.mapper.TeTenantMapper;
import com.xueyi.tenant.tenant.service.ITeStrategyService;
import com.xueyi.tenant.tenant.service.ITeTenantService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.xueyi.common.core.constant.TenantConstants.MASTER;
import static com.xueyi.common.core.constant.TenantConstants.SOURCE;

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

    @Autowired
    private ITeStrategyService strategyService;

    @Autowired
    private RemoteDeptService deptService;

    @Autowired
    private RemotePostService postService;

    @Autowired
    private RemoteUserService userService;

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
     */
    @Override
    @DS(SOURCE)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void organizeInit(TeTenantRegister tenantRegister) {
        Long enterpriseId = tenantRegister.getTenant().getId();
        String sourceName = tenantRegister.getTenant().getSourceName();
        tenantRegister.getDept().setEnterpriseId(enterpriseId);
        tenantRegister.getDept().setSourceName(sourceName);
        R<SysDeptDto> deptR = deptService.add(tenantRegister.getDept(), SecurityConstants.INNER);
        if(deptR.isFail())
            throw new ServiceException("新增失败！");
        tenantRegister.getPost().setEnterpriseId(enterpriseId);
        tenantRegister.getPost().setSourceName(sourceName);
        tenantRegister.getPost().setDeptId(deptR.getResult().getId());
        R<SysPostDto> postR = postService.add(tenantRegister.getPost(), SecurityConstants.INNER);
        if(postR.isFail())
            throw new ServiceException("新增失败！");
        tenantRegister.getUser().setEnterpriseId(enterpriseId);
        tenantRegister.getUser().setSourceName(sourceName);
        tenantRegister.getUser().setPostIds(new Long[]{postR.getResult().getId()});
        R<SysUserDto> userR = userService.add(tenantRegister.getUser(), SecurityConstants.INNER);
        if(userR.isFail())
            throw new ServiceException("新增失败！");
    }

    /**
     * 租户权限数据初始化
     *
     * @param tenantRegister 租户初始化对象
     */
    @Override
    public void authorityInit(TeTenantRegister tenantRegister) {

    }
}