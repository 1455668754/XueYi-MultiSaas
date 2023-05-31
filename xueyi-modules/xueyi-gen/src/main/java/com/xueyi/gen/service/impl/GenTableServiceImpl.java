package com.xueyi.gen.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.xueyi.common.core.constant.basic.DictConstants;
import com.xueyi.common.core.constant.basic.HttpConstants;
import com.xueyi.common.core.constant.basic.ServiceConstants;
import com.xueyi.common.core.constant.gen.GenConstants.OptionField;
import com.xueyi.common.core.constant.gen.GenConstants.TemplateType;
import com.xueyi.common.core.utils.core.ArrayUtil;
import com.xueyi.common.core.utils.core.CharsetUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.result.R;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.gen.config.GenConfig;
import com.xueyi.gen.correlate.GenTableCorrelate;
import com.xueyi.gen.domain.dto.GenTableColumnDto;
import com.xueyi.gen.domain.dto.GenTableDto;
import com.xueyi.gen.domain.query.GenTableQuery;
import com.xueyi.gen.manager.IGenTableManager;
import com.xueyi.gen.service.IGenTableColumnService;
import com.xueyi.gen.service.IGenTableService;
import com.xueyi.gen.util.GenUtils;
import com.xueyi.gen.util.VelocityInitializer;
import com.xueyi.gen.util.VelocityUtils;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.feign.RemoteMenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 业务管理 业务层处理
 *
 * @author xueyi
 */
@Slf4j
@Service
public class GenTableServiceImpl extends BaseServiceImpl<GenTableQuery, GenTableDto, GenTableCorrelate, IGenTableManager> implements IGenTableService {

    @Autowired
    private RemoteMenuService remoteMenuService;

    @Autowired
    private IGenTableColumnService subService;

    /**
     * 获取后端代码生成地址
     *
     * @param table      业务表信息
     * @param template   模板文件路径
     * @param fromSource 访问来源
     * @return 生成地址
     */
    public static String getGenPath(GenTableDto table, String template, ServiceConstants.FromSource fromSource) {
        String genPath = table.getGenPath();
        if (StrUtil.equals(genPath, StrUtil.SLASH)) {
            String prefixPath = System.getProperty("user.dir") + File.separator + "src" + File.separator;
            return prefixPath + VelocityUtils.getFileName(prefixPath, template, table, fromSource);
        }
        String prefixPath = genPath + File.separator;
        return prefixPath + VelocityUtils.getFileName(prefixPath, template, table, fromSource);
    }

    /**
     * 获取前端代码生成地址
     *
     * @param table      业务表信息
     * @param template   模板文件路径
     * @param fromSource 访问来源
     * @return 生成地址
     */
    public static String getUiPath(GenTableDto table, String template, ServiceConstants.FromSource fromSource) {
        String uiPath = table.getUiPath();
        if (StrUtil.equals(uiPath, StrUtil.SLASH)) {
            String prefixPath = System.getProperty("user.dir") + File.separator;
            return prefixPath + VelocityUtils.getFileName(prefixPath, template, table, fromSource);
        }
        String prefixPath = uiPath + File.separator;
        return prefixPath + VelocityUtils.getFileName(prefixPath, template, table, fromSource);
    }

    /**
     * 查询数据库列表
     *
     * @param genTableDto 业务对象
     * @return 数据库表集合
     */
    @Override
    public List<GenTableDto> selectDbTableList(GenTableDto genTableDto) {
        return baseManager.selectDbTableList(genTableDto);
    }

    /**
     * 根据表名称组查询数据库列表
     *
     * @param tableNames 表名称组
     * @return 数据库表集合
     */
    @Override
    public List<GenTableDto> selectDbTableListByNames(String[] tableNames) {
        return baseManager.selectDbTableListByNames(tableNames);
    }

    /**
     * 导入表结构
     *
     * @param tableList 导入表列表
     */
    @Override
    @DSTransactional
    public void importGenTable(List<GenTableDto> tableList) {
        try {
            tableList.forEach(table -> {
                GenUtils.initTable(table);
                int row = baseManager.insert(table);
                if (row > 0) {
                    List<GenTableColumnDto> columnList = subService.selectDbTableColumnsByName(table.getName());
                    columnList.forEach(column -> GenUtils.initColumnField(column, table));
                    subService.insertBatch(columnList);
                    GenUtils.initTableOptions(columnList, table);
                    baseManager.update(table);
                }
            });
        } catch (Exception e) {
            AjaxResult.warn(StrUtil.format("导入失败：{}", e.getMessage()));
        }
    }

    /**
     * 修改业务
     *
     * @param table 业务信息
     */
    @Override
    @DSTransactional
    public int update(GenTableDto table) {
        int row = baseManager.update(table);
        if (row > 0) GenUtils.updateCheckColumn(table);
        table.getSubList().forEach(column -> subService.update(column));
        return row;
    }

