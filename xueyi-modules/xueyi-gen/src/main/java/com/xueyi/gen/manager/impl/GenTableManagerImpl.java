package com.xueyi.gen.manager.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.context.SecurityContextHolder;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.web.entity.manager.impl.BaseManagerImpl;
import com.xueyi.gen.domain.dto.GenTableDto;
import com.xueyi.gen.domain.model.GenTableConverter;
import com.xueyi.gen.domain.po.GenTablePo;
import com.xueyi.gen.domain.query.GenTableQuery;
import com.xueyi.gen.manager.IGenTableManager;
import com.xueyi.gen.mapper.GenTableMapper;
import org.springframework.stereotype.Component;

import java.io.Serializable;
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
    @Override
    public List<GenTableDto> selectDbTableList(GenTableQuery table) {
        if (StrUtil.isNotBlank(table.getSourceName())) {
            SecurityContextHolder.setSourceName(table.getSourceName());
        }
        List<GenTableDto> list = baseMapper.selectDbTableList(table);
        SecurityContextHolder.rollLastSourceName();
        return list;
    }

    /**
     * 根据表名称组查询数据库列表
     *
     * @param names      表名称组
     * @param sourceName 数据源
     * @return 数据库表集合
     */
    @Override
    public List<GenTableDto> selectDbTableListByNames(String[] names, String sourceName) {
        if (StrUtil.isNotBlank(sourceName)) {
            SecurityContextHolder.setSourceName(sourceName);
        }
        List<GenTableDto> list = baseMapper.selectDbTableListByNames(names);
        SecurityContextHolder.rollLastSourceName();
        return list;
    }

    /**
     * 根据表名称查询数据库列表
     *
     * @param name 表名称
     * @return 数据库表
     */
    @Override
    public GenTableDto selectDbTableByName(String name) {
        return baseMapper.selectDbTableByName(name);
    }

    /**
     * 修改其它生成选项
     *
     * @param id      Id
     * @param options 其它生成选项
     * @return 结果
     */
    @Override
    public int updateOptions(Serializable id, String options) {
        return baseMapper.update(new GenTableDto(),
                Wrappers.<GenTablePo>update().lambda()
                        .set(GenTablePo::getOptions, options)
                        .eq(GenTablePo::getId, id));
    }

}
