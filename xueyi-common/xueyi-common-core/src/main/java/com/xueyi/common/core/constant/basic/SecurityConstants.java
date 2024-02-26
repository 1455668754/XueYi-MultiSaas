package com.xueyi.common.core.constant.basic;

import com.xueyi.common.core.utils.core.EnumUtil;
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

    /** {bcrypt} 加密的特征码 */
    public static final String BCRYPT = "{bcrypt}";

    /** {noop} 加密的特征码 */
    public static final String NOOP = "{noop}";


    /** 用户类型 */
    @Getter
    @AllArgsConstructor
    public enum UserType {

        NORMAL("01", "普通用户"),
        ADMIN("00", "超管用户");

        private final String code;
        private final String info;

    }

    /** 租户类型 */
    @Getter
    @AllArgsConstructor
    public enum TenantType {

        NORMAL("N", "普通租户"),
        ADMIN("Y", "租管租户");

        private final String code;
        private final String info;

    }

    /** 数据范围 */
    @Getter
    @AllArgsConstructor
    public enum DataScope {

        NONE("0", "无数据权限"),
        ALL("1", "全部数据权限"),
        CUSTOM("2", "自定义数据权限"),
        DEPT("3", "本部门数据权限"),
        DEPT_AND_CHILD("4", "本部门及以下数据权限"),
        POST("5", "本岗位数据权限"),
        SELF("6", "仅本人数据权限");

        private final String code;
        private final String info;

        public static DataScope getByCode(String code) {
            return EnumUtil.getByCode(DataScope.class, code);
        }
    }

    /** oauth参数名称 */
    @Getter
    @AllArgsConstructor
    public enum OAuth2ParameterNames {

        GRANT_TYPE("grant_type", "授权类型"),
        ACCOUNT_TYPE("account_type", "账户类型");

        private final String code;
        private final String info;
    }

    /** 认证模式 */
    @Getter
    @AllArgsConstructor
    public enum GrantType {

        AUTHORIZATION_CODE("authorization_code", "授权码模式"),
        CLIENT_CREDENTIALS("client_credentials", "客户端模式"),
        PASSWORD("password", "密码模式"),
        WECHAT_MP("wechat_mp", "微信公众号模式"),
        WECHAT_MA("wechat_ma", "微信小程序模式"),
        REFRESH_TOKEN("refresh_token", "刷新模式");

        private final String code;
        private final String info;

        public static boolean isClient(String code) {
            return StrUtil.equals(CLIENT_CREDENTIALS.code, code);
        }

    }

    /** 账户类型 */
    @Getter
    @AllArgsConstructor
    public enum AccountType {

        ADMIN("admin", "后台账户"),
        MEMBER("member", "会员账户"),
        PLATFORM("platform", "平台账户"),
        EXTERNAL("external", "外系统账户");

        private final String code;
        private final String info;

        public static SecurityConstants.AccountType getByCode(String code) {
            return EnumUtil.getByCode(SecurityConstants.AccountType.class, code);
        }

        public static SecurityConstants.AccountType getByCodeElseNull(String code) {
            return EnumUtil.getByCodeElseNull(SecurityConstants.AccountType.class, code);
        }

        /** 管理端用户 */
        public boolean isAdmin() {
            return isAdmin(code);
        }

        /** 管理端用户 */
        public static boolean isAdmin(String code) {
            return StrUtil.equals(code, ADMIN.code);
        }

        /** 会员端用户 */
        public boolean isMember() {
            return isMember(code);
        }

        /** 会员端用户 */
        public static boolean isMember(String code) {
            return StrUtil.equals(code, MEMBER.code);
        }
    }

    /** 后台账户 - 登陆参数 */
    @Getter
    @AllArgsConstructor
    public enum LoginParam {

        ENTERPRISE_NAME("enterpriseName", "企业账号"),
        USER_NAME("userName", "用户账号"),
        PASSWORD("password", "用户密码");

        private final String code;
        private final String info;

    }

    /** 会员账户 - 登陆参数 */
    @Getter
    @AllArgsConstructor
    public enum LoginMemberParam {

        CODE("code", "code"),
        SIGNATURE("signature", "signature"),
        SESSION_KEY("sessionKey", "完整用户信息的加密数据"),
        ENCRYPTED_DATA("encryptedData", "完整用户信息的加密数据"),
        iv("iv", "加密算法的初始向量"),
        SECRET("secret", "平台账户Token");

        private final String code;
        private final String info;

    }

    /** 平台账户 - 登陆参数 */
    @Getter
    @AllArgsConstructor
    public enum LoginPlatformParam {

        ENTERPRISE_ID("enterpriseId", "企业Id"),
        APP_ID("appId", "应用Id");

        private final String code;
        private final String info;

    }

    /** 通用安全常量 */
    @Getter
    @AllArgsConstructor
    public enum BaseSecurity {
        AUTHORIZATION_HEADER(SecurityConstants.AUTHORIZATION_HEADER, "授权信息"),
        SUPPLY_AUTHORIZATION_HEADER("supply-authorization", "补充授权信息"),
        CLIENT_ID("clientId", "客户端ID"),
        FROM_SOURCE(SecurityConstants.FROM_SOURCE, "请求来源"),
        ALLOW_LIST("allow-list", "白名单标识"),
        BLOCK_LIST("block-list", "黑名单标识"),
        TOKEN("token", "用户唯一标识"),
        ACCESS_TOKEN("access_token", "用户唯一标识 - 访问令牌"),
        REFRESH_TOKEN("refresh_token", "用户唯一标识 - 刷新令牌"),
        ENTERPRISE("enterprise", "企业信息"),
        ENTERPRISE_ID(SecurityConstants.ENTERPRISE_ID, "企业Id"),
        LAST_ENTERPRISE_ID("last_enterprise_id", "上一次变更企业Id"),
        ENTERPRISE_NAME("enterprise_name", "企业账号"),
        PASSWORD("password", "用户密码"),
        USER("user", "用户信息"),
        USER_ID("user_id", "用户Id"),
        USER_NAME("user_name", "用户账号"),
        NICK_NAME("nick_name", "用户昵称"),
        IS_LESSOR(SecurityConstants.IS_LESSOR, "企业类型"),
        USER_TYPE("user_type", "用户类型"),
        USER_KEY("user_key", "用户标识"),
        SOURCE("source", "租户策略源"),
        SOURCE_NAME(SecurityConstants.SOURCE_NAME, "租户策略源名称"),
        LAST_SOURCE_NAME("last_source_name", "上一次租户策略源名称"),
        USER_INFO("user_info", "登录用户"),
        ACCOUNT_TYPE("account_type", "账户类型"),
        TENANT_IGNORE("tenant_ignore", "租户控制忽略"),
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

    /** 会员端安全常量 */
    @Getter
    @AllArgsConstructor
    public enum MemberSecurity {

        APPLICATION_ID("application_id", "应用Id"),
        APP_ID("app_id", "AppId");

        private final String code;
        private final String info;

    }

    /** 平台端安全常量 */
    @Getter
    @AllArgsConstructor
    public enum PlatformSecurity {

        APP_ID("app_id", "AppId");

        private final String code;
        private final String info;

    }
}
