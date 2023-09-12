package com.xueyi.common.core.web.model;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 默认用户信息
 *
 * @author xueyi
 */
@Data
public class BaseLoginUser<User> implements UserDetails, OAuth2AuthenticatedPrincipal {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 用户唯一标识 - 访问令牌 */
    protected String accessToken;

    /** 用户唯一标识 - 刷新令牌 */
    protected String refreshToken;

    /** 用户唯一标识 - 令牌 */
    protected String stateToken;

    /** 用户唯一标识 - 令牌 */
    protected String codeToken;

    /** 企业账号Id */
    protected Long enterpriseId;

    /** 企业账号 */
    protected String enterpriseName;

    /** 用户名Id */
    protected Long userId;

    /** 用户名 */
    protected String userName;

    /** 密码 */
    protected String password;

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
    protected SysSource source;

    /** 企业信息 */
    protected SysEnterprise enterprise;

    /** 用户信息 */
    protected User user;

    /** 账户类型 */
    protected SecurityConstants.AccountType accountType;

    private Set<GrantedAuthority> authorities;

    private boolean accountNonExpired = true;

    private boolean accountNonLocked = true;

    private boolean credentialsNonExpired = true;

    private boolean enabled = true;

    public String getUserName() {
        return userName;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return new HashMap<>();
    }

    @Override
    public String getName() {
        return userName;
    }

    /** 初始化企业信息 */
    public void initEnterprise(SysEnterprise enterprise) {
        setEnterprise(enterprise);
        setEnterpriseId(enterprise.getId());
        setEnterpriseName(enterprise.getName());
        setIsLessor(enterprise.getIsLessor());
    }

    /** 初始化数据源信息 */
    public void initSource(SysSource source) {
        setSource(source);
        setSourceName(source.getMaster());
    }
}
