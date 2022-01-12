package com.xueyi.gen.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.xueyi.common.core.constant.AuthorityConstants;
import com.xueyi.common.core.constant.GenConstants;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.gen.domain.dto.GenTableColumnDto;
import com.xueyi.gen.domain.dto.GenTableDto;
import org.apache.velocity.VelocityContext;

import java.util.*;

/**
 * 模板工具类
 *
 * @author xueyi
 */
public class VelocityUtils {

    /** 项目空间路径 */
    private static final String PROJECT_PATH = "main/java";

    /** mybatis空间路径 */
    private static final String MYBATIS_PATH = "main/resources/mapper";

    /** 隐藏字段数组 */
    private static final String HIDE = "hide";

    /** 覆写字段数组 */
    private static final String COVER = "cover";

    /** 前端路径区分标识 */
    private static final String FRONT_DEPTH_IDENTIFICATION = ".";

    /**
     * 设置模板变量信息
     *
     * @return 模板列表
     */
    public static VelocityContext prepareContext(GenTableDto genTable) {
        String moduleName = genTable.getModuleName();
        String businessName = genTable.getBusinessName();
        String packageName = genTable.getPackageName();
        String tplCategory = genTable.getTplCategory();
        String functionName = genTable.getFunctionName();
        JSONObject optionsObj = JSONObject.parseObject(genTable.getOptions());
        Map<String, Set<String>> domainMap = getCoverMap(genTable);

        VelocityContext velocityContext = new VelocityContext();
        // 模板类型
        velocityContext.put("tplCategory", genTable.getTplCategory());
        // 数据库表名
        velocityContext.put("tableName", genTable.getName());
        // 生成功能名
        velocityContext.put("functionName", StringUtils.isNotEmpty(functionName) ? functionName : "【请填写功能名称】");
        // 实体类名称(首字母大写)
        velocityContext.put("ClassName", genTable.getClassName());
        // 实体类名称(首字母小写)
        velocityContext.put("className", StringUtils.uncapitalize(genTable.getClassName()));
        // 实体类名称(首字母大写 | 无前缀)
        velocityContext.put("ClassNameNoPrefix", genTable.getClassName().replaceFirst(genTable.getPrefix(), ""));
        // 实体类名称(首字母小写 | 无前缀)
        velocityContext.put("classNameNoPrefix", StringUtils.uncapitalize(genTable.getClassName().replaceFirst(genTable.getPrefix(), "")));
        // 生成模块名
        velocityContext.put("moduleName", genTable.getModuleName());
        // 生成模块名
        velocityContext.put("authorityName", genTable.getAuthorityName());
        // 生成业务名(首字母大写)
        velocityContext.put("BusinessName", StringUtils.capitalize(genTable.getBusinessName()));
        // 生成业务名(首字母小写)
        velocityContext.put("businessName", genTable.getBusinessName());
        // 生成业务名(字母全大写)
        velocityContext.put("BUSINESSName", StringUtils.upperCase(genTable.getBusinessName()));
        // 生成包路径前缀
        velocityContext.put("basePackage", getPackagePrefix(packageName));
        // 生成包路径
        velocityContext.put("packageName", packageName);
        // 作者
        velocityContext.put("author", genTable.getFunctionAuthor());
//        // 当前日期
//        velocityContext.put("datetime", DateUtils.getDate());
        // 主键字段
        velocityContext.put("pkColumn", genTable.getPkColumn());
        // 导入包集合
        velocityContext.put("importList", getImportList(genTable, domainMap.get(HIDE)));
        // 字段集合
        velocityContext.put("columns", genTable.getSubList());
        // 数据表排除字段
        velocityContext.put("excludeProperty", getExcludePropertyList(genTable));
        // 业务表信息
        velocityContext.put("table", genTable);
        // 字典组
        velocityContext.put("dictMap", getDictMap(genTable));
        // Po覆盖字段集合 (生成po的控制参数)
        velocityContext.put("coverField", domainMap.get(COVER));
        // Po隐藏字段集合 (生成po的控制参数)
        velocityContext.put("hideField", domainMap.get(HIDE));
        // 前端隐藏字段集合 (生成po的控制参数)
        velocityContext.put("frontHideField", getFrontHideField(genTable.getTplCategory()));
        // 必定隐藏字段集合 (全局隐藏的控制参数)
        velocityContext.put("mustHideField", Arrays.asList(GenConstants.COLUMN_MUST_HIDE));
        // 是否为多租户（true | false）
        velocityContext.put("isTenant", isTenant(genTable));
        // 源策略模式
        velocityContext.put("sourceMode", getSourceMode(genTable));
        // 源策略是否为主库
        velocityContext.put("isMasterSource", isMasterSource(genTable));
        // 获取其他重要参数（名称、状态...）
        getOtherMainColum(velocityContext, genTable);
        // sql模板设置
        setMenuVelocityContext(velocityContext, genTable, optionsObj);
        switch (Objects.requireNonNull(GenConstants.TemplateType.getValue(tplCategory))) {
            case TREE:
                setTreeVelocityContext(velocityContext, genTable, optionsObj);
                break;
            case SUB_TREE:
                setTreeVelocityContext(velocityContext, genTable, optionsObj);
            case SUB_BASE:
                setSubVelocityContext(velocityContext, genTable);
        }
        return velocityContext;
    }

