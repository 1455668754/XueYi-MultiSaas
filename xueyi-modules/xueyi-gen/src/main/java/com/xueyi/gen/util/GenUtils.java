package com.xueyi.gen.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.xueyi.common.core.constant.basic.DictConstants;
import com.xueyi.common.core.constant.gen.GenConstants;
import com.xueyi.common.core.constant.system.AuthorityConstants;
import com.xueyi.common.core.utils.core.*;
import com.xueyi.gen.config.GenConfig;
import com.xueyi.gen.domain.dto.GenTableColumnDto;
import com.xueyi.gen.domain.dto.GenTableDto;
import org.apache.commons.lang3.RegExUtils;

import java.util.List;

/**
 * 代码生成器 工具类
 *
 * @author xueyi
 */
public class GenUtils {

    /**
     * 初始化表信息
     */
    public static void initTable(GenTableDto genTable) {
        genTable.setClassName(StrUtil.convertToCamelCase(genTable.getName()));
        getRemoveItem(genTable);
        genTable.setTplCategory(GenConstants.TemplateType.BASE.getCode());
        genTable.setBusinessName(getBusinessName(genTable.getName()));
        genTable.setFunctionName(replaceText(genTable.getComment()));
        genTable.setFunctionAuthor(GenConfig.getAuthor());
        if (StrUtil.isNotBlank(GenConfig.getUiPath()))
            genTable.setUiPath(GenConfig.getUiPath());
    }

    /**
     * 初始化其它生成选项
     */
    public static void initTableOptions(List<GenTableColumnDto> columnList, GenTableDto table) {
        JSONObject optionJson = new JSONObject();
        // 1.设置默认模块
        optionJson.put(GenConstants.OptionField.PARENT_MODULE_ID.getCode(), AuthorityConstants.MODULE_DEFAULT_NODE.toString());
        // 2.设置默认菜单
        optionJson.put(GenConstants.OptionField.PARENT_MENU_ID.getCode(), AuthorityConstants.MENU_TOP_NODE.toString());
        // 3.检测是否为多租户模式
        String[] javaFields = columnList.stream().map(GenTableColumnDto::getJavaField).toArray(String[]::new);
        optionJson.put(GenConstants.OptionField.IS_TENANT.getCode(), ArrayUtil.containsAny(GenConfig.getEntity().getBack().getTenant(), javaFields)
                ? DictConstants.DicYesNo.YES.getCode()
                : DictConstants.DicYesNo.NO.getCode());
        // 4.设置默认源策略模式
        optionJson.put(GenConstants.OptionField.SOURCE_MODE.getCode(),
                StrUtil.equals(optionJson.getString(GenConstants.OptionField.IS_TENANT.getCode()), DictConstants.DicYesNo.YES.getCode())
                        ? GenConstants.SourceMode.ISOLATE.getCode()
                        : GenConstants.SourceMode.MASTER.getCode());
        // 5.初始化配置
        optionJson.put(GenConstants.OptionField.HAS_API_ES.getCode(), DictConstants.DicYesNo.NO.getCode());
        optionJson.put(GenConstants.OptionField.API_LIST.getCode(), DictConstants.DicYesNo.YES.getCode());
        optionJson.put(GenConstants.OptionField.API_GET_INFO.getCode(), DictConstants.DicYesNo.YES.getCode());
        optionJson.put(GenConstants.OptionField.API_ADD.getCode(), DictConstants.DicYesNo.YES.getCode());
        optionJson.put(GenConstants.OptionField.API_EDIT.getCode(), DictConstants.DicYesNo.YES.getCode());
        optionJson.put(GenConstants.OptionField.API_BATCH_REMOVE.getCode(), DictConstants.DicYesNo.YES.getCode());
        optionJson.put(GenConstants.OptionField.API_ES.getCode(), DictConstants.DicYesNo.NO.getCode());
        optionJson.put(GenConstants.OptionField.API_IMPORT.getCode(), DictConstants.DicYesNo.NO.getCode());
        optionJson.put(GenConstants.OptionField.API_EXPORT.getCode(), DictConstants.DicYesNo.NO.getCode());
        optionJson.put(GenConstants.OptionField.API_CACHE.getCode(), DictConstants.DicYesNo.NO.getCode());
        columnList.forEach(column -> {
            GenConstants.OptionField optionField = GenConstants.OptionField.getByCode(column.getJavaField());
            if (ObjectUtil.isNotNull(optionField)) {
                switch (optionField) {
                    case ID -> {
                        if (column.getIsPk())
                            optionJson.put(GenConstants.OptionField.TREE_ID.getCode(), column.getIdStr());
                    }
                    case NAME -> optionJson.put(GenConstants.OptionField.TREE_NAME.getCode(), column.getIdStr());
                    case STATUS -> {
                        optionJson.put(GenConstants.OptionField.HAS_API_ES.getCode(), DictConstants.DicYesNo.YES.getCode());
                        optionJson.put(GenConstants.OptionField.API_ES.getCode(), DictConstants.DicYesNo.YES.getCode());
                    }
                    case SORT -> optionJson.put(GenConstants.OptionField.SORT.getCode(), column.getIdStr());
                    case PARENT_ID -> {
                        optionJson.put(GenConstants.OptionField.PARENT_ID.getCode(), column.getIdStr());
                        table.setTplCategory(GenConstants.TemplateType.TREE.getCode());
                    }
                    case ANCESTORS -> optionJson.put(GenConstants.OptionField.ANCESTORS.getCode(), column.getIdStr());
                    case LEVEL -> optionJson.put(GenConstants.OptionField.LEVEL.getCode(), column.getIdStr());
                }
            }
        });
        table.setOptions(optionJson.toString());
    }

