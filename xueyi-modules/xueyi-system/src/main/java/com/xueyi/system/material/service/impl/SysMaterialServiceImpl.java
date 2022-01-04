package com.xueyi.system.material.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.api.domain.material.dto.SysMaterialDto;
import com.xueyi.system.material.manager.SysMaterialManager;
import com.xueyi.system.material.mapper.SysMaterialMapper;
import com.xueyi.system.material.service.ISysMaterialService;
import org.springframework.stereotype.Service;

/**
 * 素材管理 服务层处理
 *
 * @author xueyi
 */
@Service
@DS("#isolate")
public class SysMaterialServiceImpl extends BaseServiceImpl<SysMaterialDto, SysMaterialManager, SysMaterialMapper> implements ISysMaterialService {
}
