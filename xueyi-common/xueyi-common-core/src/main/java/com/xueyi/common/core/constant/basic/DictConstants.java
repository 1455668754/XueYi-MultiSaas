package com.xueyi.common.core.constant.basic;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 字典通用常量
 *
 * @author xueyi
 */
public class DictConstants {

    /** 字典类型 */
    @Getter
    @AllArgsConstructor
    public enum DictType {

        SYS_NORMAL_DISABLE("sys_normal_disable", "系统开关列表"),
        SYS_SHOW_HIDE("sys_show_hide", "常规：显隐列表"),
        SYS_COMMON_PRIVATE("sys_common_private", "常规：公共私有列表"),
        SYS_YES_NO("sys_yes_no", "常规：是否列表"),
        SYS_USER_SEX("sys_user_sex", "用户性别列表");

        private final String code;
        private final String info;

    }

    /** 常规：是否列表（Y是 N否） */
    @Getter
    @AllArgsConstructor
    public enum DicYesNo {

        YES("Y", "是"),
        NO("N", "否");

        private final String code;
        private final String info;

    }

    /** 常规：显隐列表（0显示 1隐藏） */
    @Getter
    @AllArgsConstructor
    public enum DicShowHide {

        SHOW("0", "显示"),
        HIDE("1", "隐藏");

        private final String code;
        private final String info;

    }

    /** 常规：公共私有列表（0公共 1私有） */
    @Getter
    @AllArgsConstructor
    public enum DicCommonPrivate {

        COMMON("0", "公共"),
        PRIVATE("1", "私有");

        private final String code;
        private final String info;

    }

    /** 常规：状态列表（0正常 1失败） */
    @Getter
    @AllArgsConstructor
    public enum DicStatus {

        NORMAL("0", "正常"),
        FAIL("1", "失败");

        private final String code;
        private final String info;

    }

    /** 字典：字典数据类型列表（0默认 1只增 2只减 3只读） */
    @Getter
    @AllArgsConstructor
    public enum DicDataType {

        DEFAULT("0", "默认"),
        INCREASE("1", "只增"),
        SUBTRACT("2", "只减"),
        READ("3", "只读");

        private final String code;
        private final String info;

    }

    /** 字典：缓存类型列表（0租户 1全局） */
    @Getter
    @AllArgsConstructor
    public enum DicCacheType {

        TENANT("0", "租户"),
        OVERALL("1", "全局");

        private final String code;
        private final String info;

    }
}
