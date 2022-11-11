package com.xueyi.common.core.constant.system;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.utils.core.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 权限通用常量
 *
 * @author xueyi
 */
public class AuthorityConstants {

    /** 菜单树 - 顶级节点Id */
    public static final Long MENU_TOP_NODE = BaseConstants.TOP_ID;

    /** 默认模块Id */
    public static final Long MODULE_DEFAULT_NODE = 1L;

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
            return Objects.requireNonNull(EnumUtil.getByCode(DataScope.class, code));
        }

    }

    /** 页面类型 */
    @Getter
    @AllArgsConstructor
    public enum FrameType {

        NORMAL("0", "正常"),
        EMBEDDED("1", "内嵌"),
        EXTERNAL_LINKS("2", "外链");

        private final String code;
        private final String info;

    }

    /** 菜单类型 */
    @Getter
    @AllArgsConstructor
    public enum MenuType {

        DIR("M", "目录"),
        MENU("C", "菜单"),
        DETAILS("X", "详情"),
        BUTTON("F", "按钮");

        private final String code;
        private final String info;

        public static MenuType getByCode(String code) {
            return Objects.requireNonNull(EnumUtil.getByCode(MenuType.class, code));
        }

    }

    /** 权限类型 */
    @Getter
    @AllArgsConstructor
    public enum AuthorityType {

        MODULE("0", "模块"),
        MENU("1", "菜单");

        private final String code;
        private final String info;

    }
}