package com.xueyi.gen.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.xueyi.common.core.constant.basic.DictConstants;
import com.xueyi.common.core.constant.basic.ServiceConstants;
import com.xueyi.common.core.constant.gen.GenConstants;
import com.xueyi.common.core.constant.system.AuthorityConstants;
import com.xueyi.common.core.utils.core.*;
import com.xueyi.gen.config.GenConfig;
import com.xueyi.gen.domain.dto.GenTableColumnDto;
import com.xueyi.gen.domain.dto.GenTableDto;
import org.apache.velocity.VelocityContext;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 模板工具类
 *
 * @author xueyi
 */
public class VelocityUtils {

    /** 主目录 */
    private static final String PROJECT_PATH = "main/java" ;

    /** 隐藏字段数组 */
    private static final String HIDE = "hide" ;

    /** 覆写字段数组 */
    private static final String COVER = "cover" ;

    /**
     * 设置模板变量信息
     *
     * @return 模板列表
     */
    public static VelocityContext prepareContext(GenTableDto genTable) {
        String businessName = genTable.getBusinessName();
        String packageName = genTable.getPackageName();
        String tplCategory = genTable.getTplCategory();
        String functionName = genTable.getFunctionName();
        JSONObject optionsObj = JSON.parseObject(genTable.getOptions());
        Map<String, Set<String>> domainMap = getCoverMap(genTable);
        initTableField(genTable);
        VelocityContext velocityContext = new VelocityContext();
        // 模板类型
        velocityContext.put("tplCategory", genTable.getTplCategory());
        // 数据库表名
        velocityContext.put("tableName", genTable.getName());
        // 生成功能名
        velocityContext.put("functionName", StrUtil.isNotEmpty(functionName) ? functionName : "【请填写功能名称】");
        // 实体类名称(首字母大写)
        velocityContext.put("ClassName", genTable.getClassName());
        // 实体类名称(首字母小写)
        velocityContext.put("className", StrUtil.uncapitalize(genTable.getClassName()));
        // 实体类名称(全大写 | _划分)
        velocityContext.put("ClASS_NAME", (StrUtil.toUnderlineCase(genTable.getClassName())).toUpperCase());
        // 实体类名称(首字母大写 | 无前缀)
        velocityContext.put("ClassNameNoPrefix", genTable.getClassName().replaceFirst(genTable.getPrefix(), StrUtil.EMPTY));
        // 实体类名称(首字母小写 | 无前缀)
        velocityContext.put("classNameNoPrefix", StrUtil.uncapitalize(genTable.getClassName().replaceFirst(genTable.getPrefix(), StrUtil.EMPTY)));
        // 生成模块名
        velocityContext.put("moduleName", genTable.getModuleName());
        // 生成模块名
        velocityContext.put("authorityName", genTable.getAuthorityName());
        // 生成业务名(首字母大写)
        velocityContext.put("BusinessName", StrUtil.capitalize(businessName));
        // 生成业务名(首字母小写)
        velocityContext.put("businessName", businessName);
        // 生成业务名(字母全大写)
        velocityContext.put("BUSINESSName", StrUtil.upperCase(businessName));
        // 生成包路径前缀
        velocityContext.put("basePackage", getPackagePrefix(packageName));
        // 生成包路径
        velocityContext.put("packageName", packageName);
        // 作者
        velocityContext.put("author", genTable.getFunctionAuthor());
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
        velocityContext.put("mustHideField", Arrays.asList(GenConfig.getEntity().getMustHide()));
        // 是否为多租户（true | false）
        velocityContext.put("isTenant", isTenant(genTable));
        // 源策略模式
        velocityContext.put("sourceMode", getSourceMode(optionsObj));
        // 源策略是否为主库
        velocityContext.put("isMasterSource", isMasterSource(genTable));
        // 获取其他重要参数（名称、状态...）
        getOtherMainColum(velocityContext, genTable);
        // sql模板设置
        setMenuVelocityContext(velocityContext, optionsObj);
        // api设置
        setApiStatus(velocityContext, optionsObj);
        if (GenConstants.TemplateType.getByCode(tplCategory) == GenConstants.TemplateType.TREE) {
            setTreeVelocityContext(velocityContext, genTable, optionsObj);
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
    public static void setMenuVelocityContext(VelocityContext context, JSONObject optionsObj) {
        context.put("parentModuleId", getParentModuleId(optionsObj));
        context.put("parentMenuId", getParentMenuId(optionsObj));
        context.put("parentMenuAncestors", optionsObj.getString(GenConstants.OptionField.PARENT_MENU_ANCESTORS.getCode()));
        // 生成菜单menuId0-9
        for (int i = 0; i < 10; i++) {
            context.put("menuId" + i, IdUtil.getSnowflake(0, 0).nextId());
            context.put("menuName" + i, IdUtil.simpleUUID());
        }
    }

    /**
     * 设置树表模板变量信息
     */
    public static void setTreeVelocityContext(VelocityContext context, GenTableDto genTable, JSONObject optionsObj) {
        JSONObject treeObject = new JSONObject();
        genTable.getSubList().forEach(column -> {
            if (ObjectUtil.equals(column.getId(), optionsObj.getLong(GenConstants.OptionField.TREE_ID.getCode()))) {
                treeObject.put("idColumn", column);
            } else if (ObjectUtil.equals(column.getId(), optionsObj.getLong(GenConstants.OptionField.PARENT_ID.getCode()))) {
                treeObject.put("parentIdColumn", column);
            } else if (ObjectUtil.equals(column.getId(), optionsObj.getLong(GenConstants.OptionField.TREE_NAME.getCode()))) {
                treeObject.put("nameColumn", column);
            }
        });
        context.put("treeMap", treeObject);
    }

    /**
     * 设置接口变量信息
     *
     * @param context    模板列表
     * @param optionsObj 配置JSON
     */
    public static void setApiStatus(VelocityContext context, JSONObject optionsObj) {
        JSONObject apiJSon = new JSONObject();
        apiJSon.put("list", checkApiStatus(optionsObj, GenConstants.OptionField.API_LIST));
        apiJSon.put("getInfo", checkApiStatus(optionsObj, GenConstants.OptionField.API_GET_INFO));
        apiJSon.put("add", checkApiStatus(optionsObj, GenConstants.OptionField.API_ADD));
        apiJSon.put("edit", checkApiStatus(optionsObj, GenConstants.OptionField.API_EDIT));
        apiJSon.put("editStatus", checkApiStatus(optionsObj, GenConstants.OptionField.API_ES));
        apiJSon.put("batchRemove", checkApiStatus(optionsObj, GenConstants.OptionField.API_BATCH_REMOVE));
        apiJSon.put("import", checkApiStatus(optionsObj, GenConstants.OptionField.API_IMPORT));
        apiJSon.put("export", checkApiStatus(optionsObj, GenConstants.OptionField.API_EXPORT));
        apiJSon.put("cache", checkApiStatus(optionsObj, GenConstants.OptionField.API_CACHE));
        context.put("api", apiJSon);
    }

    /**
     * 校验Api是否开启
     *
     * @param optionsObj  配置JSON
     * @param optionField API类型
     * @return 结果
     */
    private static boolean checkApiStatus(JSONObject optionsObj, GenConstants.OptionField optionField) {
        return StrUtil.equals(optionsObj.getString(optionField.getCode()), DictConstants.DicYesNo.YES.getCode());
    }

    /**
     * 获取模板信息
     *
     * @param tplCategory 表模板类型
     * @param fromSource  访问来源
     * @return 模板列表
     */
    public static List<String> getTemplateList(String tplCategory, ServiceConstants.FromSource fromSource) {
        List<String> templates = new ArrayList<>();

        if (StrUtil.equals(tplCategory, GenConstants.TemplateType.MERGE.getCode())) {
            templates.add("vm/java/merge/merge.java.vm");
            templates.add("vm/java/merge/mergeMapper.java.vm");
        } else {
            templates.add("vm/java/query.java.vm");
            templates.add("vm/java/dto.java.vm");
            templates.add("vm/java/po.java.vm");
            templates.add("vm/java/converter.java.vm");
            templates.add("vm/java/controller.java.vm");
            templates.add("vm/java/service.java.vm");
            templates.add("vm/java/serviceImpl.java.vm");
            templates.add("vm/java/manager.java.vm");
            templates.add("vm/java/managerImpl.java.vm");
            templates.add("vm/java/mapper.java.vm");
            templates.add("vm/sql/sql.sql.vm");
            switch (fromSource) {
                case CLOUD -> {
                    templates.add("vm/cloud/js/api.js.vm");
                    templates.add("vm/cloud/js/auth.js.vm");
                    templates.add("vm/cloud/js/enum.js.vm");
                    templates.add("vm/cloud/vue/index.vue.vm");
                }
                case MULTI -> {
                    templates.add("vm/multi/ts/api.ts.vm");
                    templates.add("vm/multi/ts/data.ts.vm");
                    templates.add("vm/multi/ts/auth.ts.vm");
                    templates.add("vm/multi/ts/enum.ts.vm");
                    templates.add("vm/multi/ts/infoModel.ts.vm");
                    templates.add("vm/multi/vue/detail.vue.vm");
                    templates.add("vm/multi/vue/index.vue.vm");
                    templates.add("vm/multi/vue/modal.vue.vm");
                }
                default -> {
                }
            }
        }
        return templates;
    }

    /**
     * 获取文件名
     *
     * @param realPath   物理路径
     * @param template   文件名
     * @param genTable   业务表数据传输对象
     * @param fromSource 访问来源
     */
    public static String getFileName(String realPath, String template, GenTableDto genTable, ServiceConstants.FromSource fromSource) {
        // 包路径
        String packageName = genTable.getPackageName();
        // 模块名
        String moduleName = genTable.getModuleName();
        // 权限名
        String authorityName = genTable.getAuthorityName();
        // 大写类名
        String className = genTable.getClassName();
        // 业务名称
        String businessName = genTable.getBusinessName();
        // 业务名称(首字母大写)
        String BusinessName = StrUtil.capitalize(genTable.getBusinessName());

        String javaPath = PROJECT_PATH + StrUtil.SLASH + StrUtil.replace(packageName, StrUtil.DOT, StrUtil.SLASH);

        if (template.contains("query.java.vm"))
            return StrUtil.format("{}/domain/query/{}Query.java", javaPath, className);
        else if (template.contains("dto.java.vm"))
            return StrUtil.format("{}/domain/dto/{}Dto.java", javaPath, className);
        else if (template.contains("po.java.vm")) return StrUtil.format("{}/domain/po/{}Po.java", javaPath, className);
        else if (template.contains("converter.java.vm"))
            return StrUtil.format("{}/domain/model/{}Converter.java", javaPath, className);
        else if (template.contains("controller.java.vm"))
            return StrUtil.format("{}/controller/{}Controller.java", javaPath, className);
        else if (template.contains("service.java.vm"))
            return StrUtil.format("{}/service/I{}Service.java", javaPath, className);
        else if (template.contains("serviceImpl.java.vm"))
            return StrUtil.format("{}/service/impl/{}ServiceImpl.java", javaPath, className);
        else if (template.contains("manager.java.vm"))
            return StrUtil.format("{}/manager/I{}Manager.java", javaPath, className);
        else if (template.contains("managerImpl.java.vm"))
            return StrUtil.format("{}/manager/impl/{}Manager.java", javaPath, className);
        else if (template.contains("mapper.java.vm"))
            return StrUtil.format("{}/mapper/{}Mapper.java", javaPath, className);
        else if (template.contains("merge.java.vm"))
            return StrUtil.format("{}/domain/merge/{}.java", javaPath, className);
        else if (template.contains("mergeMapper.java.vm"))
            return StrUtil.format("{}/mapper/merge/{}Mapper.java", javaPath, className);

        else if (template.contains("sql.sql.vm")) return StrUtil.format("sql/{}.sql", businessName);

        switch (fromSource) {
            case CLOUD -> {
                if (template.contains("api.js.vm"))
                    return StrUtil.format("xueyi-ui/src/api/{}/{}/{}.js", moduleName, authorityName, businessName);
                else if (template.contains("auth.js.vm"))
                    return StrUtil.format("xueyi-ui/src/constants/auth/{}/{}/{}.auth.js", moduleName, authorityName, businessName);
                else if (template.contains("enum.js.vm"))
                    return StrUtil.format("xueyi-ui/src/constants/enums/{}/{}/{}.enum.js", moduleName, authorityName, businessName);

                else if (template.contains("index.vue.vm"))
                    return StrUtil.format("xueyi-ui/src/views/{}/{}/{}/index.vue", moduleName, authorityName, businessName);
            }
            case MULTI -> {
                if (template.contains("api.ts.vm"))
                    return StrUtil.format("multi-ui/src/api/{}/{}/{}.ts", moduleName, authorityName, businessName);
                else if (template.contains("infoModel.ts.vm")) {
                    String prefixPath = "multi-ui/src/model" ;
                    String suffixFile = "" ;
                    initIndexFile(realPath, prefixPath, suffixFile, moduleName, authorityName, businessName);
                    return StrUtil.format("{}/{}/{}/{}.ts", prefixPath, moduleName, authorityName, businessName);
                } else if (template.contains("auth.ts.vm")) {
                    String prefixPath = "multi-ui/src/auth" ;
                    String suffixFile = ".auth" ;
                    initIndexFile(realPath, prefixPath, suffixFile, moduleName, authorityName, businessName);
                    return StrUtil.format("{}/{}/{}/{}{}.ts", prefixPath, moduleName, authorityName, businessName, suffixFile);
                } else if (template.contains("enum.ts.vm")) {
                    String prefixPath = "multi-ui/src/enums" ;
                    String suffixFile = ".enum" ;
                    initIndexFile(realPath, prefixPath, suffixFile, moduleName, authorityName, businessName);
                    return StrUtil.format("{}/{}/{}/{}{}.ts", prefixPath, moduleName, authorityName, businessName, suffixFile);
                } else if (template.contains("data.ts.vm"))
                    return StrUtil.format("multi-ui/src/views/{}/{}/{}/{}.data.ts", moduleName, authorityName, businessName, businessName);
                else if (template.contains("index.vue.vm"))
                    return StrUtil.format("multi-ui/src/views/{}/{}/{}/index.vue", moduleName, authorityName, businessName);
                else if (template.contains("detail.vue.vm"))
                    return StrUtil.format("multi-ui/src/views/{}/{}/{}/{}Detail.vue", moduleName, authorityName, businessName, BusinessName);
                else if (template.contains("modal.vue.vm"))
                    return StrUtil.format("multi-ui/src/views/{}/{}/{}/{}Modal.vue", moduleName, authorityName, businessName, BusinessName);
            }
            default -> {
            }
        }

        return "" ;
    }

    /**
     * 获取包前缀
     *
     * @param packageName 包名称
     * @return 包前缀名称
     */
    public static String getPackagePrefix(String packageName) {
        int lastIndex = packageName.lastIndexOf(StrUtil.DOT);
        return StrUtil.sub(packageName, 0, lastIndex);
    }

    /**
     * 初始化表字段
     *
     * @param genTable 业务表对象
     */
    public static void initTableField(GenTableDto genTable) {
        genTable.getSubList().forEach(item -> {
            item.setIsDivideTable(StrUtil.notEquals(item.getJavaField(), StrUtil.toCamelCase(item.getName())));
            item.setIsPo(Boolean.TRUE);
            if (item.getIsHide()) {
                item.setIsPo(Boolean.FALSE);
            } else if (ArrayUtil.contains(GenConfig.getEntity().getMustHide(), item.getJavaField())) {
                item.setIsPo(Boolean.FALSE);
            } else if (!genTable.isMerge() && ArrayUtil.contains(GenConfig.getEntity().getBack().getBase(), item.getJavaField())) {
                item.setIsPo(Boolean.FALSE);
            } else if (genTable.isTree() && ArrayUtil.contains(GenConfig.getEntity().getBack().getTree(), item.getJavaField())) {
                item.setIsPo(Boolean.FALSE);
            }
            GenConstants.GenField field = GenConstants.GenField.getByCode(item.getJavaField());
            if (ObjectUtil.isNotNull(field)) {
                switch (field) {
                    case ID, PARENT_ID -> {
                        if (item.getIsDivideTable()
                                || StrUtil.notEquals(item.getJavaType(), GenConstants.JavaType.LONG.getCode())
                                || StrUtil.notEquals(item.getQueryType(), GenConstants.QueryType.EQ.getCode())) {
                            item.setIsPo(Boolean.TRUE);
                        }
                    }
                    case NAME, REMARK -> {
                        if (item.getIsDivideTable()
                                || StrUtil.notEquals(item.getJavaType(), GenConstants.JavaType.STRING.getCode())
                                || StrUtil.notEquals(item.getQueryType(), GenConstants.QueryType.LIKE.getCode())) {
                            item.setIsPo(Boolean.TRUE);
                        }
                    }
                    case STATUS -> {
                        if (item.getIsDivideTable()
                                || StrUtil.notEquals(item.getJavaType(), GenConstants.JavaType.STRING.getCode())
                                || StrUtil.notEquals(item.getQueryType(), GenConstants.QueryType.EQ.getCode())
                                || !StrUtil.contains(item.getComment(), GenConstants.GenCheck.NORMAL_DISABLE.getInfo())) {
                            item.setIsPo(Boolean.TRUE);
                        }
                    }
                    case SORT -> {
                        if (item.getIsDivideTable()
                                || StrUtil.notEquals(item.getJavaType(), GenConstants.JavaType.INTEGER.getCode())
                                || StrUtil.notEquals(item.getQueryType(), GenConstants.QueryType.EQ.getCode())) {
                            item.setIsPo(Boolean.TRUE);
                        }
                    }
                    case ANCESTORS -> {
                        if (item.getIsDivideTable()
                                || StrUtil.notEquals(item.getJavaType(), GenConstants.JavaType.STRING.getCode())
                                || StrUtil.notEquals(item.getQueryType(), GenConstants.QueryType.EQ.getCode())) {
                            item.setIsPo(Boolean.TRUE);
                        }
                    }
                    default -> {
                    }
                }
            }
        });
    }

    /**
     * 获取覆盖与隐藏字段信息
     */
    public static Map<String, Set<String>> getCoverMap(GenTableDto genTable) {
        Set<String> coverSet = genTable.getSubList().stream().filter(GenTableColumnDto::getIsCover).map(GenTableColumnDto::getJavaField).collect(Collectors.toSet());
        Set<String> hideSet = genTable.getSubList().stream().filter(GenTableColumnDto::getIsHide).map(GenTableColumnDto::getJavaField).collect(Collectors.toSet());
        switch (GenConstants.TemplateType.getByCode(genTable.getTplCategory())) {
            case TREE -> {
                Set<String> treeSet = new HashSet<>(Arrays.asList(ArrayUtil.addAll(GenConfig.getEntity().getBack().getBase(), GenConfig.getEntity().getBack().getTree())));
                treeSet.removeAll(coverSet);
                hideSet.addAll(treeSet);
            }
            case BASE -> {
                Set<String> baseSet = new HashSet<>(Arrays.asList(ArrayUtil.addAll(GenConfig.getEntity().getBack().getBase())));
                baseSet.removeAll(coverSet);
                hideSet.addAll(baseSet);
            }
        }
        Map<String, Set<String>> map = new HashMap<>();
        map.put(COVER, coverSet);
        map.put(HIDE, hideSet);
        return map;
    }

    /**
     * 是否为多租户
     *
     * @param genTable 业务表对象
     * @return 是否为多租户
     */
    public static boolean isTenant(GenTableDto genTable) {
        JSONObject optionsObj = JSON.parseObject(genTable.getOptions());
        return StrUtil.equals(optionsObj.getString(GenConstants.OptionField.IS_TENANT.getCode()), DictConstants.DicYesNo.YES.getCode());
    }

    /**
     * 获取源策略
     *
     * @param optionsObj 生成其他选项
     * @return 是否为多租户
     */
    public static String getSourceMode(JSONObject optionsObj) {
        return optionsObj.getString(GenConstants.OptionField.SOURCE_MODE.getCode());
    }

    /**
     * 获取前端隐藏字段
     *
     * @param tplCategory 表模板类型
     * @return 前端隐藏字段集合
     */
    public static Set<String> getFrontHideField(String tplCategory) {
        Set<String> stringSet = new HashSet<>(Arrays.asList(GenConfig.getEntity().getMustHide()));
        switch (GenConstants.TemplateType.getByCode(tplCategory)) {
            case TREE -> {
                stringSet.addAll(Arrays.asList(GenConfig.getEntity().getFront().getTree()));
                stringSet.addAll(Arrays.asList(GenConfig.getEntity().getFront().getBase()));
            }
            case BASE -> stringSet.addAll(Arrays.asList(GenConfig.getEntity().getFront().getBase()));
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
        JSONObject optionsObj = JSON.parseObject(genTable.getOptions());
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
        if (ObjectUtil.isNotNull(subGenTableDto)) {
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
            if (StrUtil.isNotEmpty(column.getDictType()) && StrUtil.equalsAny(column.getHtmlType(), GenConstants.DisplayType.SELECT.getCode(), GenConstants.DisplayType.CHECKBOX_GROUP.getCode(), GenConstants.DisplayType.RADIO_BUTTON_GROUP.getCode(), GenConstants.DisplayType.RADIO_GROUP.getCode())) {
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
        for (String removeName : GenConfig.getDictTypeRemove()) {
            if (dictType.startsWith(removeName))
                return StrUtil.format(GenConstants.DICT_NAME, StrUtil.convertToCamelCase(StrUtil.removePrefix(dictType, removeName)));
        }
        return StrUtil.format(GenConstants.DICT_NAME, StrUtil.convertToCamelCase(dictType));
    }

    /**
     * 获取数据表排除字段
     *
     * @param genTable 业务表对象
     * @return 返回字典组
     */
    public static Set<String> getExcludePropertyList(GenTableDto genTable) {
        String[] columnNames = genTable.getSubList().stream().map(GenTableColumnDto::getJavaField).toArray(String[]::new);
        if (genTable.isMerge())
            return new HashSet<>();
        Set<String> excludeList = new HashSet<>();
        if (ArrayUtil.isNotEmpty(GenConfig.getEntity().getBack().getBase())) {
            Set<String> filedSet = Arrays.stream(GenConfig.getEntity().getBack().getBase())
                    .filter(item -> !ArrayUtil.contains(columnNames, item)).map(item -> StrUtil.toUnderlineCase(item).toUpperCase()).collect(Collectors.toSet());
            if (CollUtil.isNotEmpty(filedSet))
                excludeList.addAll(filedSet);
        }
        if (genTable.isTree() && ArrayUtil.isNotEmpty(GenConfig.getEntity().getBack().getTree())) {
            Set<String> filedSet = Arrays.stream(GenConfig.getEntity().getBack().getTree())
                    .filter(item -> !ArrayUtil.contains(columnNames, item)).map(item -> StrUtil.toUnderlineCase(item).toUpperCase()).collect(Collectors.toSet());
            if (CollUtil.isNotEmpty(filedSet))
                excludeList.addAll(filedSet);
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
        return optionsObj.containsKey(GenConstants.OptionField.PARENT_MODULE_ID.getCode()) ? optionsObj.getLong(GenConstants.OptionField.PARENT_MODULE_ID.getCode()) : AuthorityConstants.MODULE_DEFAULT_NODE;
    }

    /**
     * 获取上级菜单Id字段
     *
     * @param optionsObj 生成其他选项
     * @return 上级菜单Id字段
     */
    public static Long getParentMenuId(JSONObject optionsObj) {
        return optionsObj.containsKey(GenConstants.OptionField.PARENT_MENU_ID.getCode()) ? optionsObj.getLong(GenConstants.OptionField.PARENT_MENU_ID.getCode()) : AuthorityConstants.MENU_TOP_NODE;
    }

    /**
     * 获取生成相对路径
     *
     * @param table    业务表对象
     * @param subTable 子业务表对象
     * @return 生成相对路径
     */
    public static String getRelativePath(GenTableDto table, GenTableDto subTable) {
        return StrUtil.equals(table.getModuleName(), subTable.getModuleName()) ? StrUtil.equals(table.getAuthorityName(), subTable.getAuthorityName()) ? StrUtil.DOT : StrUtil.DOUBLE_DOT + StrUtil.SLASH + subTable.getAuthorityName() : StrUtil.DOUBLE_DOT + StrUtil.SLASH + StrUtil.DOUBLE_DOT + StrUtil.SLASH + subTable.getAuthorityName();
    }

    /**
     * 生成前端index文件
     *
     * @param realPath      项目路径
     * @param prefixPath    路径前缀
     * @param suffixFile    文件后缀
     * @param moduleName    生成模块路径
     * @param authorityName 生成权限名
     * @param businessName  生成业务名
     */
    public static void initIndexFile(String realPath, String prefixPath, String suffixFile, String moduleName, String authorityName, String businessName) {
        if (StrUtil.isEmpty(realPath)) return;
        String indexName = "index.ts" ;
        String importSentence = "export * from './{}'" ;
        StringBuilder sb = new StringBuilder(realPath + prefixPath + File.separator + moduleName + File.separator);
        outIndexFile(sb + indexName, StrUtil.format(importSentence, authorityName));
        sb.append(authorityName).append(File.separator);
        outIndexFile(sb + indexName, StrUtil.format(importSentence, businessName + suffixFile));
    }

    /**
     * 生成前端index文件
     *
     * @param path     文件地址
     * @param sentence 校验语句
     */
    public static void outIndexFile(String path, String sentence) {
        if (FileUtil.exist(path)) {
            FileReader fileReader = new FileReader(path);
            if (!StrUtil.contains(fileReader.readString(), sentence)) {
                FileWriter writer = new FileWriter(path);
                writer.append(sentence + ";\r\n");
            }
        } else {
            FileUtil.touch(path);
            FileWriter writer = new FileWriter(path);
            writer.write(sentence + ";\r\n");
        }
    }
}