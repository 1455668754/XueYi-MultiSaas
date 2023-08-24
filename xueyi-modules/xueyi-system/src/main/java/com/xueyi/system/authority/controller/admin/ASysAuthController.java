package com.xueyi.system.authority.controller.admin;

import com.xueyi.common.core.utils.TreeUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.security.annotation.AdminAuth;
import com.xueyi.common.web.entity.controller.BasisController;
import com.xueyi.system.authority.service.ISysAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统服务 | 权限模块 | 权限管理 | 管理端 业务处理
 *
 * @author xueyi
 */
@AdminAuth
@RestController
@RequestMapping("/admin/auth")
public class ASysAuthController extends BasisController {

    @Autowired
    private ISysAuthService authService;

    /**
     * 获取全部公共模块 | 菜单权限树
     */
    @GetMapping(value = "/common/authScope")
    @PreAuthorize("@ss.hasAnyAuthority(@Auth.TE_TENANT_ADD, @Auth.TE_TENANT_AUTH)")
    public AjaxResult getCommonAuthScope() {
        return success(TreeUtil.buildTree(authService.selectCommonAuthScope()));
    }

    /**
     * 获取企业模块 | 菜单权限树
     */
    @GetMapping(value = "/enterprise/authScope")
    @PreAuthorize("@ss.hasAnyAuthority(@Auth.SYS_ROLE_ADD, @Auth.SYS_ROLE_AUTH)")
    public AjaxResult getEnterpriseAuthScope() {
        return success(TreeUtil.buildTree(authService.selectEnterpriseAuthScope()));
    }
}
