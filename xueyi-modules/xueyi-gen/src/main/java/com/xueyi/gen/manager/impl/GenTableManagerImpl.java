package com.xueyi.gen.manager.impl;

import com.xueyi.common.core.context.SecurityContextHolder;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.datasource.annotation.Isolate;
import com.xueyi.common.datasource.annotation.Master;
import com.xueyi.common.web.entity.manager.impl.BaseManagerImpl;
import com.xueyi.gen.domain.dto.GenTableDto;
import com.xueyi.gen.domain.model.GenTableConverter;
import com.xueyi.gen.domain.po.GenTablePo;
import com.xueyi.gen.domain.query.GenTableQuery;
import com.xueyi.gen.manager.IGenTableManager;
import com.xueyi.gen.mapper.GenTableMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 业务管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class GenTableManagerImpl extends BaseManagerImpl<GenTableQuery, GenTableDto, GenTablePo, GenTableMapper, GenTableConverter> implements IGenTableManager {

    /**
     * 查询数据库列表
     *
     * @param table 业务对象
     * @return 数据库表集合
     */
    @Master
    @Override
    public List<GenTableDto> selectDbTableList(GenTableQuery table) {
        return selectDbTableList(table, null);
    }

    /**
     * 查询数据库列表
     *
     * @param table      业务对象
     * @param sourceName 数据源
     * @return 数据库表集合
     */
    @Isolate
    @Override
    public List<GenTableDto> selectDbTableList(GenTableQuery table, String sourceName) {
        if (StrUtil.isNotBlank(sourceName)) {
            SecurityContextHolder.setSourceName(sourceName);
        }
        List<GenTableDto> list = baseMapper.selectDbTableList(table);
        if (StrUtil.isNotBlank(sourceName)) {
            SecurityContextHolder.rollLastSourceName();
        }
        return list;
    }

    /**
     * 根据表名称组查询数据库列表 | 主库
     *
     * @param names 表名称组
     * @return 数据库表集合
     */
    @Master
    @Override
    public List<GenTableDto> selectDbTableListByNames(String[] names) {
        return selectDbTableListByNames(names, null);
    }

    /**
     * 根据表名称组查询数据库列表 | 租户库
     *
     * @param names      表名称组
     * @param sourceName 数据源
     * @return 数据库表集合
     */
    @Isolate
    @Override
    public List<GenTableDto> selectDbTableListByNames(String[] names, String sourceName) {
        if (StrUtil.isNotBlank(sourceName)) {
            SecurityContextHolder.setSourceName(sourceName);
        }
        List<GenTableDto> list = baseMapper.selectDbTableListByNames(names);
        if (StrUtil.isNotBlank(sourceName)) {
            SecurityContextHolder.rollLastSourceName();
        }
        return list;
    }
}
