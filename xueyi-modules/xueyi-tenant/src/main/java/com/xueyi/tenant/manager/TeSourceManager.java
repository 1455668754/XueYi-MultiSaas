package com.xueyi.tenant.manager;

import com.xueyi.common.web.entity.manager.BaseManager;
import com.xueyi.tenant.api.domain.source.dto.TeSourceDto;
import com.xueyi.tenant.mapper.TeSourceMapper;
import org.springframework.stereotype.Component;

/**
 * 数据源管理 数据封装层
 *
 * @author xueyi
 */
@Component
public class TeSourceManager extends BaseManager<TeSourceDto, TeSourceMapper> {
}
