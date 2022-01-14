package com.xueyi.tenant.tenant.controller;

import com.xueyi.tenant.api.tenant.domain.dto.TeStrategyDto;
import com.xueyi.tenant.tenant.service.ITeStrategyService;
import com.xueyi.common.web.entity.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据源策略管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/strategy")
public class TeStrategyController extends BaseController<TeStrategyDto, ITeStrategyService> {

    /** 定义节点名称 */
    protected String getNodeName() {
        return "数据源策略";
    }
}