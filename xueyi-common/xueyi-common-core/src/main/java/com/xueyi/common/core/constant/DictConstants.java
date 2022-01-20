package com.xueyi.common.core.constant;

/**
 * 字典通用常量
 *
 * @author xueyi
 */
public class DictConstants {

    /** 字典类型 */
    public enum DictType {

        SYS_NORMAL_DISABLE("sys_normal_disable", "系统开关列表"),
        SYS_USER_SEX("sys_user_sex", "用户性别列表");

        private final String code;
        private final String info;

        DictType(String code, String info) {
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
