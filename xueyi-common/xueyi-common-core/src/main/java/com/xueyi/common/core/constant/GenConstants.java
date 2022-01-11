package com.xueyi.common.core.constant;

import com.xueyi.common.core.utils.StringUtils;

/**
 * 代码生成通用常量
 *
 * @author xueyi
 */
public class GenConstants {

    /** 数据库字符串类型 */
    public static final String[] COLUMN_TYPE_STR = {"char", "varchar", "nvarchar", "varchar2"};

    /** 数据库文本类型 */
    public static final String[] COLUMN_TYPE_TEXT = {"tinytext", "text", "mediumtext", "longtext"};

    /** 数据库日期类型 */
    public static final String[] COLUMN_TYPE_DATE = {"datetime", "time", "date", "timestamp"};

//    /** 数据库时间类型 */
//    public static final String[] COLUMN_TYPE_TIME = { "datetime", "time", "date", "timestamp" };

    /** 数据库数字类型 */
    public static final String[] COLUMN_TYPE_NUMBER = {"tinyint", "smallint", "mediumint", "int", "number", "integer"};

    /** 数据库长数字类型 */
    public static final String[] COLUMN_TYPE_LONG = {"bigint"};

    /** 数据库浮点类型 */
    public static final String[] COLUMN_TYPE_FLOAT = {"float", "double", "decimal"};

    /** 页面不需要编辑字段 */
    public static final String[] COLUMN_NAME_NOT_INSERT = {"id", "createBy", "createTime", "updateBy", "updateTime"};

    /** 页面不需要查看字段 */
    public static final String[] COLUMN_NAME_NOT_VIEW = {"id", "createBy", "createTime", "updateBy", "updateTime"};

    /** 页面不需要编辑字段 */
    public static final String[] COLUMN_NAME_NOT_EDIT = {"id", "createBy", "createTime", "updateBy", "updateTime"};

    /** 页面不需要显示的列表字段 */
    public static final String[] COLUMN_NAME_NOT_LIST = {"id", "createBy", "createTime", "updateBy", "updateTime", "remark"};

    /** 页面不需要查询字段 */
    public static final String[] COLUMN_NAME_NOT_QUERY = {"id", "sort", "createBy", "createTime", "updateBy", "updateTime", "remark"};

    /** 后端base基类字段 */
    public static final String[] BASE_ENTITY = {"id", "name", "status", "sort", "remark", "createBy", "createTime", "updateBy", "updateTime", "delFlag"};

    /** 后端tree基类字段 */
    public static final String[] TREE_ENTITY = {"parentId", "ancestors"};

    /** 后端sub基类字段 */
    public static final String[] SUB_ENTITY = {};

    /** 后端tenant字段 */
    public static final String[] TENANT_ENTITY = {"tenantId"};

    /** 前端base基类字段 */
    public static final String[] BASE_FRONT_ENTITY = {"createBy", "createName", "createTime", "updateBy", "updateName", "updateTime", "delFlag"};

    /** 前端tree基类字段 */
    public static final String[] TREE_FRONT_ENTITY = {"parentId", "ancestors"};

    /** 前端sub基类字段 */
    public static final String[] SUB_FRONT_ENTITY = {};

    /** 必定隐藏字段（前后端均隐藏） */
    public static final String[] COLUMN_MUST_HIDE = {"delFlag", "tenantId"};

    /** 字典名称转换移除匹配字段 */
    public static final String[] DICT_TYPE_REMOVE = {"sys_", "te_", "gen_"};

    /** 字典名称结尾字段 */
    public static final String DICT_NAME_ENDING = "Options";

    /** 状态（Y是 N否） */
    public enum Status {

        TRUE("Y", "是"), FALSE("N", "否");

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

    /** 覆写字段 */
    public enum CoverField {

        ID("id", "id", "id"),
        NAME("name", "名称", "name"),
        STATUS("status", "状态（0正常 1停用）", "（0正常 1停用）"),
        SORT("sort", "显示顺序", "sort"),
        PARENT_ID("parentId", "父节点", "parentId"),
        ANCESTORS("ancestors", "祖籍列表", "ancestors");

        private final String code;
        private final String info;
        private final String key;

        CoverField(String code, String info,String key) {
            this.code = code;
            this.info = info;
            this.key = key;
        }

        public String getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }

        public String getKey() {
            return key;
        }
    }

    /** 其它生成选项字段 */
    public enum OptionField {

