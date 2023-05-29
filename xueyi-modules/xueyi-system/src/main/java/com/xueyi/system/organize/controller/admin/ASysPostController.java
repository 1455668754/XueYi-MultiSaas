package com.xueyi.system.organize.controller.admin;

import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.api.organize.domain.query.SysPostQuery;
import com.xueyi.system.organize.controller.base.BSysPostController;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
 * 系统服务 | 组织模块 | 岗位管理 | 管理端 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/admin/post")
public class ASysPostController extends BSysPostController {

    /**
     * 查询岗位列表
     */
    @Override
    @GetMapping("/list")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_POST_LIST)")
    public AjaxResult list(SysPostQuery post) {
        return super.list(post);
    }

    /**
     * 查询岗位详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_POST_SINGLE)")
    public AjaxResult getInfo(@PathVariable Serializable id) {
        return super.getInfo(id);
    }

    /**
     * 查询部门关联的角色Id集
     */
    @GetMapping(value = "/auth/{id}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_POST_AUTH)")
    public AjaxResult getRoleAuth(@PathVariable Long id) {
        return success(organizeService.selectPostRoleMerge(id));
    }

    /**
     * 岗位导出
     */
    @Override
    @PostMapping("/export")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_POST_EXPORT)")
    @Log(title = "岗位管理", businessType = BusinessType.EXPORT)
    public void export(HttpServletResponse response, SysPostQuery post) {
        super.export(response, post);
    }

    /**
     * 岗位新增
     */
    @Override
    @PostMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_POST_ADD)")
    @Log(title = "岗位管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated({V_A.class}) @RequestBody SysPostDto post) {
        return super.add(post);
    }

    /**
     * 岗位修改
     */
    @Override
    @PutMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_POST_EDIT)")
    @Log(title = "岗位管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated({V_E.class}) @RequestBody SysPostDto post) {
        return super.edit(post);
    }

    /**
     * 查询岗位关联的角色Id集
     */
    @PutMapping(value = "/auth")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_POST_AUTH)")
    public AjaxResult editRoleAuth(@RequestBody SysPostDto post) {
        organizeService.editPostIdRoleMerge(post.getId(), post.getRoleIds());
        return success();
    }

    /**
     * 岗位修改状态
     */
    @Override
    @PutMapping("/status")
    @PreAuthorize("@ss.hasAnyAuthority(@Auth.SYS_POST_EDIT, @Auth.SYS_POST_ES)")
    @Log(title = "岗位管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@RequestBody SysPostDto post) {
        return super.editStatus(post);
    }

    /**
     * 岗位批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_POST_DEL)")
    @Log(title = "岗位管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }

    /**
     * 获取岗位选择框列表
     */
    @Override
    @GetMapping("/option")
    public AjaxResult option() {
        return super.option();
    }

}
