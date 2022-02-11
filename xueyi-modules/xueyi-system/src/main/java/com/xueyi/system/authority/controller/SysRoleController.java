package com.xueyi.system.authority.controller;

import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.BaseConstants;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.api.authority.domain.dto.SysRoleDto;
import com.xueyi.system.authority.service.ISysRoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/role")
public class SysRoleController extends BaseController<SysRoleDto, ISysRoleService> {

    /** 定义节点名称 */
    protected String getNodeName() {
        return "角色";
    }

    /**
     * 获取角色选择框列表
     */
    @Override
    @GetMapping("/option")
    public AjaxResult option() {
        return super.option();
    }

    /**
     * 前置校验 （强制）增加/修改
     */
    @Override
    protected void baseRefreshValidated(BaseConstants.Operate operate, SysRoleDto role) {
        if (baseService.checkRoleCodeUnique(role.getId(), role.getCode()))
            throw new ServiceException(StrUtil.format("{}{}{}失败，角色编码已存在", operate.getInfo(), getNodeName(), role.getName()));
        else if (baseService.checkNameUnique(role.getId(), role.getName()))
            throw new ServiceException(StrUtil.format("{}{}{}失败，角色名称已存在", operate.getInfo(), getNodeName(), role.getName()));
        else if (baseService.checkRoleKeyUnique(role.getId(), role.getRoleKey()))
            throw new ServiceException(StrUtil.format("{}{}{}失败，角色权限已存在", operate.getInfo(), getNodeName(), role.getName()));
    }
}
