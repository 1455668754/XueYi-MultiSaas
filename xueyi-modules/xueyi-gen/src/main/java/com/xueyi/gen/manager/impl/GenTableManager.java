package com.xueyi.gen.manager.impl;

import com.xueyi.common.core.utils.core.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.web.entity.manager.impl.SubBaseManager;
import com.xueyi.gen.domain.dto.GenTableColumnDto;
import com.xueyi.gen.domain.dto.GenTableDto;
import com.xueyi.gen.domain.model.GenTableColumnConverter;
import com.xueyi.gen.domain.model.GenTableConverter;
import com.xueyi.gen.domain.po.GenTableColumnPo;
import com.xueyi.gen.domain.po.GenTablePo;
import com.xueyi.gen.domain.query.GenTableColumnQuery;
import com.xueyi.gen.domain.query.GenTableQuery;
import com.xueyi.gen.manager.IGenTableManager;
import com.xueyi.gen.mapper.GenTableColumnMapper;
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
public class GenTableManager extends SubBaseManager<GenTableQuery, GenTableDto, GenTablePo, GenTableMapper, GenTableConverter, GenTableColumnQuery, GenTableColumnDto, GenTableColumnPo, GenTableColumnMapper, GenTableColumnConverter> implements IGenTableManager {

    /**
     * 查询数据库列表
     *
     * @param genTableDto 业务对象
     * @return 数据库表集合
     */
    @Override
    public List<GenTableDto> selectDbTableList(GenTableDto genTableDto) {
        return baseMapper.selectDbTableList(genTableDto);
    }

    /**
     * 根据表名称组查询数据库列表
     *
     * @param names 表名称组
     * @return 数据库表集合
     */
    @Override
    public List<GenTableDto> selectDbTableListByNames(String[] names) {
        return baseMapper.selectDbTableListByNames(names);
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

    /**
     * 设置主子表中子表外键值
     */
    @Override
    protected void setForeignKey(LambdaQueryWrapper<GenTableColumnPo> queryWrapper, LambdaUpdateWrapper<GenTableColumnPo> updateWrapper, GenTableDto table, Serializable id) {
        Serializable Id = ObjectUtil.isNotNull(table) ? table.getId() : id;
        if (ObjectUtil.isNotNull(queryWrapper))
            queryWrapper.eq(GenTableColumnPo::getTableId, Id);
        else
            updateWrapper.eq(GenTableColumnPo::getTableId, Id);
    }
}
