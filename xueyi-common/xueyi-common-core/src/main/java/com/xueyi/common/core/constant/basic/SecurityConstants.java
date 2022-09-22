package com.xueyi.common.core.constant.basic;

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

    /** 请求来源 */
    public static final String FROM_SOURCE = "from-source";

    /** 授权信息 */
    public static final String AUTHORIZATION_HEADER = "authorization";

    /** 企业Id */
    public static final String ENTERPRISE_ID = "enterprise_id";

    /** 企业类型 */
    public static final String IS_LESSOR = "is_lessor";

    /** 租户策略源名称 */
    public static final String SOURCE_NAME = "source_name";

    /** 内部请求 */
    public static final String INNER = "inner";

    /** 数据权限 - 创建者 */
    public static final String CREATE_BY = "create_by";

    /** 数据权限 - 更新者 */
    public static final String UPDATE_BY = "update_by";

    /** 通用安全常量 */
    public enum BaseSecurity {

        TOKEN("token", "用户唯一标识"),
        ENTERPRISE("enterprise", "企业信息"),
        ENTERPRISE_ID("enterprise_id", "企业Id"),
        ENTERPRISE_NAME("enterprise_name", "企业账号"),
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

        BaseSecurity(String code, String info) {
            this.code = code;
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }
    }

    /** 管理端安全常量 */
    public enum AdminSecurity {

        DATA_SCOPE("data_scope", "数据权限"),
        MODULE_ROUTE("module_route", "模块路由列表"),
        MENU_ROUTE("menu_route", "菜单路由列表"),
        ROUTE_URL("route_url", "路由路径映射列表");

        private final String code;
        private final String info;

        AdminSecurity(String code, String info) {
            this.code = code;
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }
    }
}
