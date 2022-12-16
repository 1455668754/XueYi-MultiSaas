package com.xueyi.gen.manager.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.web.entity.domain.SlaveRelation;
import com.xueyi.common.web.entity.manager.impl.BaseManagerImpl;
import com.xueyi.gen.domain.dto.GenTableDto;
import com.xueyi.gen.domain.model.GenTableConverter;
import com.xueyi.gen.domain.po.GenTablePo;
import com.xueyi.gen.domain.query.GenTableQuery;
import com.xueyi.gen.manager.IGenTableManager;
import com.xueyi.gen.mapper.GenTableMapper;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.xueyi.gen.domain.merge.MergeGroup.GEN_TABLE_GROUP;

/**
 * 业务管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class GenTableManagerImpl extends BaseManagerImpl<GenTableQuery, GenTableDto, GenTablePo, GenTableMapper, GenTableConverter> implements IGenTableManager {

    /**
     * 初始化从属关联关系
     *
     * @return 关系对象集合
     */
    protected List<SlaveRelation> subRelationInit() {
        return new ArrayList<>(){{
            add(new SlaveRelation(GEN_TABLE_GROUP, GenTableColumnManagerImpl.class));
        }};
    }

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

}
