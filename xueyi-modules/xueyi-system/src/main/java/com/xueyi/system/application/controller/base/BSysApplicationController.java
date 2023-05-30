package com.xueyi.system.application.controller.base;

import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.api.application.domain.dto.SysApplicationDto;
import com.xueyi.system.api.application.domain.query.SysApplicationQuery;
import com.xueyi.system.application.service.ISysApplicationService;

/**
 * 系统服务 | 应用模块 | 应用管理 | 通用 业务处理
 *
 * @author xueyi
 */
public class BSysApplicationController extends BaseController<SysApplicationQuery, SysApplicationDto, ISysApplicationService> {

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "应用";
    }

}