    /**
     * 获取附加参数信息
     */
    public static void getOtherMainColum(VelocityContext context, GenTableDto genTable) {
        for (GenTableColumnDto column : genTable.getSubList()) {
            if (StrUtil.equals(column.getJavaField(), GenConstants.OptionField.NAME.getCode()))
                context.put("nameColumn", column); // 名称字段
            else if (StrUtil.equals(column.getJavaField(), GenConstants.OptionField.STATUS.getCode()))
                context.put("statusColumn", column); // 状态字段
            else if (StrUtil.equals(column.getJavaField(), GenConstants.OptionField.SORT.getCode()))
                context.put("sortColumn", column); // 排序字段
        }
    }

    /**
     * 设置sql模板变量信息
     */
    public static void setMenuVelocityContext(VelocityContext context, GenTableDto genTable, JSONObject optionsObj) {
        context.put("parentModuleId", getParentModuleId(optionsObj));
        context.put("parentMenuId", getParentMenuId(optionsObj));
        context.put("parentMenuPath", optionsObj.getString(GenConstants.OptionField.PARENT_MENU_PATH.getCode()));
        context.put("parentMenuAncestors", optionsObj.getString(GenConstants.OptionField.PARENT_MENU_ANCESTORS.getCode()));

        context.put("menuId0", IdUtil.getSnowflake(0, 0).nextId());
        context.put("menuId1", IdUtil.getSnowflake(0, 0).nextId());
        context.put("menuId2", IdUtil.getSnowflake(0, 0).nextId());
        context.put("menuId3", IdUtil.getSnowflake(0, 0).nextId());
        context.put("menuId4", IdUtil.getSnowflake(0, 0).nextId());
        context.put("menuId5", IdUtil.getSnowflake(0, 0).nextId());
        context.put("menuId6", IdUtil.getSnowflake(0, 0).nextId());
    }

