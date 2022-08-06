package com.xueyi.system.api.model;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.system.AuthorityConstants;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据权限
 *
 * @author xueyi
 */
public class DataScope implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 企业账号Id */
    private Long enterpriseId;

    /** 租户标识 */
    private String isLessor;

    /** 用户名Id */
    private Long userId;

    /** 用户标识 */
    private String userType;

    /** 权限列表 */
    private Set<String> permissions;

    /** 角色权限列表 */
    private Set<String> roles;

    /** 角色Id列表 */
    private Set<Long> roleIds;

    /** 数据范围（1全部数据权限 2自定数据权限 3本部门数据权限 4本部门及以下数据权限 5本岗位数据权限  6仅本人数据权限） */
    private String dataScope;

    /** 权限控制 - 部门 */
    private Set<Long> deptScope;

    /** 权限控制 - 岗位 */
    private Set<Long> postScope;

    /** 权限控制 - 用户 */
    private Set<Long> userScope;

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getIsLessor() {
        return isLessor;
    }

    public void setIsLessor(String isLessor) {
        this.isLessor = isLessor;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public String getDataScope() {
        return dataScope;
    }

    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
    }

    public Set<Long> getDeptScope() {
        return CollUtil.isEmpty(deptScope) ? new HashSet<>() : deptScope;
    }

    public void setDeptScope(Set<Long> deptScope) {
        this.deptScope = deptScope;
    }

    public Set<Long> getPostScope() {
        return CollUtil.isEmpty(postScope) ? new HashSet<>() : postScope;
    }

    public void setPostScope(Set<Long> postScope) {
        this.postScope = postScope;
    }

    public Set<Long> getUserScope() {
        return CollUtil.isEmpty(userScope) ? new HashSet<>() : userScope;
    }

    public void setUserScope(Set<Long> userScope) {
        this.userScope = userScope;
    }

    public boolean isLessor() {
        return StrUtil.equals(AuthorityConstants.TenantType.ADMIN.getCode(), isLessor);
    }

    public boolean isAdmin() {
        return StrUtil.equals(AuthorityConstants.UserType.ADMIN.getCode(), userType);
    }
}
