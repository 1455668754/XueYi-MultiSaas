package com.xueyi.common.core.constant.gen;

import com.xueyi.common.core.utils.core.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 代码生成通用常量
 *
 * @author xueyi
 */
public class GenConstants {

    /** 字典名称 */
    public static final String DICT_NAME = "Dic{}Options";

    /** 校验内容 */
    @Getter
    @AllArgsConstructor
    public enum GenCheck {

        NORMAL_DISABLE("0", "（0正常 1停用）"),
        SHOW_HIDE("1", "（0显示 1隐藏）"),
        YES_NO("2", "（Y是 N否）");

        private final String code;
        private final String info;

    }

    /** 生成字段 */
    @Getter
    @AllArgsConstructor
    public enum GenField {

        ID("id", "主键字段", "id"),
        NAME("name", "名称字段", "name"),
        STATUS("status", "状态字段", "status"),
        SORT("sort", "序号字段", "sort"),
        TYPE("type", "类型字段", "type"),
        SEX("sex", "性别字段", "sex"),
        PARENT_ID("parentId", "树父编码字段", "parent_id"),
        ANCESTORS("ancestors", "祖籍列表字段", "ancestors"),
        LOGO("logo", "logo字段", "logo"),
        IMAGE("image", "图片字段", "image"),
        FILE("file", "文件字段", "file"),
        COMMENT("comment", "注释字段", "comment"),
        REMARK("remark", "备注字段", "remark");

        private final String code;
        private final String info;
        private final String key;

        public static GenField getByCode(String code) {
            return EnumUtil.getByCodeElseNull(GenField.class, code);
        }

    }

    /** 其它生成选项字段 */
    @Getter
    @AllArgsConstructor
    public enum OptionField {
        PARENT_MODULE_ID("parentModuleId", "归属模块Id字段", null),
        PARENT_MENU_ID("parentMenuId", "归属菜单Id字段", null),
        PARENT_MENU_ANCESTORS("parentMenuAncestors", "归属菜单祖籍字段", null),

        TREE_ID("treeCode", "树编码字段", "id"),
        PARENT_ID("parentId", "树父编码字段", "parent_id"),
        TREE_NAME("treeName", "树名称字段", "name"),
        ANCESTORS("ancestors", "祖籍列表字段", "ancestors"),
        LEVEL("level", "层级字段", "level"),

//        FOREIGN_ID("foreignId", "外键关联的主表字段字段", null),
//        SUB_TABLE_ID("subTableId", "关联子表的表名字段", null),
//        SUB_FOREIGN_ID("subForeignId", "关联子表的外键名字段", null),

        ID("id", "主键字段", "id"),
        NAME("name", "名称字段", "name"),
        STATUS("status", "状态字段", "status"),
        SORT("sort", "序号字段", "sort"),
        IS_TENANT("isTenant", "多租户", null),
        SOURCE_MODE("sourceMode", "源策略模式", null),

        HAS_API_ES("hasApiES", "存在接口：修改状态", null),

        API_LIST("apiList", "接口：查询列表", null),
        API_IMPORT("apiImport", "接口：导入", null),
        API_EXPORT("apiExport", "接口：导出", null),
        API_GET_INFO("apiGetInfo", "接口：查询详情", null),
        API_ADD("apiAdd", "接口：新增", null),
        API_EDIT("apiEdit", "接口：修改", null),
        API_ES("apiEditStatus", "接口：修改状态", null),
        API_BATCH_REMOVE("apiBatchRemove", "接口：批量删除", null);

        private final String code;
        private final String info;
        private final String key;

        public static OptionField getByCode(String code) {
            return EnumUtil.getByCodeElseNull(OptionField.class, code);
        }

    }

    /** 表模板类型 */
    @Getter
    @AllArgsConstructor
    public enum TemplateType {

        BASE("base", "单表"),
        TREE("tree", "树表"),
        MERGE("merge", "关联表");

        private final String code;
        private final String info;

        public static TemplateType getByCode(String code) {
            return EnumUtil.getByCode(TemplateType.class, code);
        }

    }

    /** 字段Java类型 */
    @Getter
    @AllArgsConstructor
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

    }

    /** 字段查询类型 */
    @Getter
    @AllArgsConstructor
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

    }

    /** 字段显示类型 */
    @Getter
    @AllArgsConstructor
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
        IMAGE_UPLOAD("ImageUpload", "图片上传"),
        FILE_UPLOAD("Upload", "文件上传"),
        TINYMCE("tinymce", "富文本控件"),
        MARKDOWN("markdown", "MarkDown控件");

        private final String code;
        private final String info;

    }

    /** 源策略模式 */
    @Getter
    @AllArgsConstructor
    public enum SourceMode {

        ISOLATE("Isolate", "策略源"),
        MASTER("Master", "主数据源");

        private final String code;
        private final String info;

    }

    /** 源策略模式 */
    @Getter
    @AllArgsConstructor
    public enum GenType {

        ISOLATE("Isolate", "策略源"),
        MASTER("Master", "主数据源");

        private final String code;
        private final String info;

    }

}