    public static void setTreeVelocityContext(VelocityContext context, GenTableDto genTable, JSONObject optionsObj) {

        Map<String, GenTableColumnDto> treeMap = new HashMap<>();
        for (GenTableColumnDto column : genTable.getSubList()) {
            if (StrUtil.equals(optionsObj.getString(GenConstants.OptionField.COVER_TREE_ID.getCode()), GenConstants.Status.TRUE.getCode())
                    && ObjectUtil.equals(column.getId(), optionsObj.getLong(GenConstants.OptionField.TREE_ID.getCode()))) {
                treeMap.put("idColumn", column);
            } else if (StrUtil.equals(optionsObj.getString(GenConstants.OptionField.COVER_TREE_ID.getCode()), GenConstants.Status.FALSE.getCode())
                    && StrUtil.equals(column.getJavaField(), optionsObj.getString(GenConstants.CoverField.ID.getCode()))) {
                treeMap.put("idColumn", column);
            }

            if (StrUtil.equals(optionsObj.getString(GenConstants.OptionField.COVER_PARENT_ID.getCode()), GenConstants.Status.TRUE.getCode())
                    && ObjectUtil.equals(column.getId(), optionsObj.getLong(GenConstants.OptionField.PARENT_ID.getCode()))) {
                treeMap.put("parentIdColumn", column);
            } else if (StrUtil.equals(optionsObj.getString(GenConstants.OptionField.COVER_PARENT_ID.getCode()), GenConstants.Status.FALSE.getCode())
                    && StrUtil.equals(column.getJavaField(), optionsObj.getString(GenConstants.CoverField.PARENT_ID.getCode()))) {
                treeMap.put("parentIdColumn", column);
            }

            if (StrUtil.equals(optionsObj.getString(GenConstants.OptionField.COVER_TREE_NAME_ID.getCode()), GenConstants.Status.TRUE.getCode())
                    && ObjectUtil.equals(column.getId(), optionsObj.getLong(GenConstants.OptionField.PARENT_ID.getCode()))) {
                treeMap.put("nameColumn", column);
            } else if (StrUtil.equals(optionsObj.getString(GenConstants.OptionField.COVER_TREE_NAME_ID.getCode()), GenConstants.Status.FALSE.getCode())
                    && StrUtil.equals(column.getJavaField(), optionsObj.getString(GenConstants.CoverField.NAME.getCode()))) {
                treeMap.put("nameColumn", column);
            }
        }
        context.put("treeMap", treeMap);
    }

    /**
     * 设置主子表模板变量信息
     */
    public static void setSubVelocityContext(VelocityContext context, GenTableDto table) {
        GenTableDto subTable = table.getSubTable();
        String functionName = subTable.getFunctionName();

        // 外键关联的主表字段信息
        context.put("foreignColumn", getForeignMainColumn(table));
        // 子表外键字段信息
        context.put("subForeignColumn", getForeignColumn(table));
        context.put("subTable", subTable);
        context.put("subTableName", subTable.getName());
        // 实体类名称(首字母大写)
        context.put("subClassName", subTable.getClassName());
        // 实体类名称(首字母小写)
        context.put("subclassName", StringUtils.uncapitalize(subTable.getClassName()));
        // 实体类名称(首字母大写 | 无前缀)
        context.put("subClassNameNoPrefix", subTable.getClassName().replaceFirst(subTable.getPrefix(), ""));
        // 实体类名称(首字母小写 | 无前缀)
        context.put("subclassNameNoPrefix", StringUtils.uncapitalize(subTable.getClassName().replaceFirst(subTable.getPrefix(), "")));
        // 生成包路径
        context.put("subPackageName", subTable.getPackageName());
        // 生成功能名
        context.put("subFunctionName", StringUtils.isNotEmpty(functionName) ? functionName : "【请填写功能名称】");
        // 生成业务名(首字母大写)
        context.put("subBusinessName", StringUtils.capitalize(subTable.getBusinessName()));
        // 生成业务名(首字母小写)
        context.put("subbusinessName", subTable.getBusinessName());
        // 生成业务名(字母全大写)
        context.put("subBUSINESSName", StringUtils.upperCase(subTable.getBusinessName()));
    }

