package com.xueyi.system.authority.controller.inner;

import com.xueyi.common.core.utils.TreeUtil;
import com.xueyi.common.core.web.result.R;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.common.web.entity.controller.BasisController;
import com.xueyi.system.authority.domain.vo.SysAuthTree;
import com.xueyi.system.authority.service.ISysAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统服务 | 权限模块 | 权限管理 | 内部调用 业务处理
 *
 * @author xueyi
 */
@InnerAuth
@RestController
@RequestMapping("/inner/auth")
public class ASysAuthController extends BasisController {

    @Autowired
    private ISysAuthService authService;

    /**
     * 获取租户权限 | 叶子节点
     */
    @GetMapping("/getTenantAuth")
    public R<Long[]> getTenantAuthInner() {
        List<SysAuthTree> leafNodes = TreeUtil.getLeafNodes(TreeUtil.buildTree(authService.selectTenantAuth()));
        return R.ok(leafNodes.stream().map(SysAuthTree::getId).toArray(Long[]::new));
    }

    /**
     * 新增租户权限
     */
    @PostMapping("/addTenantAuth")
    public R<Boolean> addTenantAuthInner(@RequestBody Long[] authIds) {
        authService.addTenantAuth(authIds);
        return R.ok();
    }

    /**
     * 修改租户权限
     */
    @PostMapping("/editTenantAuth")
    public R<Boolean> editTenantAuthInner(@RequestBody Long[] authIds) {
        authService.editTenantAuth(authIds);
        return R.ok();
    }
}
