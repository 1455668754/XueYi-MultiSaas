package com.xueyi.system.organize.service.impl;

import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import com.xueyi.system.api.organize.domain.query.SysEnterpriseQuery;
import com.xueyi.system.organize.manager.ISysEnterpriseManager;
import com.xueyi.system.organize.service.ISysEnterpriseService;
import org.springframework.stereotype.Service;

/**
 * 系统服务 | 组织模块 | 企业管理 服务层处理
 *
 * @author xueyi
 */
@Service
public class SysEnterpriseServiceImpl extends BaseServiceImpl<SysEnterpriseQuery, SysEnterpriseDto, ISysEnterpriseManager> implements ISysEnterpriseService {

    /**
     * 根据名称查询状态正常企业对象
     *
     * @param name 名称
     * @return 企业对象
     */
    @Override
    public SysEnterpriseDto selectByName(String name) {
        return baseManager.selectByName(name);
    }
}
