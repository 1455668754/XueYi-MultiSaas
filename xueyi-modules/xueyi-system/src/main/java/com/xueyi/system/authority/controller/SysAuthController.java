package com.xueyi.system.authority.controller;

import com.xueyi.common.core.utils.TreeUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.result.R;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.common.web.entity.controller.BasisController;
import com.xueyi.system.authority.domain.vo.SysAuthTree;
import com.xueyi.system.authority.service.ISysAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/auth")
public class SysAuthController extends BasisController {

    @Autowired
    private ISysAuthService authService;

    /**
     * 获取租户权限 | 叶子节点 | 内部调用
     */
    @InnerAuth
    @GetMapping("/inner/getTenantAuth")
    public R<Long[]> getTenantAuthInner() {
        List<SysAuthTree> leafNodes = TreeUtil.getLeafNodes(TreeUtil.buildTree(authService.selectTenantAuth()));
        return R.ok(leafNodes.stream().map(SysAuthTree::getId).toArray(Long[]::new));
    }

    /**
     * 新增租户权限 | 内部调用
     */
    @InnerAuth
    @PostMapping("/inner/addTenantAuth")
    public R<Boolean> addTenantAuthInner(@RequestBody Long[] authIds) {
        authService.addTenantAuth(authIds);
        return R.ok();
    }

    /**
     * 修改租户权限 | 内部调用
     */
    @InnerAuth
    @PostMapping("/inner/editTenantAuth")
    public R<Boolean> editTenantAuthInner(@RequestBody Long[] authIds) {
        authService.editTenantAuth(authIds);
        return R.ok();
    }

    /**
     * 获取公共模块|菜单权限树
     */
    @GetMapping(value = "/tenant/authScope")
    @PreAuthorize("@ss.hasAnyAuthority(@Auth.TE_TENANT_ADD, @Auth.TE_TENANT_AUTH)")
    public AjaxResult getCommonAuthScope() {
        return success(TreeUtil.buildTree(authService.selectCommonAuthScope()));
    }

    /**
     * 获取企业模块|菜单权限树
     */
    @GetMapping(value = "/enterprise/authScope")
    @PreAuthorize("@ss.hasAnyAuthority(@Auth.SYS_ROLE_ADD, @Auth.SYS_ROLE_AUTH)")
    public AjaxResult getEnterpriseAuthScope() {
        return success(TreeUtil.buildTree(authService.selectEnterpriseAuthScope()));
    }

}
