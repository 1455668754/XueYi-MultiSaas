package com.xueyi.system.organize.controller.base;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.web.entity.controller.TreeController;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.query.SysDeptQuery;
import com.xueyi.system.organize.service.ISysDeptService;

/**
 * 系统服务 | 组织模块 | 部门管理 | 通用 业务处理
 *
 * @author xueyi
 */
public class BSysDeptController extends TreeController<SysDeptQuery, SysDeptDto, ISysDeptService> {

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "部门";
    }

    /**
     * 前置校验 增加/修改
     */
    @Override
    protected void AEHandle(BaseConstants.Operate operate, SysDeptDto dept) {
        if (baseService.checkNameUnique(dept.getId(), dept.getParentId(), dept.getName()))
            warn(StrUtil.format("{}{}{}失败，部门名称已存在", operate.getInfo(), getNodeName(), dept.getName()));
    }
}
