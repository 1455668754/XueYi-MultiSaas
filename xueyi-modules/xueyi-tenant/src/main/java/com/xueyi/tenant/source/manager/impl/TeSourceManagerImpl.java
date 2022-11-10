package com.xueyi.tenant.source.manager.impl;

import cn.hutool.core.util.IdUtil;
import com.xueyi.common.web.entity.manager.impl.BaseManagerImpl;
import com.xueyi.tenant.api.source.domain.dto.TeSourceDto;
import com.xueyi.tenant.api.source.domain.model.TeSourceConverter;
import com.xueyi.tenant.api.source.domain.po.TeSourcePo;
import com.xueyi.tenant.api.source.domain.query.TeSourceQuery;
import com.xueyi.tenant.source.manager.ITeSourceManager;
import com.xueyi.tenant.source.mapper.TeSourceMapper;
import org.springframework.stereotype.Component;

/**
 * 数据源管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class TeSourceManagerImpl extends BaseManagerImpl<TeSourceQuery, TeSourceDto, TeSourcePo, TeSourceMapper, TeSourceConverter> implements ITeSourceManager {

    /**
     * 新增数据源对象
     *
     * @param source 数据源对象
     * @return 结果
     */
    @Override
    public int insert(TeSourceDto source) {
        source.setSlave(IdUtil.simpleUUID());
        return baseMapper.insert(source);
    }
}