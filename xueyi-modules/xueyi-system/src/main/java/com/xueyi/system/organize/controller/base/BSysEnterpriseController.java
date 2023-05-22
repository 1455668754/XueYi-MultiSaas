package com.xueyi.system.organize.controller.base;

import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import com.xueyi.system.api.organize.domain.query.SysEnterpriseQuery;
import com.xueyi.system.organize.service.ISysEnterpriseService;

/**
 * 企业管理 | 通用 业务处理
 *
 * @author xueyi
 */
public class BSysEnterpriseController extends BaseController<SysEnterpriseQuery, SysEnterpriseDto, ISysEnterpriseService> {

    /** 定义节点名称 */
    protected String getNodeName() {
        return "企业";
    }

}
