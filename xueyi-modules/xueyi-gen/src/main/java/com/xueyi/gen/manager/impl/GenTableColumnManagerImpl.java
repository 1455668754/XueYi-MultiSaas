package com.xueyi.gen.manager.impl;

import com.xueyi.common.core.context.SecurityContextHolder;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.datasource.annotation.Isolate;
import com.xueyi.common.datasource.annotation.Master;
import com.xueyi.common.web.entity.manager.impl.BaseManagerImpl;
import com.xueyi.gen.domain.dto.GenTableColumnDto;
import com.xueyi.gen.domain.model.GenTableColumnConverter;
import com.xueyi.gen.domain.po.GenTableColumnPo;
import com.xueyi.gen.domain.query.GenTableColumnQuery;
import com.xueyi.gen.manager.IGenTableColumnManager;
import com.xueyi.gen.mapper.GenTableColumnMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 业务字段管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class GenTableColumnManagerImpl extends BaseManagerImpl<GenTableColumnQuery, GenTableColumnDto, GenTableColumnPo, GenTableColumnMapper, GenTableColumnConverter> implements IGenTableColumnManager {

    /**
     * 根据表名称查询数据库表列信息 | 主库
     *
     * @param tableName 表名称
     * @return 数据库表列信息
     */
    @Master
    @Override
    public List<GenTableColumnDto> selectDbTableColumnsByName(String tableName) {
        return selectDbTableColumnsByName(tableName, null);
    }

    /**
     * 根据表名称查询数据库表列信息 | 租户库
     *
     * @param tableName  表名称
     * @param sourceName 数据源
     * @return 数据库表列信息
     */
    @Isolate
    @Override
    public List<GenTableColumnDto> selectDbTableColumnsByName(String tableName, String sourceName) {
        if (StrUtil.isNotBlank(sourceName)) {
            SecurityContextHolder.setSourceName(sourceName);
        }
        List<GenTableColumnDto> list = baseMapper.selectDbTableColumnsByName(tableName);
        if (StrUtil.isNotBlank(sourceName)) {
            SecurityContextHolder.rollLastSourceName();
        }
        return list;
    }
}