    /**
     * 初始化列属性字段
     */
    public static void initColumnField(GenTableColumnDto column, GenTableDto table) {
        String dataType = column.getType();
        column.setTableId(table.getId());
        column.setCreateBy(table.getCreateBy());
        // 设置java字段名
        column.setJavaField(StrUtil.toCamelCase(column.getName()));
        String javaField = column.getJavaField();
        // 设置默认类型
        column.setJavaType(GenConstants.JavaType.STRING.getCode());
        // 设置默认显示类型
        column.setHtmlType(GenConstants.DisplayType.INPUT.getCode());
        // 设置默认查询类型（长整型防精度丢失，到前端会自动转成字符串，故使用文本框）
        column.setQueryType(GenConstants.QueryType.EQ.getCode());
        if (ArrayUtil.contains(GenConfig.getDataBase().getTypeStr(), dataType) && !StrUtil.equals(column.getComment(), column.readNameNoSuffix())) {
            column.setHtmlType(GenConstants.DisplayType.SELECT.getCode());
            if (StrUtil.contains(column.getComment(), GenConstants.GenCheck.NORMAL_DISABLE.getInfo())) {
                column.setHtmlType(GenConstants.DisplayType.RADIO_BUTTON_GROUP.getCode());
                column.setDictType(DictConstants.DictType.SYS_NORMAL_DISABLE.getCode());
            } else if (StrUtil.contains(column.getComment(), GenConstants.GenCheck.SHOW_HIDE.getInfo())) {
                column.setHtmlType(GenConstants.DisplayType.RADIO_BUTTON_GROUP.getCode());
                column.setDictType(DictConstants.DictType.SYS_SHOW_HIDE.getCode());
            } else if (StrUtil.contains(column.getComment(), GenConstants.GenCheck.YES_NO.getInfo())) {
                column.setHtmlType(GenConstants.DisplayType.RADIO_BUTTON_GROUP.getCode());
                column.setDictType(DictConstants.DictType.SYS_YES_NO.getCode());
            }
        } else if (ArrayUtil.contains(GenConfig.getDataBase().getTypeText(), dataType)) {
            column.setHtmlType(GenConstants.DisplayType.INPUT_TEXTAREA.getCode());
        } else if (ArrayUtil.contains(GenConfig.getDataBase().getTypeDate(), dataType)) {
            column.setJavaType(GenConstants.JavaType.DATE.getCode());
            column.setHtmlType(GenConstants.DisplayType.DATE_PICKER.getCode());
        } else if (ArrayUtil.contains(GenConfig.getDataBase().getTypeFloat(), dataType)) {
            column.setHtmlType(GenConstants.DisplayType.INPUT_NUMBER.getCode());
            column.setJavaType(GenConstants.JavaType.BIG_DECIMAL.getCode());
        } else if (ArrayUtil.contains(GenConfig.getDataBase().getTypeNumber(), dataType)) {
            column.setHtmlType(GenConstants.DisplayType.INPUT_NUMBER.getCode());
            column.setJavaType(GenConstants.JavaType.INTEGER.getCode());
        } else if (ArrayUtil.contains(GenConfig.getDataBase().getTypeLong(), dataType)) {
            column.setJavaType(GenConstants.JavaType.LONG.getCode());
        }

        // 插入字段（默认除必须移除的参数外所有字段都需要插入）
        boolean mustCheck = ArrayUtil.contains(GenConfig.getEntity().getMustHide(), javaField) || column.getIsPk();
        column.setIsInsert(!(ArrayUtil.contains(GenConfig.getOperate().getNotInsert(), javaField) || mustCheck));
        // 查看字段
        column.setIsView(!(ArrayUtil.contains(GenConfig.getOperate().getNotView(), javaField) || mustCheck));
        // 编辑字段
        column.setIsEdit(!(ArrayUtil.contains(GenConfig.getOperate().getNotEdit(), javaField) || mustCheck));
        // 列表字段
        column.setIsList(!(ArrayUtil.contains(GenConfig.getOperate().getNotList(), javaField) || mustCheck));
        // 查询字段
        column.setIsQuery(!(ArrayUtil.contains(GenConfig.getOperate().getNotQuery(), javaField) || mustCheck));
        // 导入字段
        column.setIsImport(!(ArrayUtil.contains(GenConfig.getOperate().getNotImport(), javaField) || mustCheck));
        // 导出字段
        column.setIsExport(!(ArrayUtil.contains(GenConfig.getOperate().getNotExport(), javaField) || mustCheck));
        // 隐藏字段
        column.setIsHide(ArrayUtil.contains(GenConfig.getEntity().getMustHide(), javaField));
        // 掩蔽字段（默认不掩蔽）
        column.setIsCover(Boolean.FALSE);

        // 特殊指定
        GenConstants.GenField field = GenConstants.GenField.getByCode(javaField);
        if (ObjectUtil.isNotNull(field)) {
            switch (field) {
                case NAME -> column.setQueryType(GenConstants.QueryType.LIKE.getCode());
                case SEX -> column.setDictType(DictConstants.DictType.SYS_USER_SEX.getCode());
                case LOGO, IMAGE -> column.setHtmlType(GenConstants.DisplayType.IMAGE_UPLOAD.getCode());
                case FILE -> column.setHtmlType(GenConstants.DisplayType.FILE_UPLOAD.getCode());
                case COMMENT -> column.setHtmlType(GenConstants.DisplayType.TINYMCE.getCode());
                case REMARK -> column.setHtmlType(GenConstants.DisplayType.INPUT_TEXTAREA.getCode());
            }
        }
        // 最终校验
        basicInitColumn(column);
    }

