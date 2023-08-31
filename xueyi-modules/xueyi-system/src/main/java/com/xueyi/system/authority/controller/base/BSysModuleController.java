package com.xueyi.system.authority.controller.base;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.context.SecurityContextHolder;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.security.utils.SecurityUserUtils;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.api.authority.domain.query.SysModuleQuery;
import com.xueyi.system.authority.service.ISysModuleService;

/**
 * 系统服务 | 权限模块 | 模块管理 | 通用 业务处理
 *
 * @author xueyi
 */
public class BSysModuleController extends BaseController<SysModuleQuery, SysModuleDto, ISysModuleService> {

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "模块";
    }

    /**
     * 前置校验 新增/修改
     */
    @Override
    protected void AEHandle(BaseConstants.Operate operate, SysModuleDto module) {
        SecurityContextHolder.setTenantIgnore();
        boolean isNotUnique = baseService.checkNameUnique(module.getId(), module.getName());
        SecurityContextHolder.clearTenantIgnore();
        if (isNotUnique) {
            warn(StrUtil.format("{}{}{}失败，{}名称已存在！", operate.getInfo(), getNodeName(), module.getName(), getNodeName()));
        }
        if (module.isCommon() && SecurityUserUtils.isNotAdminTenant()) {
            warn(StrUtil.format("{}{}{}失败，无操作权限！", operate.getInfo(), getNodeName(), module.getName()));
        }
    }
}
