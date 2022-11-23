package com.xueyi.gen.manager;

import com.xueyi.common.web.entity.manager.IBaseManager;
import com.xueyi.gen.domain.dto.GenTableDto;
import com.xueyi.gen.domain.query.GenTableQuery;

import java.io.Serializable;
import java.util.List;

/**
 * 业务管理 数据封装层
 *
 * @author xueyi
 */
public interface IGenTableManager extends IBaseManager<GenTableQuery, GenTableDto> {

    /**
     * 查询数据库列表
     *
     * @param genTableDto 业务对象
     * @return 数据库表集合
     */
    public List<GenTableDto> selectDbTableList(GenTableDto genTableDto);

    /**
     * 根据表名称组查询数据库列表
     *
     * @param names 表名称组
     * @return 数据库表集合
     */
    public List<GenTableDto> selectDbTableListByNames(String[] names);

    /**
     * 根据表名称查询数据库列表
     *
     * @param name 表名称
     * @return 数据库表
     */
    public GenTableDto selectDbTableByName(String name);

    /**
     * 修改其它生成选项
     *
     * @param id      Id
     * @param options 其它生成选项
     * @return 结果
     */
    public int updateOptions(Serializable id, String options);
}