    /**
     * 最终校验列属性字段
     */
    public static void updateCheckColumn(GenTableDto table) {
        JSONObject objectJson = JSON.parseObject(table.getOptions());
        table.getSubList().forEach(column -> {
            if (StrUtil.equalsAny(table.getTplCategory(), GenConstants.TemplateType.TREE.getCode())) {
                if (ObjectUtil.equals(column.getId(), objectJson.getLong(GenConstants.OptionField.PARENT_ID.getCode()))) {
                    column.setJavaField(GenConstants.OptionField.PARENT_ID.getCode());
                } else if (ObjectUtil.equals(column.getId(), objectJson.getLong(GenConstants.OptionField.ANCESTORS.getCode()))) {
                    column.setJavaField(GenConstants.OptionField.ANCESTORS.getCode());
                }
            }
            // 最终校验
            basicInitColumn(column);
        });
    }

    /**
     * 最终校验列属性字段
     */
    public static void basicInitColumn(GenTableColumnDto column) {
        // 校验是否需要隐藏
        boolean isMustHide = ArrayUtil.contains(GenConfig.getEntity().getMustHide(), column.getName());
        if (column.getIsHide() || isMustHide) {
            if (isMustHide) {
                column.setIsHide(Boolean.TRUE);
            }
            column.setIsView(Boolean.FALSE);
            column.setIsInsert(Boolean.FALSE);
            column.setIsEdit(Boolean.FALSE);
            column.setIsImport(Boolean.FALSE);
            column.setIsExport(Boolean.FALSE);
            column.setIsUnique(Boolean.FALSE);
            column.setIsRequired(Boolean.FALSE);
            column.setIsList(Boolean.FALSE);
            column.setIsQuery(Boolean.FALSE);
        }
    }

    /**
     * 获取符合的替换前缀组 | 获取包名 | 获取模块名
     *
     * @param genTable 业务表对象
     */
    public static void getRemoveItem(GenTableDto genTable) {
        if (GenConfig.getAutoRemovePre() && CollUtil.isNotEmpty(GenConfig.getRemoveLists())) {
            for (GenConfig.RemoveItem removeItem : GenConfig.getRemoveLists()) {
                if (StrUtil.equals(StrUtil.sub(genTable.getName(), NumberUtil.Zero, removeItem.getPrefix().length()), removeItem.getPrefix())) {
                    genTable.setPrefix(StrUtil.convertToCamelCase(removeItem.getPrefix()));
                    genTable.setPackageName(removeItem.getPackageName());
                    genTable.setModuleName(getModuleName(removeItem.getPackageName()));
                    genTable.setAuthorityName(genTable.getModuleName());
                    return;
                }
            }
        }
    }

    /**
     * 获取模块名
     *
     * @param packageName 包名
     * @return 模块名
     */
    public static String getModuleName(String packageName) {
        int lastIndex = packageName.lastIndexOf(StrUtil.DOT);
        int nameLength = packageName.length();
        return StrUtil.sub(packageName, lastIndex + NumberUtil.One, nameLength);
    }

    /**
     * 获取业务名
     *
     * @param tableName 表名
     * @return 业务名
     */
    public static String getBusinessName(String tableName) {
        int lastIndex = tableName.lastIndexOf(StrUtil.UNDERLINE);
        int nameLength = tableName.length();
        return StrUtil.sub(tableName, lastIndex + NumberUtil.One, nameLength);
    }

    /**
     * 关键字替换
     *
     * @param text 需要被替换的名字
     * @return 替换后的名字
     */
    public static String replaceText(String text) {
        return RegExUtils.replaceAll(text, "(?:信息表|表|雪忆)", StrUtil.EMPTY);
    }
}