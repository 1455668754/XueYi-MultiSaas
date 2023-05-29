package com.xueyi.system.organize.controller.admin;

import com.xueyi.common.core.utils.TreeUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.web.entity.controller.BasisController;
import com.xueyi.system.organize.service.ISysOrganizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统服务 | 组织模块 | 组织管理 | 管理端 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/admin/organize")
public class ASysOrganizeController extends BasisController {

    @Autowired
    private ISysOrganizeService organizeService;

    /**
     * 获取企业部门|岗位树
     */
    @GetMapping(value = "/organizeScope")
    @PreAuthorize("@ss.hasAnyAuthority(@Auth.SYS_ROLE_ADD, @Auth.SYS_ROLE_AUTH)")
    public AjaxResult getOrganizeScope() {
        return success(TreeUtil.buildTree(organizeService.selectOrganizeScope()));
    }

    /**
     * 获取下拉树列表
     */
    @GetMapping("/option")
    public AjaxResult option() {
        return success(organizeService.selectOrganizeTreeExDeptNode());
    }
}
