package com.xueyi.system.api.authority.constant;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.utils.core.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
            return EnumUtil.getByCode(MenuType.class, code);
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