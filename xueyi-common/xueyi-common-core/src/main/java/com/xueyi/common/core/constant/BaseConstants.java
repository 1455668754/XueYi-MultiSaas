package com.xueyi.common.core.constant;

import cn.hutool.core.util.StrUtil;

/**
 * 通用常量
 *
 * @author xueyi
 */
public class BaseConstants {

    /** 顶级节点Id */
    public static final Long TOP_NODE = 0L;

    /** 顶级节点Id */
    public static final Long NONE_ID = -1L;

    /** 操作类型 */
    public enum Operate {

        ADD("add", "新增"),
        ADD_FORCE("add", "强制新增"),
        EDIT("edit", "修改"),
        EDIT_FORCE("edit", "强制修改"),
        EDIT_STATUS("editStatus", "修改状态"),
        EDIT_STATUS_FORCE("editStatus", "强制修改状态"),
        DELETE("delete", "删除"),
        DELETE_FORCE("delete", "强制删除");

        private final String code;
        private final String info;

        Operate(String code, String info) {
            this.code = code;
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }

        /** 是否为新增 | 强制新增 */
        public boolean isAdd(){
            return StrUtil.equalsAny(name(), Operate.ADD.name(), Operate.ADD_FORCE.name());
        }
        /** 是否为修改 | 强制修改 */
        public boolean isEdit(){
            return StrUtil.equalsAny(name(), Operate.EDIT.name(), Operate.EDIT_FORCE.name());
        }
        /** 是否为修改状态 | 强制修改状态 */
        public boolean isEditStatus(){
            return StrUtil.equalsAny(name(), Operate.EDIT_STATUS.name(), Operate.EDIT_STATUS_FORCE.name());
        }
        /** 是否为删除 | 强制删除 */
        public boolean isDelete(){
            return StrUtil.equalsAny(name(), Operate.DELETE.name(), Operate.DELETE_FORCE.name());
        }
    }

    /** 字段映射名 */
    public enum Entity {

        ID("id", "Id"),
        PARENT_ID("parentId", "父级Id"),
        SORT("sort", "显示顺序"),
        CHILDREN("children", "子部门集合");

        private final String code;
        private final String info;

        Entity(String code, String info) {
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

    /** 状态 */
    public enum Status {

        NORMAL("0", "正常"),
        DISABLE("1", "停用"),
        EXCEPTION("1", "异常");

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

    /** 系统默认值 */
    public enum Whether {

        YES("Y", "是"),
        NO("N", "否");

        private final String code;
        private final String info;

        Whether(String code, String info) {
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
