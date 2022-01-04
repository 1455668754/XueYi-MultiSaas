package com.xueyi.tenant.service;

import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.tenant.domain.dto.TeTenantDto;

/**
 * 租户管理 服务层
 *
 * @author xueyi
 */
public interface ITeTenantService extends IBaseService<TeTenantDto> {

//    /**
//     * 查询租户信息列表
//     *
//     * @param tenant 租户信息
//     * @return 租户信息集合
//     */
//    public List<TeTenantDto> mainSelectTenantList(TeTenantDto tenant);
//
//    /**
//     * 查询租户信息
//     *
//     * @param tenant 租户信息
//     * @return 租户信息
//     */
//    public TeTenantDto mainSelectTenantById(TeTenantDto tenant);
//
//    /**
//     * 新增租户信息
//     *
//     * @param tenant 租户信息
//     * @return 结果
//     */
//    public int mainInsertTenant(TeTenantDto tenant);
//
//    /**
//     * 注册租户信息
//     *
//     * @param tenant 租户信息
//     * @return 结果
//     */
//    public int mainRegisterTenant(TeTenantDto tenant);
//
//    /**
//     * 修改租户信息
//     *
//     * @param tenant 租户信息
//     * @return 结果
//     */
//    public int mainUpdateTenant(TeTenantDto tenant);
//
//    /**
//     * 修改租户信息排序
//     *
//     * @param tenant 租户信息
//     * @return 结果
//     */
//    public int mainUpdateTenantSort(TeTenantDto tenant);
//
//    /**
//     * 批量删除租户信息
//     *
//     * @param tenant 租户信息
//     * @return 结果
//     */
//    public int mainDeleteTenantByIds(TeTenantDto tenant);
//
//    /**
//     * 查询租户Id存在于数组中的租户信息
//     *
//     * @param tenant 租户信息 | params.Ids 租户Ids组
//     * @return 租户信息集合
//     */
//    public Set<TeTenantDto> mainCheckTenantListByIds(TeTenantDto tenant);
//
//    /**
//     * 校验租户账号是否唯一
//     *
//     * @param tenant 租户信息 | tenantName 租户Id
//     * @return 结果
//     */
//    public String mainCheckTenantNameUnique(TeTenantDto tenant);
//
//    /**
//     * 根据租户Id查询租户信息
//     *
//     * @param tenant 租户信息 | tenantId 租户Id
//     * @return 租户信息
//     */
//    public TeTenantDto mainCheckTenantByTenantId(TeTenantDto tenant);
}