package com.xueyi.system.authority.controller.base;

import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.authority.domain.dto.SysAuthGroupDto;
import com.xueyi.system.authority.domain.query.SysAuthGroupQuery;
import com.xueyi.system.authority.service.ISysAuthGroupService;

/**
 * 系统服务 | 权限模块 | 企业权限组管理 | 通用 业务处理
 *
 * @author xueyi
 */
public class BSysAuthGroupController extends BaseController<SysAuthGroupQuery, SysAuthGroupDto, ISysAuthGroupService> {

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "权限组";
    }
}
