package com.xueyi.gen.service;

import com.alibaba.fastjson2.JSONObject;
import com.xueyi.common.core.constant.basic.ServiceConstants;
import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.gen.domain.dto.GenTableDto;
import com.xueyi.gen.domain.query.GenTableQuery;

import java.util.List;

/**
 * 业务管理 服务层
 *
 * @author xueyi
 */
public interface IGenTableService extends IBaseService<GenTableQuery, GenTableDto> {

    /**
     * 查询据库列表
     *
     * @param genTable 业务信息
     * @return 数据库表集合
     */
    List<GenTableDto> selectDbTableList(GenTableDto genTable);

    /**
     * 查询据库列表
     *
     * @param tableNames 表名称组
     * @return 数据库表集合
     */
    List<GenTableDto> selectDbTableListByNames(String[] tableNames);

    /**
     * 导入表结构
     *
     * @param tableList 导入表列表
     */
    void importGenTable(List<GenTableDto> tableList);

    /**
     * 预览代码
     *
     * @param id         Id
     * @param fromSource 访问来源
     * @return 预览数据列表
     */
    List<JSONObject> previewCode(Long id, ServiceConstants.FromSource fromSource);

    /**
     * 生成代码（下载方式）
     *
     * @param id         Id
     * @param fromSource 访问来源
     * @return 数据
     */
    byte[] downloadCode(Long id, ServiceConstants.FromSource fromSource);

    /**
     * 生成代码（自定义路径）
     *
     * @param id         Id
     * @param fromSource 访问来源
     */
    void generatorCode(Long id, ServiceConstants.FromSource fromSource);

    /**
     * 批量生成代码（下载方式）
     *
     * @param ids        Ids数组
     * @param fromSource 访问来源
     * @return 数据
     */
    byte[] downloadCode(Long[] ids, ServiceConstants.FromSource fromSource);

    /**
     * 修改保存参数校验
     *
     * @param genTable 业务信息
     */
    void validateEdit(GenTableDto genTable);

}