    /**
     * 获取模板信息
     *
     * @return 模板列表
     */
    public static List<String> getTemplateList(String tplCategory) {
        List<String> templates = new ArrayList<>();
        if (StrUtil.equals(tplCategory, GenConstants.TemplateType.MERGE.getCode())) {
            templates.add("vm/java/merge/merge.java.vm");
            templates.add("vm/java/merge/mergeMapper.java.vm");
        } else {
            templates.add("vm/java/dto.java.vm");
            templates.add("vm/java/po.java.vm");
            templates.add("vm/java/controller.java.vm");
            templates.add("vm/java/service.java.vm");
            templates.add("vm/java/serviceImpl.java.vm");
            templates.add("vm/java/manager.java.vm");
            templates.add("vm/java/mapper.java.vm");
            templates.add("vm/sql/sql.sql.vm");
            templates.add("vm/ts/api.ts.vm");
            templates.add("vm/ts/data.ts.vm");
            templates.add("vm/ts/auth.ts.vm");
            templates.add("vm/ts/enum.ts.vm");
            templates.add("vm/ts/infoModel.ts.vm");
            switch (Objects.requireNonNull(GenConstants.TemplateType.getValue(tplCategory))) {
                case BASE:
                    templates.add("vm/vue/detail.vue.vm");
                    templates.add("vm/vue/base/index.vue.vm");
                    templates.add("vm/vue/model.vue.vm");
                    break;
                case TREE:
                    templates.add("vm/vue/detail.vue.vm");
                    templates.add("vm/vue/tree/index.vue.vm");
                    templates.add("vm/vue/model.vue.vm");
                    break;
                case SUB_TREE:
                case SUB_BASE:
                    templates.add("vm/vue/detail.vue.vm");
                    templates.add("vm/vue/index.vue.vm");
                    templates.add("vm/vue/model.vue.vm");
            }
        }
        return templates;
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String template, GenTableDto genTable) {
        // 文件名称
        String fileName = "";
        // 包路径
        String packageName = genTable.getPackageName();
        // 模块名
        String moduleName = genTable.getModuleName();
        // 大写类名
        String className = genTable.getClassName();
        // 业务名称
        String businessName = genTable.getBusinessName();

        String javaPath = PROJECT_PATH + "/" + StringUtils.replace(packageName, ".", "/");
        String mybatisPath = MYBATIS_PATH + "/" + moduleName;
        String vuePath = "vue";

        switch (template){
            case "po.java.vm":
                return StringUtils.format("{}/domain/{}.java", javaPath, className);
            case "dto.java.vm":
                return StringUtils.format("{}/domain/{}.java", javaPath, className);

        }
        if (template.contains("dto.java.vm")) {
            fileName = StringUtils.format("{}/domain/{}.java", javaPath, className);
        }
        if (template.contains("sub-dto.java.vm") && StrUtil.equals(GenConstants.TemplateType.SUB_BASE.getCode(), genTable.getTplCategory())) {
            fileName = StringUtils.format("{}/domain/{}.java", javaPath, genTable.getSubTable().getClassName());
        } else if (template.contains("mapper.java.vm")) {
            fileName = StringUtils.format("{}/mapper/{}Mapper.java", javaPath, className);
        } else if (template.contains("service.java.vm")) {
            fileName = StringUtils.format("{}/service/I{}Service.java", javaPath, className);
        } else if (template.contains("serviceImpl.java.vm")) {
            fileName = StringUtils.format("{}/service/impl/{}ServiceImpl.java", javaPath, className);
        } else if (template.contains("controller.java.vm")) {
            fileName = StringUtils.format("{}/controller/{}Controller.java", javaPath, className);
        } else if (template.contains("mapper.xml.vm")) {
            fileName = StringUtils.format("{}/{}Mapper.xml", mybatisPath, className);
        } else if (template.contains("sql.sql.vm")) {
            fileName = businessName + "Menu.sql";
        } else if (template.contains("api.js.vm")) {
            fileName = StringUtils.format("{}/api/{}/{}.js", vuePath, moduleName, businessName);
        } else if (template.contains("index.vue.vm")) {
            fileName = StringUtils.format("{}/views/{}/{}/index.vue", vuePath, moduleName, businessName);
        } else if (template.contains("index-tree.vue.vm")) {
            fileName = StringUtils.format("{}/views/{}/{}/index.vue", vuePath, moduleName, businessName);
        }
        return fileName;
    }

