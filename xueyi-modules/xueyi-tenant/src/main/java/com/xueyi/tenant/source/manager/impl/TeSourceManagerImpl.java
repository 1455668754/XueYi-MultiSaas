package com.xueyi.tenant.source.manager.impl;

import com.xueyi.common.web.entity.manager.impl.BaseManagerImpl;
import com.xueyi.tenant.api.source.domain.dto.TeSourceDto;
import com.xueyi.tenant.api.source.domain.po.TeSourcePo;
import com.xueyi.tenant.api.source.domain.query.TeSourceQuery;
import com.xueyi.tenant.source.domain.model.TeSourceConverter;
import com.xueyi.tenant.source.manager.ITeSourceManager;
import com.xueyi.tenant.source.mapper.TeSourceMapper;
import org.springframework.stereotype.Component;

/**
 * 租户服务 | 策略模块 | 数据源管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class TeSourceManagerImpl extends BaseManagerImpl<TeSourceQuery, TeSourceDto, TeSourcePo, TeSourceMapper, TeSourceConverter> implements ITeSourceManager {
}