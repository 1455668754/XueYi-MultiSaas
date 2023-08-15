package com.xueyi.common.core.constant.basic;

import com.xueyi.common.core.utils.core.EnumUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用常量
 *
 * @author xueyi
 */
public class BaseConstants {

    /** 顶级节点Id */
    public static final Long TOP_ID = 0L;

    /** 公共节点Id */
    public static final Long COMMON_ID = 0L;

    /** 空节点Id */
    public static final Long NONE_ID = -2L;

    /** 操作类型 */
    @Getter
    @AllArgsConstructor
    public enum Operate {

        ADD("add", "新增"),
        EDIT("edit", "修改"),
        EDIT_STATUS("editStatus", "修改状态"),
        DELETE("delete", "删除");

        private final String code;
        private final String info;

        /** 是否为新增 | 强制新增 */
        public boolean isAdd() {
            return StrUtil.equalsAny(name(), Operate.ADD.name());
        }

        /** 是否为修改 | 强制修改 */
        public boolean isEdit() {
            return StrUtil.equalsAny(name(), Operate.EDIT.name());
        }

        /** 是否为修改状态 */
        public boolean isES() {
            return StrUtil.equals(name(), Operate.EDIT_STATUS.name());
        }

        /** 是否为删除 */
        public boolean isDelete() {
            return StrUtil.equals(name(), Operate.DELETE.name());
        }
    }

    /** 查询类型 */
    @Getter
    @AllArgsConstructor
    public enum SelectType {

        NORMAL("normal", "正常查询"),
        EXTRA("extra", "附加查询");

        private final String code;
        private final String info;

        /** 是否为正常查询 */
        public boolean isNormal() {
            return StrUtil.equalsAny(name(), SelectType.NORMAL.name());
        }

        /** 是否为附加查询 */
        public boolean isExtra() {
            return StrUtil.equalsAny(name(), SelectType.EXTRA.name());
        }

    }

    /** 字段映射名 */
    @Getter
    @AllArgsConstructor
    public enum Entity {

        ID("id", "Id"),
        PARENT_ID("parentId", "父级Id"),
        SORT("sort", "显示顺序"),
        CHILDREN("children", "子部门集合");

        private final String code;
        private final String info;

    }

    /** 状态 */
    @Getter
    @AllArgsConstructor
    public enum Status {

        NORMAL("0", "正常"),
        DISABLE("1", "停用"),
        EXCEPTION("1", "异常");

        private final String code;
        private final String info;

    }

    /** 系统默认值 */
    @Getter
    @AllArgsConstructor
    public enum Whether {

        YES("Y", "是"),
        NO("N", "否");

        private final String code;
        private final String info;

    }

    /** 导入类型 */
    @Getter
    @AllArgsConstructor
    public enum ImportType {

        DEFAULT("1", "默认 - 新增&&修改"),
        ONLY_INSERT("2", "仅新增"),
        ONLY_UPDATE("3", "仅修改");

        private final String code;
        private final String info;

        public static ImportType getByCode(String code) {
            return EnumUtil.getByCodeElseNull(ImportType.class, code);
        }

    }
}
