package com.xueyi.system.source.manager.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.system.api.source.domain.Source;
import com.xueyi.system.source.manager.ISysSourceManager;
import com.xueyi.system.source.mapper.SysSourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * 策略组管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class SysSourceManager implements ISysSourceManager {

    @Autowired
    private SysSourceMapper baseMapper;

    /**
     * 查询源策略组列表
     *
     * @return 策略组集合
     */
    @Override
    public List<Source> selectSourceList() {
        return baseMapper.selectList(
                Wrappers.<Source>query().lambda()
                        .eq(Source::getStatus, BaseConstants.Status.NORMAL.getCode()));
    }

    /**
     * 根据Id查询源策略组
     *
     * @param id 源策略Id
     * @return 源策略组
     */
    @Override
    public Source selectById(Serializable id) {
        return baseMapper.selectOne(Wrappers.<Source>query().lambda()
                .eq(Source::getId, id)
                .eq(Source::getStatus, BaseConstants.Status.NORMAL.getCode()));
    }
}
