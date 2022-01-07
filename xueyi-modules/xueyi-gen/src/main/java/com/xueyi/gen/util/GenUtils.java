package com.xueyi.gen.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.AuthorityConstants;
import com.xueyi.common.core.constant.GenConstants;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.gen.config.GenConfig;
import com.xueyi.gen.domain.dto.GenTableColumnDto;
import com.xueyi.gen.domain.dto.GenTableDto;
import org.apache.commons.lang3.RegExUtils;

import java.util.Arrays;
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
        genTable.setClassName(StringUtils.convertToCamelCase(genTable.getName()));
        getRemoveItem(genTable);
        genTable.setBusinessName(getBusinessName(genTable.getName()));
        genTable.setFunctionName(replaceText(genTable.getComment()));
        genTable.setFunctionAuthor(GenConfig.getAuthor());
    }

    /**
     * 初始化其它生成选项
     */
    public static void initTableOptions(List<GenTableColumnDto> columnList, GenTableDto table) {
        StringBuilder options = new StringBuilder();
        String prefix = ", \"", infill = "\": \"", suffix = "\"" ;
        options.append("{ \"").append(GenConstants.OptionField.PARENT_MODULE_ID.getCode()).append(infill).append(AuthorityConstants.MODULE_DEFAULT_NODE).append(suffix)
                .append(prefix).append(GenConstants.OptionField.PARENT_MENU_ID.getCode()).append(infill).append(AuthorityConstants.MENU_TOP_NODE).append(suffix);
        String isCoverId = GenConstants.Status.FALSE.getCode();
        String isCoverName = GenConstants.Status.FALSE.getCode();
        String isCoverStatus = GenConstants.Status.FALSE.getCode();
        String isCoverSort = GenConstants.Status.FALSE.getCode();
        String isTenant = GenConstants.Status.FALSE.getCode();
        for (GenTableColumnDto column : columnList) {
            if (column.isPk()
                    && (!StrUtil.equals(column.getJavaType(), GenConstants.JavaType.LONG.getCode()) || !StrUtil.equals(column.getJavaField(), GenConstants.CoverField.ID.getCode()))) {
                options.append(prefix).append(GenConstants.OptionField.ID.getCode()).append(infill).append(column.getId()).append(suffix);
                isCoverId = GenConstants.Status.TRUE.getCode();
            } else if (StrUtil.equals(column.getJavaField(), GenConstants.CoverField.NAME.getCode())
                    && !StrUtil.equals(column.getJavaType(), GenConstants.JavaType.STRING.getCode())) {
                options.append(prefix).append(GenConstants.OptionField.NAME.getCode()).append(infill).append(column.getId()).append(suffix);
                isCoverName = GenConstants.Status.TRUE.getCode();
            } else if (StrUtil.equals(column.getJavaField(), GenConstants.CoverField.STATUS.getCode())
                    && (!StrUtil.equals(column.getJavaType(), GenConstants.JavaType.STRING.getCode()) || !column.getComment().contains(GenConstants.CoverField.STATUS.getKey()))) {
                options.append(prefix).append(GenConstants.OptionField.STATUS.getCode()).append(infill).append(column.getId()).append(suffix);
                isCoverStatus = GenConstants.Status.TRUE.getCode();
            } else if (StrUtil.equals(column.getJavaField(), GenConstants.CoverField.SORT.getCode())
                    && !StrUtil.equals(column.getJavaType(), GenConstants.JavaType.INTEGER.getCode())) {
                options.append(prefix).append(GenConstants.OptionField.STATUS.getCode()).append(infill).append(column.getId()).append(suffix);
                isCoverSort = GenConstants.Status.TRUE.getCode();
            }
            if (StrUtil.equalsAny(column.getJavaField(), GenConstants.TENANT_ENTITY)) {
                isTenant = GenConstants.Status.TRUE.getCode();
            }
        }
        options.append(prefix).append(GenConstants.OptionField.COVER_ID.getCode()).append(infill).append(isCoverId).append(suffix)
                .append(prefix).append(GenConstants.OptionField.COVER_NAME.getCode()).append(infill).append(isCoverName).append(suffix)
                .append(prefix).append(GenConstants.OptionField.COVER_STATUS.getCode()).append(infill).append(isCoverStatus).append(suffix)
                .append(prefix).append(GenConstants.OptionField.COVER_SORT.getCode()).append(infill).append(isCoverSort).append(suffix)
                .append(prefix).append(GenConstants.OptionField.IS_TENANT.getCode()).append(infill).append(isTenant).append(suffix)
                .append(prefix).append(
                        StrUtil.equals(isTenant, GenConstants.Status.TRUE.getCode())
                                ? GenConstants.SourceMode.ISOLATE.getCode()
                                : GenConstants.SourceMode.MASTER.getCode()).append(infill).append(isTenant).append(suffix).append(" }");
        table.setOptions(options.toString());
    }

    /**
     * 初始化列属性字段
     */
    public static void initColumnField(GenTableColumnDto column, GenTableDto table) {
        String dataType = column.getType();
        column.setTableId(table.getId());
        column.setCreateBy(table.getCreateBy());
        // 设置java字段名
        column.setJavaField(StringUtils.toCamelCase(column.getName()));
        String javaField = column.getJavaField();
        // 设置默认类型
        column.setJavaType(GenConstants.JavaType.STRING.getCode());

        // 设置默认查询类型（长整型防精度丢失，到前端会自动转成字符串，故使用文本框）
        column.setQueryType(GenConstants.QueryType.EQ.getCode());
        if (arraysContains(GenConstants.COLUMN_TYPE_STR, dataType) || arraysContains(GenConstants.COLUMN_TYPE_TEXT, dataType)) {
            column.setHtmlType(arraysContains(GenConstants.COLUMN_TYPE_TEXT, dataType) ? GenConstants.DisplayType.INPUT_TEXTAREA.getCode() : GenConstants.DisplayType.INPUT.getCode());
        } else if (arraysContains(GenConstants.COLUMN_TYPE_DATE, dataType)) {
            column.setJavaType(GenConstants.JavaType.DATE.getCode());
            column.setHtmlType(GenConstants.DisplayType.DATE_PICKER.getCode());
        } else if (arraysContains(GenConstants.COLUMN_TYPE_FLOAT, dataType)) {
            column.setHtmlType(GenConstants.DisplayType.INPUT_NUMBER.getCode());
            column.setJavaType(GenConstants.JavaType.BIG_DECIMAL.getCode());
        } else if (arraysContains(GenConstants.COLUMN_TYPE_NUMBER, dataType)) {
            column.setHtmlType(GenConstants.DisplayType.INPUT_NUMBER.getCode());
            column.setJavaType(GenConstants.JavaType.INTEGER.getCode());
        } else if (arraysContains(GenConstants.COLUMN_TYPE_LONG, dataType)) {
            column.setHtmlType(GenConstants.DisplayType.INPUT.getCode());
            column.setJavaType(GenConstants.JavaType.LONG.getCode());
        }

        // 插入字段（默认除必须移除的参数外所有字段都需要插入）
        column.setInsert(!(arraysContains(GenConstants.COLUMN_NAME_NOT_INSERT, javaField) || arraysContains(GenConstants.COLUMN_MUST_HIDE, javaField) || column.isPk()));
        // 查看字段
        column.setView(!(arraysContains(GenConstants.COLUMN_NAME_NOT_VIEW, javaField) || arraysContains(GenConstants.COLUMN_MUST_HIDE, javaField) || column.isPk()));
        // 编辑字段
        column.setEdit(!(arraysContains(GenConstants.COLUMN_NAME_NOT_EDIT, javaField) || arraysContains(GenConstants.COLUMN_MUST_HIDE, javaField) || column.isPk()));
        // 列表字段
        column.setList(!(arraysContains(GenConstants.COLUMN_NAME_NOT_LIST, javaField) || arraysContains(GenConstants.COLUMN_MUST_HIDE, javaField) || column.isPk()));
        // 查询字段
        column.setQuery(!(arraysContains(GenConstants.COLUMN_NAME_NOT_QUERY, javaField) || arraysContains(GenConstants.COLUMN_MUST_HIDE, javaField) || column.isPk()));

        // 查询字段类型
        if (StringUtils.endsWithIgnoreCase(javaField, "name")) {
            column.setQueryType(GenConstants.QueryType.LIKE.getCode());
        }
        // 状态字段设置单选框
        if (StringUtils.endsWithIgnoreCase(javaField, "status")) {
            column.setHtmlType(GenConstants.DisplayType.RADIO_BUTTON_GROUP.getCode());
        }
        // 类型&性别字段设置下拉框
        else if (StringUtils.endsWithIgnoreCase(javaField, "type")
                || StringUtils.endsWithIgnoreCase(javaField, "sex")) {
            column.setHtmlType(GenConstants.DisplayType.SELECT.getCode());
        }
        // 图片字段设置图片上传控件
        else if (StringUtils.endsWithIgnoreCase(javaField, "image")) {
            column.setHtmlType(GenConstants.DisplayType.IMAGE_UPLOAD.getCode());
        }
        // 文件字段设置文件上传控件
        else if (StringUtils.endsWithIgnoreCase(javaField, "file")) {
            column.setHtmlType(GenConstants.DisplayType.FILE_UPLOAD.getCode());
        }
        // 内容字段设置富文本控件
        else if (StringUtils.endsWithIgnoreCase(javaField, "content")) {
            column.setHtmlType(GenConstants.DisplayType.TINYMCE.getCode());
        }
    }

    /**
     * 校验数组是否包含指定值
     *
     * @param arr         数组
     * @param targetValue 值
     * @return 是否包含
     */
    public static boolean arraysContains(String[] arr, String targetValue) {
        return Arrays.asList(arr).contains(targetValue);
    }

    /**
     * 获取符合的替换前缀组 | 获取包名 | 获取模块名
     *
     * @param genTable 业务表对象
     */
    public static void getRemoveItem(GenTableDto genTable) {
        boolean autoRemovePre = GenConfig.isAutoRemovePre();
        if (autoRemovePre && CollUtil.isNotEmpty(GenConfig.getRemoveLists()))
            for (GenConfig.RemoveItem removeItem : GenConfig.getRemoveLists())
                if (StrUtil.equals(StrUtil.sub(genTable.getName(), 0, removeItem.getPrefix().length()), removeItem.getPrefix())) {
                    genTable.setPrefix(StringUtils.convertToCamelCase(removeItem.getPrefix()));
                    genTable.setPackageName(removeItem.getPackageName());
                    genTable.setFrontPackageName(removeItem.getFrontPackageName());
                    genTable.setModuleName(getModuleName(removeItem.getPackageName()));
                    genTable.setAuthorityName(genTable.getModuleName());
                    return;
                }
    }

    /**
     * 获取模块名
     *
     * @param packageName 包名
     * @return 模块名
     */
    public static String getModuleName(String packageName) {
        int lastIndex = packageName.lastIndexOf(".");
        int nameLength = packageName.length();
        return StringUtils.substring(packageName, lastIndex + 1, nameLength);
    }

    /**
     * 获取业务名
     *
     * @param tableName 表名
     * @return 业务名
     */
    public static String getBusinessName(String tableName) {
        int lastIndex = tableName.lastIndexOf("_");
        int nameLength = tableName.length();
        return StringUtils.substring(tableName, lastIndex + 1, nameLength);
    }

    /**
     * 关键字替换
     *
     * @param text 需要被替换的名字
     * @return 替换后的名字
     */
    public static String replaceText(String text) {
        return RegExUtils.replaceAll(text, "(?:信息表|表|雪忆)", "");
    }
}