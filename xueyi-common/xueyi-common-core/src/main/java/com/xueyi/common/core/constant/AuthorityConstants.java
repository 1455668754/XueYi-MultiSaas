package com.xueyi.common.core.constant;

import com.xueyi.common.core.utils.StringUtils;

/**
 * 权限通用常量
 *
 * @author xueyi
 */
public class AuthorityConstants {

    /** 公共企业Id */
    public static final Long COMMON_ENTERPRISE = 0L;

    /** 公共企业Id */
    public static final Long COMMON_USER = 0L;

    /** 菜单树 - 顶级节点Id */
    public static final Long MENU_TOP_NODE = 0L;

    /** 默认模块Id */
    public static final Long MODULE_DEFAULT_NODE = 0L;

    /** 用户类型 */
    public enum UserType {

        NORMAL("01", "普通用户"), ADMIN("00", "超管用户");

        private final String code;
        private final String info;

        UserType(String code, String info) {
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

    /** 租户类型 */
    public enum TenantType {

        NORMAL("N", "普通租户"), ADMIN("Y", "租管租户");

        private final String code;
        private final String info;

        TenantType(String code, String info) {
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

    /** 公共数据 */
    public enum IsCommon {

        YES("Y", "是"), NO("N", "否");

        private final String code;
        private final String info;

        IsCommon(String code, String info) {
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

    /** 数据范围 */
    public enum DataScope {

        ALL("1","全部数据权限"),
        CUSTOM("2","自定义数据权限"),
        DEPT("3","本部门数据权限"),
        DEPT_AND_CHILD("4","本部门及以下数据权限"),
        POST("5","本岗位数据权限"),
        SELF("6","仅本人数据权限");

        private final String code;
        private final String info;

        DataScope(String code, String info) {
            this.code = code;
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }

        public static DataScope getValue(String code){
            for(DataScope scope : values()){
                if(StringUtils.equals(code, scope.getCode())){
                    return scope;
                }
            }
            return null;
        }
    }

    /** 显隐状态 */
    public enum Hide {

        YES("Y", "隐藏"), NO("N", "显示");

        private final String code;
        private final String info;

        Hide(String code, String info) {
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

    /** 显隐状态 */
    public enum Status {

        YES("Y", "是"), NO("N", "否");

        private final String code;
        private final String info;

        Status(String code, String info) {
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

    /** 是否缓存 */
    public enum Cache {

        YES("Y", "是"), NO("N", "否");

        private final String code;
        private final String info;

        Cache(String code, String info) {
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

    /** 是否菜单外链 */
    public enum Frame {

        YES("Y", "是"), NO("N", "否");

        private final String code;
        private final String info;

        Frame(String code, String info) {
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

    /** 页面类型 */
    public enum FrameType {

        NORMAL("0", "正常"), EMBEDDED("1", "内嵌"), EXTERNAL_LINKS("2", "外链");

        private final String code;
        private final String info;

        FrameType(String code, String info) {
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

    /** 菜单类型 */
    public enum MenuType {

        DIR("M", "目录"), MENU("C", "菜单"), DETAILS("X", "详情"), BUTTON("F", "按钮");

        private final String code;
        private final String info;

        MenuType(String code, String info) {
            this.code = code;
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }

        public static MenuType getValue(String code) {
            for (MenuType one : values())
                if (StringUtils.equals(code, one.getCode()))
                    return one;
            return null;
        }
    }

    /** 组件标识 */
    public enum ComponentType {

        LAYOUT("LAYOUT", "Layout"), PARENT_VIEW("ParentView", "ParentView"), INNER_LINK("InnerLink", "InnerLink");

        private final String code;
        private final String info;

        ComponentType(String code, String info) {
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