package com.xueyi.tenant.mapper;

import com.xueyi.tenant.domain.dto.TeTenantDto;


/**
 * 租户新增同步创建信息 数据层
 *
 * @author xueyi
 */
public interface TeCreationMapper {

    /**
     * 创建新租户部门
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenant 租户管理
     * @return 结果
     */
    public int createDeptByTenantId(TeTenantDto tenant);

    /**
     * 创建新租户岗位
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenant 租户管理
     * @return 结果
     */
    public int createPostByTenantId(TeTenantDto tenant);

    /**
     * 创建新租户员工
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenant 租户管理
     * @return 结果
     */
    public int createUserByTenantId(TeTenantDto tenant);

    /**
     * 创建新企业衍生角色
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenant 租户管理
     * @return 结果
     */
    public int createRoleByTenantId(TeTenantDto tenant);

    /**
     * 创建新租户组织-衍生角色关联
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param tenant 租户管理
     * @return 结果
     */
    public int createOrganizeRoleByTenantId(TeTenantDto tenant);
}