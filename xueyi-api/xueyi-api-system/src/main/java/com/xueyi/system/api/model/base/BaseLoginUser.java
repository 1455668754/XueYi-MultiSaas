package com.xueyi.system.api.model.base;

import com.xueyi.common.core.constant.basic.TenantConstants;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import com.xueyi.system.api.source.domain.Source;
import lombok.Data;

import java.io.Serializable;

/**
 * 默认用户信息
 *
 * @author xueyi
 */
@Data
public class BaseLoginUser<User> implements Serializable {

    private static final long serialVersionUID = 1L;

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

}
