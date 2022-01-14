package com.xueyi.tenant.source.service.impl;

import com.xueyi.tenant.api.source.domain.dto.TeSourceDto;
import com.xueyi.tenant.source.service.ITeSourceService;
import com.xueyi.tenant.source.manager.TeSourceManager;
import com.xueyi.tenant.source.mapper.TeSourceMapper;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.stereotype.Service;

import static com.xueyi.common.core.constant.TenantConstants.MASTER;

/**
 * 数据源管理 服务层处理
 *
 * @author xueyi
 */
@Service
@DS(MASTER)
public class TeSourceServiceImpl extends BaseServiceImpl<TeSourceDto, TeSourceManager, TeSourceMapper> implements ITeSourceService {
}