package com.xueyi.system.authority.controller.base;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.api.authority.domain.dto.SysRoleDto;
import com.xueyi.system.api.authority.domain.query.SysRoleQuery;
import com.xueyi.system.authority.service.ISysRoleService;

/**
 * 系统服务 | 权限模块 | 角色管理 | 通用 业务处理
 *
 * @author xueyi
 */
public class BSysRoleController extends BaseController<SysRoleQuery, SysRoleDto, ISysRoleService> {

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "角色";
    }

    /**
     * 前置校验 （强制）增加/修改
     */
    @Override
    protected void AEHandle(BaseConstants.Operate operate, SysRoleDto role) {
        if (baseService.checkNameUnique(role.getId(), role.getName()))
            warn(StrUtil.format("{}{}{}失败，角色名称已存在", operate.getInfo(), getNodeName(), role.getName()));
        // 修改禁止操作权限范围
        if (operate.isEdit())
            role.setDataScope(null);
    }
}
