package com.xueyi.tenant.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.tenant.domain.dto.TeTenantDto;
import com.xueyi.tenant.manager.TeTenantManager;
import com.xueyi.tenant.mapper.TeTenantMapper;
import com.xueyi.tenant.service.ITeCreationService;
import com.xueyi.tenant.service.ITeTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 租户管理 服务层处理
 *
 * @author xueyi
 */
@Service
@DS("#main")
public class TeTenantServiceImpl extends BaseServiceImpl<TeTenantDto, TeTenantManager, TeTenantMapper> implements ITeTenantService {

    @Autowired
    private TeTenantMapper teTenantMapper;

    @Autowired
    private ITeTenantService tenantService;

    @Autowired
    private ITeCreationService creationService;

//    /**
//     * 查询租户信息列表
//     *
//     * @param tenant 租户信息
//     * @return 租户信息
//     */
//    @Override
//    public List<TeTenantDto> mainSelectTenantList(TeTenantDto tenant) {
//        return teTenantMapper.mainSelectTenantList(tenant);
//    }
//
//    /**
//     * 查询租户信息
//     *
//     * @param tenant 租户信息
//     * @return 租户信息
//     */
//    @Override
//    public TeTenantDto mainSelectTenantById(TeTenantDto tenant) {
//        return teTenantMapper.mainSelectTenantById(tenant);
//    }
//
//    /**
//     * 新增租户信息
//     *
//     * @param tenant 租户信息
//     * @return 结果
//     */
//    @Override
//    @DSTransactional
//    @GlobalTransactional
//    @DataScope(ueAlias = "empty")
//    public int mainInsertTenant(TeTenantDto tenant) {
//        /* 获取生成雪花Id，并赋值给主键，加入至子表对应外键中 */
//        tenant.setTenantId(tenant.getSnowflakeId());
//        String mainSourceName = SourceUtils.getMainSourceNameByStrategyId(tenant.getStrategyId());
//        if (StringUtils.isNotEmpty(mainSourceName)) {
//            tenant.setSourceName(mainSourceName);
//            //新建租户时同步新建信息
//            //1.新建租户的部门|岗位|超管用户信息
//            creationService.organizeCreation(tenant);
//            //1.新建租户的衍生角色&&模块|菜单屏蔽信息
//            creationService.deriveRoleCreation(tenant);
//            return teTenantMapper.mainInsertTenant(tenant);
//        }
//        return 0;
//    }
//
//    /**
//     * 注册租户信息
//     *
//     * @param tenant 租户信息
//     * @return 结果
//     */
//    @Override
//    public int mainRegisterTenant(TeTenantDto tenant) {
//        return tenantService.mainInsertTenant(tenant);
//    }
//
//    /**
//     * 修改租户信息
//     *
//     * @param tenant 租户信息
//     * @return 结果
//     */
//    @Override
//    public int mainUpdateTenant(TeTenantDto tenant) {
//        return teTenantMapper.mainUpdateTenant(tenant);
//    }
//
//    /**
//     * 修改租户信息排序
//     *
//     * @param tenant 租户信息
//     * @return 结果
//     */
//    @Override
//    public int mainUpdateTenantSort(TeTenantDto tenant) {
//        return teTenantMapper.mainUpdateTenantSort(tenant);
//    }
//
//    /**
//     * 批量删除租户信息
//     *
//     * @param tenant 租户信息
//     * @return 结果
//     */
//    @Override
//    public int mainDeleteTenantByIds(TeTenantDto tenant) {
//        return teTenantMapper.mainDeleteTenantByIds(tenant);
//    }
//
//    /**
//     * 查询租户Id存在于数组中的租户信息
//     *
//     * @param tenant 租户信息 | params.Ids 租户Ids组
//     * @return 租户信息集合
//     */
//    public Set<TeTenantDto> mainCheckTenantListByIds(TeTenantDto tenant) {
//        return teTenantMapper.mainCheckTenantListByIds(tenant);
//    }
//
//    /**
//     * 校验租户账号是否唯一
//     *
//     * @param tenant 租户信息 | tenantName 租户Id
//     * @return 结果
//     */
//    @Override
//    public String mainCheckTenantNameUnique(TeTenantDto tenant) {
//        TeTenantDto info = teTenantMapper.mainCheckTenantNameUnique(tenant);
//        if (StringUtils.isNotNull(info)) {
//            return BaseConstants.Check.NOT_UNIQUE.getCode();
//        }
//        return BaseConstants.Check.UNIQUE.getCode();
//    }
//
//    /**
//     * 根据租户Id查询租户信息
//     *
//     * @param tenant 租户信息 | tenantId 租户Id
//     * @return 租户信息
//     */
//    @Override
//    public TeTenantDto mainCheckTenantByTenantId(TeTenantDto tenant) {
//        return teTenantMapper.mainCheckTenantByTenantId(tenant);
//    }
}