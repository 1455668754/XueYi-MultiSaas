package com.xueyi.common.core.constant.basic;

import com.xueyi.common.core.utils.core.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 安全相关通用常量
 *
 * @author xueyi
 */
public class SecurityConstants {

    /** 空用户Id */
    public static final Long EMPTY_USER_ID = BaseConstants.NONE_ID;

    /** 空租户Id */
    public static final Long EMPTY_TENANT_ID = BaseConstants.NONE_ID;

    /** 公共数据租户Id */
    public static final Long COMMON_TENANT_ID = BaseConstants.COMMON_ID;

    /** 租管角色标识 */
    public static final String ROLE_ADMINISTRATOR = "administrator";

    /** 超管角色标识 */
    public static final String ROLE_ADMIN = "admin";

    /** 超管菜单标识 */
    public static final String PERMISSION_ADMIN = "*:*:*";

    /** 请求来源 */
    public static final String FROM_SOURCE = "from-source";

    /** 内部请求 */
    public static final String INNER = "inner";

    /** 请求来源 */
    public static final String FROM_SOURCE_INNER = FROM_SOURCE + "=" + INNER;

    /** 授权信息 */
    public static final String AUTHORIZATION_HEADER = "authorization";

    /** 企业Id */
    public static final String ENTERPRISE_ID = "enterprise_id";

    /** 企业类型 */
    public static final String IS_LESSOR = "is_lessor";

    /** 租户策略源名称 */
    public static final String SOURCE_NAME = "source_name";

    /** 数据权限 - 创建者 */
    public static final String CREATE_BY = "create_by";

    /** 数据权限 - 更新者 */
    public static final String UPDATE_BY = "update_by";

    /** 项目的license */
    public static final String PROJECT_LICENSE = "https://xueyitt.com";

    /** 授权码模式confirm */
    public static final String CUSTOM_CONSENT_PAGE_URI = "/token/confirm_access";

    /** 认证模式 */
    @Getter
    @AllArgsConstructor
    public enum OauthType {

        AUTHORIZATION_CODE("authorization_code", "授权码模式"),
        CLIENT_CREDENTIALS("client_credentials", "客户端模式"),
        PASSWORD("password", "密码模式"),
        REFRESH_TOKEN("refresh_token", "刷新模式");

        private final String code;
        private final String info;

        public static boolean isClient(String code) {
            return StrUtil.equals(CLIENT_CREDENTIALS.code, code);
        }

    }

    /** 登陆参数 */
    @Getter
    @AllArgsConstructor
    public enum LoginParam {

        ENTERPRISE_NAME("enterpriseName", "企业账号"),
        USER_NAME("userName", "用户账号"),
        PASSWORD("password", "用户密码");

        private final String code;
        private final String info;

    }

    /** 通用安全常量 */
    @Getter
    @AllArgsConstructor
    public enum BaseSecurity {
        CLIENT_ID("clientId", "客户端ID"),
        FROM_SOURCE("from-source", "请求来源"),
        ALLOW_LIST("allow-list", "白名单标识"),
        BLOCK_LIST("block-list", "黑名单标识"),
        TOKEN("token", "用户唯一标识"),
        ENTERPRISE("enterprise", "企业信息"),
        ENTERPRISE_ID("enterprise_id", "企业Id"),
        ENTERPRISE_NAME("enterprise_name", "企业账号"),
        PASSWORD("password", "用户密码"),
        USER("user", "用户信息"),
        USER_ID("user_id", "用户Id"),
        USER_NAME("user_name", "用户账号"),
        NICK_NAME("nick_name", "用户昵称"),
        IS_LESSOR("is_lessor", "企业类型"),
        USER_TYPE("user_type", "用户类型"),
        USER_KEY("user_key", "用户标识"),
        SOURCE("source", "租户策略源"),
        SOURCE_NAME("source_name", "租户策略源名称"),
        LOGIN_USER("login_user", "登录用户"),
        ACCOUNT_TYPE("account_type", "账户类型"),
        EXPIRE_TIME("expire_time", "过期时间");

        private final String code;
        private final String info;

    }

    /** 管理端安全常量 */
    @Getter
    @AllArgsConstructor
    public enum AdminSecurity {

        DATA_SCOPE("data_scope", "数据权限"),
        MODULE_ROUTE("module_route", "模块路由列表"),
        MENU_ROUTE("menu_route", "菜单路由列表"),
        ROUTE_URL("route_url", "路由路径映射列表");

        private final String code;
        private final String info;

    }

}
