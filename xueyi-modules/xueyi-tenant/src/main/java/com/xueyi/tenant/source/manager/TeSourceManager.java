package com.xueyi.tenant.source.manager;

import com.xueyi.tenant.api.source.domain.dto.TeSourceDto;
import com.xueyi.tenant.source.mapper.TeSourceMapper;
import com.xueyi.common.web.entity.manager.BaseManager;
import org.springframework.stereotype.Component;

/**
 * 数据源管理 数据封装层
 *
 * @author xueyi
 */
@Component
public class TeSourceManager extends BaseManager<TeSourceDto, TeSourceMapper> {
}