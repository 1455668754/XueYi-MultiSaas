package com.xueyi.system.api.model.base;

import com.xueyi.common.core.constant.basic.TenantConstants;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import com.xueyi.system.api.source.domain.Source;

import java.io.Serializable;

/**
 * 默认用户信息
 *
 * @author xueyi
 */
public class BaseLoginUser<User> implements Serializable {

    /** 用户唯一标识 */
    protected String token;

    /** 企业账号Id */
    protected Long enterpriseId;

    /** 企业账号 */
    protected String enterpriseName;

    /** 用户名Id */
    protected Long userId;

    /** 用户名 */
    protected String userName;

    /** 用户昵称 */
    protected String nickName;

    /** 租户标识 */
    protected String isLessor;

    /** 用户标识 */
    protected String userType;

    /** 主数据源 */
    protected String sourceName;

    /** 登录时间 */
    protected Long loginTime;

    /** 登录IP地址 */
    protected String ipaddr;

    /** 源策略组 */
    protected Source source;

    /** 企业信息 */
    protected SysEnterpriseDto enterprise;

    /** 用户信息 */
    protected User user;

    /** 账户类型 */
    protected TenantConstants.AccountType accountType;

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

    public String getIsLessor() {
        return isLessor;
    }

    public void setIsLessor(String isLessor) {
        this.isLessor = isLessor;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TenantConstants.AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(TenantConstants.AccountType accountType) {
        this.accountType = accountType;
    }
}
