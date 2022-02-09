package com.xueyi.system.api.model;

import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.system.api.source.domain.Source;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * 用户信息
 *
 * @author xueyi
 */
public class LoginUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 用户唯一标识 */
    private String token;

    /** 企业账号Id */
    private Long enterpriseId;

    /** 企业账号 */
    private String enterpriseName;

    /** 租户标识 */
    private String isLessor;

    /** 用户名Id */
    private Long userId;

    /** 用户名 */
    private String userName;

    /** 用户标识 */
    private String userType;

    /** 主数据源 */
    private String sourceName;

    /** 登录时间 */
    private Long loginTime;

    /** 过期时间 */
    private Long expireTime;

    /** 登录IP地址 */
    private String ipaddr;

    /** 权限列表 */
    private Set<String> permissions;

    /** 角色权限列表 */
    private Set<String> roles;

    /** 角色Id列表 */
    private Set<Long> roleIds;

    /** 路由路径映射列表 */
    private Map<String,String> routeMap;

    /** 源策略组 */
    private Source source;

    /** 企业信息 */
    private SysEnterpriseDto enterprise;

    /** 用户信息 */
    private SysUserDto user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
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

    public Map<String, String> getRouteMap() {
        return routeMap;
    }

    public void setRouteMap(Map<String, String> routeMap) {
        this.routeMap = routeMap;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public SysEnterpriseDto getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(SysEnterpriseDto enterprise) {
        this.enterprise = enterprise;
    }

    public SysUserDto getUser() {
        return user;
    }

    public void setUser(SysUserDto user) {
        this.user = user;
    }
}