    /**
     * 预览代码
     *
     * @param id         Id
     * @param fromSource 访问来源
     * @return 预览数据列表
     */
    @Override
    public List<JSONObject> previewCode(Long id, ServiceConstants.FromSource fromSource) {
        List<JSONObject> dataMap = new ArrayList<>();
        // 查询表信息
        GenTableDto table = initTable(id);

        VelocityInitializer.initVelocity();

        VelocityContext context = VelocityUtils.prepareContext(table);

        // 获取模板列表
        JSONObject data;
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory(), fromSource);
        for (String template : templates) {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, HttpConstants.Character.UTF8.getCode());
            tpl.merge(context, sw);
            data = new JSONObject();
            String vmName = StrUtil.subAfter(template, StrUtil.SLASH, true);
            vmName = StrUtil.removeSuffix(vmName, (".vm"));
            data.put("name", vmName);
            data.put("language", StrUtil.subAfter(vmName, StrUtil.DOT, true));
            data.put("template", sw.toString());
            dataMap.add(data);
        }
        return dataMap;
    }

    /**
     * 生成代码（下载方式）
     *
     * @param id         Id
     * @param fromSource 访问来源
     * @return 数据
     */
    @Override
    public byte[] downloadCode(Long id, ServiceConstants.FromSource fromSource) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        generatorCode(id, zip, fromSource);
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    /**
     * 生成代码（自定义路径）
     *
     * @param id         Id
     * @param fromSource 访问来源
     */
    @Override
    public void generatorCode(Long id, ServiceConstants.FromSource fromSource) {
        // 查询表信息
        GenTableDto table = initTable(id);

        VelocityInitializer.initVelocity();
        VelocityContext context = VelocityUtils.prepareContext(table);

        // 获取模板列表
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory(), fromSource);
        String[] genFiles = {"merge.java.vm", "mergeMapper.java.vm", "query.java.vm", "dto.java.vm", "po.java.vm", "converter.java.vm", "controller.java.vm", "service.java.vm", "serviceImpl.java.vm", "manager.java.vm", "managerImpl.java.vm", "manager.java.vm", "mapper.java.vm", "sql.sql.vm"};
        for (String template : templates) {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, HttpConstants.Character.UTF8.getCode());
            tpl.merge(context, sw);
            try {
                String path = StrUtil.containsAny(template, genFiles) ? getGenPath(table, template, fromSource) : getUiPath(table, template, fromSource);
                FileUtils.writeStringToFile(new File(path), sw.toString(), CharsetUtil.UTF_8);
            } catch (IOException e) {
                AjaxResult.warn(StrUtil.format("渲染模板失败，表名：{}", table.getName()));
            }
        }
    }

    /**
     * 批量生成代码（下载方式）
     *
     * @param ids        Ids数组
     * @param fromSource 访问来源
     * @return 数据
     */
    @Override
    public byte[] downloadCode(Long[] ids, ServiceConstants.FromSource fromSource) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (Long id : ids) {
            generatorCode(id, zip, fromSource);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    /**
     * 查询表信息并生成代码
     *
     * @param id         Id
     * @param zip        压缩包流
     * @param fromSource 访问来源
     */
    private void generatorCode(Long id, ZipOutputStream zip, ServiceConstants.FromSource fromSource) {

        // 查询表信息
        GenTableDto table = initTable(id);

        VelocityInitializer.initVelocity();
        VelocityContext context = VelocityUtils.prepareContext(table);

        // 获取模板列表
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory(), fromSource);
        for (String template : templates) {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, HttpConstants.Character.UTF8.getCode());
            tpl.merge(context, sw);
            try {
                String fileUrl = VelocityUtils.getFileName(StrUtil.EMPTY, template, table, fromSource);
                // 添加到zip
                zip.putNextEntry(new ZipEntry(fileUrl));
                IOUtils.write(sw.toString(), zip, HttpConstants.Character.UTF8.getCode());
                IOUtils.closeQuietly(sw);
                zip.flush();
                zip.closeEntry();
            } catch (IOException e) {
                log.error("渲染模板失败，表名：" + table.getName(), e);
            }
        }
    }

    /**
     * 修改保存参数校验
     *
     * @param genTable 业务信息
     */
    @Override
    public void validateEdit(GenTableDto genTable) {
        JSONObject optionsObj = JSON.parseObject(genTable.getOptions());
        checkTclBasic(genTable, optionsObj);
        switch (TemplateType.getByCode(genTable.getTplCategory())) {
            case TREE:
                checkTclTree(optionsObj);
            case BASE:
                checkTclBase(optionsObj);
        }
    }

    /**
     * 校验基础表配置
     *
     * @param genTable   业务表
     * @param optionsObj 其它生成选项信息
     */
    private void checkTclBasic(GenTableDto genTable, JSONObject optionsObj) {
        checkSourceMode(genTable, optionsObj);
        checkCommonMode(genTable, optionsObj);

    }

    /**
     * 校验基础源策略模式配置
     *
     * @param genTable   业务表
     * @param optionsObj 其它生成选项信息
     */
    private void checkSourceMode(GenTableDto genTable, JSONObject optionsObj) {
        if (StrUtil.isEmpty(optionsObj.getString(OptionField.SOURCE_MODE.getCode()))) {
            AjaxResult.warn("未设置源策略模式！");
        }
        if (StrUtil.isNotEmpty(optionsObj.getString(OptionField.IS_TENANT.getCode())) && StrUtil.equals(optionsObj.getString(OptionField.IS_TENANT.getCode()), DictConstants.DicYesNo.YES.getCode())) {
            for (GenTableColumnDto column : genTable.getSubList()) {
                if (ArrayUtil.contains(GenConfig.getEntity().getBack().getTenant(), column.getJavaField())) {
                    return;
                }
            }
            AjaxResult.warn("未在业务表中发现多租户关键字，请关闭多租户模式重试！");
        }
    }

    /**
     * 校验数据混合模式配置
     *
     * @param genTable   业务表
     * @param optionsObj 其它生成选项信息
     */
    private void checkCommonMode(GenTableDto genTable, JSONObject optionsObj) {
        if (StrUtil.isEmpty(optionsObj.getString(OptionField.COMMON_MODE.getCode()))) {
            // 暂不开启 | 兼容vue2 next-ui变更
//            AjaxResult.warn("未设置数据混合模式！");
            return;
        }
        if (StrUtil.isNotEmpty(optionsObj.getString(OptionField.IS_COMMON.getCode())) && StrUtil.equals(optionsObj.getString(OptionField.IS_COMMON.getCode()), DictConstants.DicYesNo.YES.getCode())) {
            for (GenTableColumnDto column : genTable.getSubList()) {
                if (ArrayUtil.contains(GenConfig.getEntity().getBack().getCommon(), column.getJavaField())) {
                    return;
                }
            }
            AjaxResult.warn("未在业务表中发现公共数据关键字，请关闭数据混合模式重试！");
        }
    }

    /**
     * 校验单表配置
     *
     * @param optionsObj 其它生成选项信息
     */
    private void checkTclBase(JSONObject optionsObj) {
        if (StrUtil.isEmpty(optionsObj.getString(OptionField.PARENT_MODULE_ID.getCode())))
            AjaxResult.warn("归属模块不能为空");
        else if (StrUtil.isEmpty(optionsObj.getString(OptionField.PARENT_MENU_ID.getCode())))
            AjaxResult.warn("归属菜单不能为空");
    }

    /**
     * 校验树表配置
     *
     * @param optionsObj 其它生成选项信息
     */
    private void checkTclTree(JSONObject optionsObj) {
        if (StrUtil.isEmpty(optionsObj.getString(OptionField.TREE_ID.getCode())))
            AjaxResult.warn("树编码字段不能为空");
        else if (StrUtil.isEmpty(optionsObj.getString(OptionField.PARENT_ID.getCode())))
            AjaxResult.warn("树父编码字段不能为空");
        else if (StrUtil.isEmpty(optionsObj.getString(OptionField.TREE_NAME.getCode())))
            AjaxResult.warn("树名称字段不能为空");
        else if (StrUtil.isEmpty(optionsObj.getString(OptionField.ANCESTORS.getCode())))
            AjaxResult.warn("树祖籍列表字段不能为空");
    }

    /**
     * 初始化代码生成表数据
     *
     * @param id Id
     * @return 业务表对象
     */
    private GenTableDto initTable(Long id) {
        GenTableDto table = baseManager.selectByIdMerge(id);
        JSONObject optionsObj = JSON.parseObject(table.getOptions());
        // 设置列信息
        switch (TemplateType.getByCode(table.getTplCategory())) {
            case TREE:
                setTreeTable(table, optionsObj);
            case BASE:
                setBaseTable(table, optionsObj);
        }
        setMenuOptions(table, optionsObj);
        return table;
    }

    /**
     * 设置基础表信息
     *
     * @param table      业务表信息
     * @param optionsObj 其它生成选项信息
     */
    private void setBaseTable(GenTableDto table, JSONObject optionsObj) {
        table.getSubList().forEach(column -> {
            if (column.getIsPk()) table.setPkColumn(column);
        });
    }

    /**
     * 设置树表信息
     *
     * @param table      业务表信息
     * @param optionsObj 其它生成选项信息
     */
    private void setTreeTable(GenTableDto table, JSONObject optionsObj) {
    }

    /**
     * 设置菜单信息
     *
     * @param table      业务表信息
     * @param optionsObj 其它生成选项信息
     */
    private void setMenuOptions(GenTableDto table, JSONObject optionsObj) {
        Long menuId = optionsObj.getLong(OptionField.PARENT_MENU_ID.getCode());
        R<SysMenuDto> result = remoteMenuService.getInfoInner(menuId);
        if (result.isFail()) AjaxResult.warn("菜单服务异常，请联系管理员！");
        else if (ObjectUtil.isNull(result.getData())) AjaxResult.warn("该服务对应的菜单已被删除，请先修改后再生成代码！");
        if (StrUtil.isEmpty(result.getData().getAncestors()))
            optionsObj.put(OptionField.PARENT_MENU_ANCESTORS.getCode(), result.getData().getId());
        else
            optionsObj.put(OptionField.PARENT_MENU_ANCESTORS.getCode(), result.getData().getAncestors() + "," + result.getData().getId());
        table.setOptions(optionsObj.toString());
    }

}
