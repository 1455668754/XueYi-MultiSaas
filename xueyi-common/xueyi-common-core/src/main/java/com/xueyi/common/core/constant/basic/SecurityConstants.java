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

    /** 用户唯一标识 */
    public static final String TOKEN = "token";

    /** 企业信息 */
    public static final String ENTERPRISE = "enterprise";

    /** 企业Id */
    public static final String ENTERPRISE_ID = "enterprise_id";

    /** 企业账号 */
    public static final String ENTERPRISE_NAME = "enterprise_name";

    /** 用户信息 */
    public static final String USER = "user";

    /** 用户Id */
    public static final String USER_ID = "user_id";

    /** 用户账号 */
    public static final String USER_NAME = "user_name";

    /** 用户昵称 */
    public static final String NICK_NAME = "nick_name";

    /** 企业类型 */
    public static final String IS_LESSOR = "is_lessor";

    /** 用户类型 */
    public static final String USER_TYPE = "user_type";

    /** 用户标识 */
    public static final String USER_KEY = "user_key";

    /** 租户策略源 */
    public static final String SOURCE = "source";

    /** 租户策略源名称 */
    public static final String SOURCE_NAME = "source_name";

    /** 登录用户 */
    public static final String LOGIN_USER = "login_user";

    /** 数据权限 */
    public static final String DATA_SCOPE = "data_scope";

    /** 模块路由列表 */
    public static final String MODULE_ROUTE = "module_route";

    /** 菜单路由列表 */
    public static final String MENU_ROUTE = "menu_route";

    /** 路由路径映射列表 */
    public static final String ROUTE_URL = "route_url";

    /** 过期时间 */
    public static final String EXPIRE_TIME = "expire_time";

    /** 内部请求 */
    public static final String INNER = "inner";

    /** 数据权限 - 创建者 */
    public static final String CREATE_BY = "create_by";

    /** 数据权限 - 更新者 */
    public static final String UPDATE_BY = "update_by";
}