    /**
     * 获取主子表外键关联的主表信息
     *
     * @param genTable 业务表对象
     * @return 外键关联的主表字段对象
     */
    public static GenTableColumnDto getForeignMainColumn(GenTableDto genTable) {
        JSONObject optionsObj = JSONObject.parseObject(genTable.getOptions());
        for (GenTableColumnDto column : genTable.getSubList()) {
            if (ObjectUtil.equals(column.getId(), optionsObj.getLong(GenConstants.OptionField.FOREIGN_ID.getCode()))) {
                return column;
            }
        }
        return null;
    }

    /**
     * 获取主子表外键信息
     *
     * @param genTable 业务表对象
     * @return 外键对象
     */
    public static GenTableColumnDto getForeignColumn(GenTableDto genTable) {
        JSONObject optionsObj = JSONObject.parseObject(genTable.getOptions());
        for (GenTableColumnDto subColumn : genTable.getSubTable().getSubList()) {
            if (ObjectUtil.equals(subColumn.getId(), optionsObj.getLong(GenConstants.OptionField.SUB_FOREIGN_ID.getCode()))) {
                return subColumn;
            }
        }
        return null;
    }

    /**
     * 获取包前缀
     *
     * @param packageName 包名称
     * @return 包前缀名称
     */
    public static String getPackagePrefix(String packageName) {
        int lastIndex = packageName.lastIndexOf(".");
        return StringUtils.substring(packageName, 0, lastIndex);
    }

    /**
     * 获取覆盖与隐藏字段信息
     */
    public static Map<String, Set<String>> getCoverMap(GenTableDto genTable) {
        Set<String> hideList = new HashSet<>();
        Set<String> coverList = new HashSet<>();
        JSONObject optionsObj = JSONObject.parseObject(genTable.getOptions());
        if (StrUtil.isEmpty(optionsObj.getString(GenConstants.OptionField.IS_TENANT.getCode())) && StrUtil.equals(optionsObj.getString(GenConstants.OptionField.IS_TENANT.getCode()), GenConstants.Status.TRUE.getCode())) {
            coverList.addAll(Arrays.asList(GenConstants.TENANT_ENTITY));
        }
        if (!genTable.isMerge()) {
            hideList.addAll(Arrays.asList(GenConstants.BASE_ENTITY));
            if (StrUtil.equals(optionsObj.getString(GenConstants.OptionField.COVER_ID.getCode()), GenConstants.Status.TRUE.getCode())) {
                coverList.add(GenConstants.CoverField.ID.getCode());
            }
            if (StrUtil.equals(optionsObj.getString(GenConstants.OptionField.COVER_NAME.getCode()), GenConstants.Status.TRUE.getCode())) {
                coverList.add(GenConstants.CoverField.NAME.getCode());
            }
            if (StrUtil.equals(optionsObj.getString(GenConstants.OptionField.COVER_STATUS.getCode()), GenConstants.Status.TRUE.getCode())) {
                coverList.add(GenConstants.CoverField.STATUS.getCode());
            }
            if (StrUtil.equals(optionsObj.getString(GenConstants.OptionField.COVER_SORT.getCode()), GenConstants.Status.TRUE.getCode())) {
                coverList.add(GenConstants.CoverField.SORT.getCode());
            }
            if (genTable.isSubTree() || genTable.isTree()) {
                hideList.addAll(Arrays.asList(GenConstants.TREE_ENTITY));
                if (StrUtil.equals(optionsObj.getString(GenConstants.OptionField.COVER_TREE_ID.getCode()), GenConstants.Status.TRUE.getCode())) {
                    coverList.add(GenConstants.CoverField.ID.getCode());
                }
                if (StrUtil.equals(optionsObj.getString(GenConstants.OptionField.COVER_PARENT_ID.getCode()), GenConstants.Status.TRUE.getCode())) {
                    coverList.add(GenConstants.CoverField.NAME.getCode());
                }
                if (StrUtil.equals(optionsObj.getString(GenConstants.OptionField.COVER_ANCESTORS.getCode()), GenConstants.Status.TRUE.getCode())) {
                    coverList.add(GenConstants.CoverField.STATUS.getCode());
                }
            }
        }
        Map<String, Set<String>> map = new HashMap<>();
        hideList.removeAll(coverList);
        map.put(COVER, coverList);
        map.put(HIDE, hideList);
        return map;
    }