        SUB_TABLE_ID("subTableId", "关联子表的表名Id字段"),
        FOREIGN_ID("foreignId", "外键关联的主表字段Id字段"),
        SUB_FOREIGN_ID("subForeignId", "关联子表的外键名Id字段"),
        COVER_TREE_ID("isCoverTreeCode", "覆写树编码（Y是 N否）"),
        TREE_ID("treeCode", "树编码Id字段"),
        COVER_PARENT_ID("isCoverParentId", "覆写树父编码（Y是 N否）"),
        PARENT_ID("parentId", "树父编码Id字段"),
        COVER_TREE_NAME_ID("isCoverTreeNameId", "覆写树名称（Y是 N否）"),
        TREE_NAME_ID("treeNameId", "树名称Id字段"),
        COVER_ANCESTORS("isCoverAncestors", "覆写祖籍列表（Y是 N否）"),
        ANCESTORS("ancestors", "祖籍列表Id字段"),
        PARENT_MODULE_ID("parentModuleId", "归属模块Id字段"),
        PARENT_MENU_ID("parentMenuId", "归属菜单Id字段"),
        COVER_ID("isCoverId", "覆写Id（Y是 N否）"),
        ID("id", "主键Id字段"),
        COVER_NAME("isCoverName", "覆写name（Y是 N否）"),
        NAME("name", "名称Id字段"),
        COVER_STATUS("isCoverStatus", "覆写status（Y是 N否）"),
        STATUS("status", "状态Id字段"),
        COVER_SORT("isCoverSort", "覆写sort（Y是 N否）"),
        SORT("sort", "序号Id字段"),
        IS_TENANT("isTenant", "多租户"),
        SOURCE_MODE("sourceMode", "源策略模式");

        private final String code;
        private final String info;

        OptionField(String code, String info) {
            this.code = code;
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }

        public static OptionField getValue(String code) {
            for (OptionField one : values())
                if (StringUtils.equals(code, one.getCode()))
                    return one;
            return null;
        }
    }

    /** 表模板类型 */
    public enum TemplateType {

        BASE("base", "单表"), TREE("tree", "树表"), SUB_BASE("subBase", "主子表"), SUB_TREE("subTree", "主子树表"), MERGE("merge", "关联表");

        private final String code;
        private final String info;

        TemplateType(String code, String info) {
            this.code = code;
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }

        public static TemplateType getValue(String code) {
            for (TemplateType one : values())
                if (StringUtils.equals(code, one.getCode()))
                    return one;
            return null;
        }
    }

    /** 字段Java类型 */
    public enum JavaType {

        STRING("String", "String"),
        INTEGER("Integer", "Integer"),
        LONG("Long", "Long"),
        DOUBLE("Double", "Double"),
        BOOLEAN("Boolean", "Boolean"),
        BIG_DECIMAL("BigDecimal", "BigDecimal"),
        DATE("Date", "Date");

        private final String code;
        private final String info;

        JavaType(String code, String info) {
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

    /** 字段查询类型 */
    public enum QueryType {

        EQ("eq", "="),
        NE("ne", "!="),
        GT("gt", ">"),
        GE("ge", ">="),
        LT("lt", "<"),
        LE("le", "<="),
        LIKE("like", "like"),
        NOT_LIKE("notLike", "not like"),
        LIKE_LEFT("likeLeft", "like left"),
        LIKE_RIGHT("likeRight", "like right"),
        BETWEEN("between", "between"),
        NOT_BETWEEN("notBetween", "not between"),
        IS_NULL("isNull", "is null"),
        IS_NOT_NULL("isNotNull", "is not null");

        private final String code;
        private final String info;

        QueryType(String code, String info) {
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

    /** 字段显示类型 */
    public enum DisplayType {

        INPUT("Input", "文本框"),
        INPUT_PASSWORD("InputPassword", "密码框"),
        INPUT_NUMBER("InputNumber", "数字输入框"),
        INPUT_TEXTAREA("InputTextArea", "文本域"),
        SELECT("Select", "下拉框"),
        TREE_SELECT("TreeSelect", "树型下拉框"),
        CHECKBOX_GROUP("CheckboxGroup", "单选框"),
        RADIO_BUTTON_GROUP("RadioButtonGroup", "按钮风格单选框"),
        RADIO_GROUP("RadioGroup", "复选框"),
        DATE_PICKER("DatePicker", "日期控件"),
        TIME_PICKER("TimePicker", "时间控件"),
        IMAGE_UPLOAD("imageUpload", "图片上传"),
        FILE_UPLOAD("Upload", "文件上传"),
        TINYMCE("tinymce", "富文本控件"),
        MARKDOWN("markdown", "MarkDown控件");

        private final String code;
        private final String info;

        DisplayType(String code, String info) {
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

    /** 源策略模式 */
    public enum SourceMode {

        ISOLATE("isolate", "策略源"),
        MASTER("master", "主数据源");

        private final String code;
        private final String info;

        SourceMode(String code, String info) {
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