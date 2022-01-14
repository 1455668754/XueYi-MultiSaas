package com.xueyi.tenant.source.controller;

import com.xueyi.tenant.api.source.domain.dto.TeSourceDto;
import com.xueyi.tenant.source.service.ITeSourceService;
import com.xueyi.common.web.entity.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据源管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/source")
public class TeSourceController extends BaseController<TeSourceDto, ITeSourceService> {

    /** 定义节点名称 */
    protected String getNodeName() {
        return "数据源";
    }
}