    /**
     * 是否为多租户
     *
     * @param genTable 业务表对象
     * @return 是否为多租户
     */
    public static boolean isTenant(GenTableDto genTable) {
        JSONObject optionsObj = JSONObject.parseObject(genTable.getOptions());
        return StrUtil.equals(optionsObj.getString(GenConstants.OptionField.IS_TENANT.getCode()), GenConstants.Status.TRUE.getCode());
    }

    /**
     * 获取源策略
     *
     * @param genTable 业务表对象
     * @return 是否为多租户
     */
    public static String getSourceMode(GenTableDto genTable) {
        JSONObject optionsObj = JSONObject.parseObject(genTable.getOptions());
        return optionsObj.getString(GenConstants.OptionField.SOURCE_MODE.getCode());
    }

    /**
     * 获取前端隐藏字段
     *
     * @param tplCategory 表模板类型
     * @return 前端隐藏字段集合
     */
    public static Set<String> getFrontHideField(String tplCategory) {
        Set<String> stringSet = new HashSet<>(Arrays.asList(GenConstants.COLUMN_MUST_HIDE));
        switch (Objects.requireNonNull(GenConstants.TemplateType.getValue(tplCategory))) {
            case TREE:
                stringSet.addAll(Arrays.asList(GenConstants.TREE_FRONT_ENTITY));
                stringSet.addAll(Arrays.asList(GenConstants.BASE_FRONT_ENTITY));
                break;
            case SUB_TREE:
                stringSet.addAll(Arrays.asList(GenConstants.TREE_FRONT_ENTITY));
            case SUB_BASE:
                stringSet.addAll(Arrays.asList(GenConstants.SUB_FRONT_ENTITY));
            case BASE:
                stringSet.addAll(Arrays.asList(GenConstants.BASE_FRONT_ENTITY));
        }
        return stringSet;
    }

    /**
     * 源策略是否为主库
     *
     * @param genTable 业务表对象
     * @return 是否为多租户
     */
    public static boolean isMasterSource(GenTableDto genTable) {
        JSONObject optionsObj = JSONObject.parseObject(genTable.getOptions());
        return StrUtil.equals(optionsObj.getString(GenConstants.OptionField.SOURCE_MODE.getCode()), GenConstants.SourceMode.MASTER.getCode());
    }

    /**
     * 根据列类型获取导入包
     *
     * @param genTable 业务表对象
     * @return 返回需要导入的包列表
     */
    public static HashSet<String> getImportList(GenTableDto genTable, Set<String> hideList) {
        List<GenTableColumnDto> columns = genTable.getSubList();
        GenTableDto subGenTableDto = genTable.getSubTable();
        HashSet<String> importList = new HashSet<>();
        if (StringUtils.isNotNull(subGenTableDto)) {
            importList.add("java.util.List");
        }
        int hideLength = hideList.size();
        String[] hides = hideList.toArray(new String[hideLength]);
        for (GenTableColumnDto column : columns) {
            if (StrUtil.equals(GenConstants.JavaType.DATE.getCode(), column.getJavaType()) && !StrUtil.equalsAny(column.getJavaField(), hides)) {
                importList.add("java.util.Date");
                importList.add("com.fasterxml.jackson.annotation.JsonFormat");
            } else if (StrUtil.equals(GenConstants.JavaType.BIG_DECIMAL.getCode(), column.getJavaType()) && !StrUtil.equalsAny(column.getJavaField(), hides)) {
                importList.add("java.math.BigDecimal");
            }
        }
        return importList;
    }

