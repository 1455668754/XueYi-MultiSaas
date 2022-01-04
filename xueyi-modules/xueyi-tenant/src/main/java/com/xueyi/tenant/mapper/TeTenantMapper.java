package com.xueyi.tenant.mapper;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.tenant.domain.dto.TeTenantDto;

import java.util.List;
import java.util.Set;

/**
 * 租户管理 数据层
 *
 * @author xueyi
 */
public interface TeTenantMapper extends BaseMapper<TeTenantDto> {

    /**
     * 查询租户信息列表
     * 访问控制 e 租户查询
     *
     * @param tenant 租户信息
     * @return 租户信息集合
     */
    public List<TeTenantDto> mainSelectTenantList(TeTenantDto tenant);

    /**
     * 查询租户信息
     * 访问控制 e 租户查询
     *
     * @param tenant 租户信息
     * @return 租户信息
     */
    public TeTenantDto mainSelectTenantById(TeTenantDto tenant);

    /**
     * 新增租户信息
     * 访问控制 empty 租户更新（无前缀）()控制方法在impl层 | TeTenantServiceImpl)
     *
     * @param tenant 租户信息
     * @return 结果
     */
    public int mainInsertTenant(TeTenantDto tenant);

    /**
     * 修改租户信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenant 租户信息
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int mainUpdateTenant(TeTenantDto tenant);

    /**
     * 修改租户信息排序
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenant 租户信息
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int mainUpdateTenantSort(TeTenantDto tenant);

    /**
     * 删除租户信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenant 租户信息 | params.Ids 租户Ids组
     * @return 结果
     */
    public int mainDeleteTenantByIds(TeTenantDto tenant);

    /**
     * 查询租户Id存在于数组中的租户信息
     *
     * @param tenant 租户信息 | params.Ids 租户Ids组
     * @return 租户信息集合
     */
    public Set<TeTenantDto> mainCheckTenantListByIds(TeTenantDto tenant);

    /**
     * 校验租户账号是否唯一
     *
     * @param tenant 租户信息 | tenantName 租户Id
     * @return 租户信息
     */
    public TeTenantDto mainCheckTenantNameUnique(TeTenantDto tenant);

    /**
     * 根据租户Id查询租户信息
     *
     * @param tenant 租户信息 | tenantId 租户Id
     * @return 租户信息
     */
    public TeTenantDto mainCheckTenantByTenantId(TeTenantDto tenant);
}