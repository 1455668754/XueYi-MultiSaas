package com.xueyi.common.core.constant.basic;

import com.xueyi.common.core.utils.core.EnumUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 租户通用常量
 *
 * @author xueyi
 */
public class TenantConstants {

    /** 租户字段名 */
    public static final String TENANT_ID = "tenant_id";

    /** 公共字段名 */
    public static final String COMMON_ID = "is_common";

    /** 注册租户默认策略Id */
    public static final Long REGISTER_TENANT_STRATEGY_ID = BaseConstants.COMMON_ID;

    /** 子库必须数据表 */
    public static final String[] SLAVE_TABLE = {"sys_dept", "sys_login_log", "sys_notice", "sys_job_log", "sys_notice_log", "sys_operate_log", "sys_organize_role_merge", "sys_post", "sys_role",
            "sys_role_menu_merge", "sys_role_module_merge", "sys_tenant_menu_merge", "sys_tenant_module_merge", "sys_role_dept_merge", "sys_role_post_merge", "sys_user", "sys_user_post_merge", "xy_material", "xy_material_folder"};

    /** 策略源标识 */
    public static final String ISOLATE = "#isolute";

    /** 主数据源标识 */
    public static final String MASTER = "#master";

    /** 手动数据源标识（调用对象中的sourceName属性） */
    public static final String SOURCE = "#sourceName";

    /** 数据源 */
    @Getter
    @AllArgsConstructor
    public enum Source {

        MASTER("master", "默认数据源"), SLAVE("slave", "从数据源"), REGISTER("slave", "注册数据源");

        private final String code;
        private final String info;

    }

    /** 源同步策略类型 */
    @Getter
    @AllArgsConstructor
    public enum SyncType {

        UNCHANGED("0", "不变"), REFRESH("1", "刷新"), ADD("2", "新增"), DELETE("3", "删除");

        private final String code;
        private final String info;

    }

    /** 用户类型 */
    @Getter
    @AllArgsConstructor
    public enum AccountType {

        ADMIN("admin", "后台用户"),
        MEMBER("member", "会员用户");

        private final String code;
        private final String info;

        public static AccountType getByCode(String code) {
            return Objects.requireNonNull(EnumUtil.getByCode(AccountType.class, code));
        }

        /** 管理端用户 */
        public static boolean isAdmin(String code) {
            return StrUtil.equals(code, ADMIN.code);
        }

        /** 管理端用户 */
        public boolean isAdmin() {
            return StrUtil.equals(code, ADMIN.code);
        }

        /** 会员端用户 */
        public static boolean isMember(String code) {
            return StrUtil.equals(code, MEMBER.code);
        }

        /** 会员端用户 */
        public boolean isMember() {
            return StrUtil.equals(code, MEMBER.code);
        }
    }

}