    /**
     * 根据列类型获取字典组
     *
     * @param genTable 业务表对象
     * @return 返回字典组
     */
    public static Map<String, String> getDictMap(GenTableDto genTable) {
        Set<String> dictTypeSet = new HashSet<>();
        for (GenTableColumnDto column : genTable.getSubList()) {
            if (StringUtils.isNotEmpty(column.getDictType()) && StringUtils.equalsAny(
                    column.getHtmlType(),
                    new String[]{GenConstants.DisplayType.SELECT.getCode(), GenConstants.DisplayType.CHECKBOX_GROUP.getCode(), GenConstants.DisplayType.RADIO_BUTTON_GROUP.getCode(), GenConstants.DisplayType.RADIO_GROUP.getCode()})) {
                dictTypeSet.add(column.getDictType());
                column.setDictName(getDictName(column.getDictType()));
            }
        }
        Map<String, String> dictMap = new HashMap<>();
        for (String dictType : dictTypeSet) {
            dictMap.put(dictType, getDictName(dictType));
        }
        return CollUtil.isNotEmpty(dictTypeSet) ? dictMap : null;
    }

    /**
     * 字典名称组装
     *
     * @param dictType 字典类型
     * @return 返回字典名称
     */
    public static String getDictName(String dictType) {
        for (String removeName : GenConstants.DICT_TYPE_REMOVE) {
            if (dictType.startsWith(removeName))
                return StringUtils.convertToCamelCase(StrUtil.removePrefix(dictType, removeName)) + GenConstants.DICT_NAME_ENDING;
        }
        return StringUtils.convertToCamelCase(dictType) + GenConstants.DICT_NAME_ENDING;
    }

    /**
     * 获取数据表排除字段
     *
     * @param genTable 业务表对象
     * @return 返回字典组
     */
    public static Set<String> getExcludePropertyList(GenTableDto genTable) {
        Set<String> excludeList = new HashSet<>();
        String[] columnNames = genTable.getSubList().stream().map(GenTableColumnDto::getJavaField).toArray(String[]::new);
        if (!genTable.isMerge() && ArrayUtil.isNotEmpty(GenConstants.BASE_ENTITY)) {
            for (String field : GenConstants.BASE_ENTITY) {
                if (!StrUtil.equalsAny(field, columnNames)) {
                    excludeList.add(field);
                }
            }
        }
        if ((genTable.isTree() || genTable.isSubTree()) && ArrayUtil.isNotEmpty(GenConstants.TREE_ENTITY)) {
            for (String field : GenConstants.TREE_ENTITY) {
                if (!StrUtil.equalsAny(field, columnNames)) {
                    excludeList.add(field);
                }
            }
        }
        if ((genTable.isSubBase() || genTable.isSubTree()) && ArrayUtil.isNotEmpty(GenConstants.SUB_ENTITY)) {
            for (String field : GenConstants.SUB_ENTITY) {
                if (!StrUtil.equalsAny(field, columnNames)) {
                    excludeList.add(field);
                }
            }
        }
        return excludeList;
    }

    /**
     * 获取归属模块Id字段
     *
     * @param optionsObj 生成其他选项
     * @return 归属模块Id字段
     */
    public static Long getParentModuleId(JSONObject optionsObj) {
        return optionsObj.containsKey(GenConstants.OptionField.PARENT_MODULE_ID.getCode())
                ? optionsObj.getLong(GenConstants.OptionField.PARENT_MODULE_ID.getCode())
                : AuthorityConstants.MODULE_DEFAULT_NODE;
    }

    /**
     * 获取上级菜单Id字段
     *
     * @param optionsObj 生成其他选项
     * @return 上级菜单Id字段
     */
    public static Long getParentMenuId(JSONObject optionsObj) {
        return optionsObj.containsKey(GenConstants.OptionField.PARENT_MENU_ID.getCode())
                ? optionsObj.getLong(GenConstants.OptionField.PARENT_MENU_ID.getCode())
                : AuthorityConstants.MENU_TOP_NODE;
    }

}