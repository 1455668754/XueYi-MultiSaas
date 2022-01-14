package com.xueyi.tenant.tenant.controller;

import com.xueyi.tenant.tenant.domain.dto.TeTenantDto;
import com.xueyi.tenant.tenant.service.ITeTenantService;
import com.xueyi.common.web.entity.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 租户管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/tenant")
public class TeTenantController extends BaseController<TeTenantDto, ITeTenantService> {

    /** 定义节点名称 */
    protected String getNodeName() {
        return "租户";
    }
}