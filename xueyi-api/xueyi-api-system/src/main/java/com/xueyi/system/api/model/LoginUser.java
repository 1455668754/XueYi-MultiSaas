package com.xueyi.system.api.model;

import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.system.api.source.domain.Source;

import java.io.Serializable;
import java.util.Map;

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

    /** 用户昵称 */
    private String nickName;

    /** 用户标识 */
    private String userType;

    /** 主数据源 */
    private String sourceName;

    /** 登录时间 */
    private Long loginTime;

    /** 登录IP地址 */
    private String ipaddr;

    /** 源策略组 */
    private Source source;

    /** 企业信息 */
    private SysEnterpriseDto enterprise;

    /** 用户信息 */
    private SysUserDto user;

    /** 数据权限 */
    private DataScope scope;

    /** 路由路径映射列表 */
    private Map<String, String> routeURL;

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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
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

    public DataScope getScope() {
        return scope;
    }

    public void setScope(DataScope scope) {
        this.scope = scope;
    }

    public Map<String, String> getRouteURL() {
        return routeURL;
    }

    public void setRouteURL(Map<String, String> routeURL) {
        this.routeURL = routeURL;
    }

    /** 初始化权限范围 */
    public DataScope getDataScope() {
        scope.setEnterpriseId(enterpriseId);
        scope.setUserId(userId);
        scope.setIsLessor(isLessor);
        scope.setUserType(userType);
        return scope;
    }

    /** 初始化路由路径映射列表 */
    public void initRouteURL() {
        routeURL = null;
    }
}
