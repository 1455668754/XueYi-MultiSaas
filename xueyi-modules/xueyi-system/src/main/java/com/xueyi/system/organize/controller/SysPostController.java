package com.xueyi.system.organize.controller;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.result.R;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.common.security.annotation.Logical;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.common.security.auth.Auth;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.api.organize.domain.query.SysPostQuery;
import com.xueyi.system.organize.service.ISysDeptService;
import com.xueyi.system.organize.service.ISysOrganizeService;
import com.xueyi.system.organize.service.ISysPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

/**
 * 岗位管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/post")
public class SysPostController extends BaseController<SysPostQuery, SysPostDto, ISysPostService> {

    @Autowired
    private ISysOrganizeService organizeService;

    @Autowired
    private ISysDeptService deptService;

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "岗位";
    }

    /** 定义父数据名称 */
    protected String getParentName() {
        return "部门";
    }

    /**
     * 新增岗位 | 内部调用
     */
    @InnerAuth
    @PostMapping("/inner/add")
    public R<SysPostDto> addInner(@RequestBody SysPostDto post) {
        return baseService.addInner(post) > 0 ? R.ok(post) : R.fail();
    }

    /**
     * 查询岗位列表
     */
    @Override
    @GetMapping("/list")
    @RequiresPermissions(Auth.SYS_POST_LIST)
    public AjaxResult list(SysPostQuery post) {
        return super.list(post);
    }

    /**
     * 查询岗位详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @RequiresPermissions(Auth.SYS_POST_SINGLE)
    public AjaxResult getInfo(@PathVariable Serializable id) {
        return super.getInfo(id);
    }

    /**
     * 查询部门关联的角色Id集
     */
    @GetMapping(value = "/auth/{id}")
    @RequiresPermissions(Auth.SYS_POST_AUTH)
    public AjaxResult getRoleAuth(@PathVariable Long id) {
        return success(organizeService.selectPostRoleMerge(id));
    }

    /**
     * 岗位导出
     */
    @Override
    @PostMapping("/export")
    @RequiresPermissions(Auth.SYS_POST_EXPORT)
    @Log(title = "岗位管理", businessType = BusinessType.EXPORT)
    public void export(HttpServletResponse response, SysPostQuery post) {
        super.export(response, post);
    }

    /**
     * 岗位新增
     */
    @Override
    @PostMapping
    @RequiresPermissions(Auth.SYS_POST_ADD)
    @Log(title = "岗位管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated({V_A.class}) @RequestBody SysPostDto post) {
        return super.add(post);
    }

    /**
     * 岗位修改
     */
    @Override
    @PutMapping
    @RequiresPermissions(Auth.SYS_POST_EDIT)
    @Log(title = "岗位管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated({V_E.class}) @RequestBody SysPostDto post) {
        return super.edit(post);
    }

    /**
     * 查询岗位关联的角色Id集
     */
    @PutMapping(value = "/auth")
    @RequiresPermissions(Auth.SYS_POST_AUTH)
    public AjaxResult editRoleAuth(@RequestBody SysPostDto post) {
        organizeService.editPostIdRoleMerge(post.getId(), post.getRoleIds());
        return success();
    }

    /**
     * 岗位修改状态
     */
    @Override
    @PutMapping("/status")
    @RequiresPermissions(value = {Auth.SYS_POST_EDIT, Auth.SYS_POST_ES}, logical = Logical.OR)
    @Log(title = "岗位管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@RequestBody SysPostDto post) {
        return super.editStatus(post);
    }

    /**
     * 岗位批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @RequiresPermissions(Auth.SYS_POST_DEL)
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

    /**
     * 前置校验 （强制）增加/修改
     */
    @Override
    protected void AEHandle(BaseConstants.Operate operate, SysPostDto post) {
        switch (operate) {
            case EDIT_STATUS -> {
                if (StrUtil.equals(BaseConstants.Status.NORMAL.getCode(), post.getStatus())) {
                    SysPostDto original = baseService.selectById(post.getId());
                    if (BaseConstants.Status.DISABLE == deptService.checkStatus(original.getDeptId()))
                        warn(StrUtil.format("启用失败，该{}归属的{}已被禁用！", getNodeName(), getParentName()));
                }
            }
            case ADD, EDIT -> {
                if (baseService.checkPostCodeUnique(post.getId(), post.getCode()))
                    warn(StrUtil.format("{}{}{}失败，岗位编码已存在", operate.getInfo(), getNodeName(), post.getName()));
                else if (baseService.checkNameUnique(post.getId(), post.getName()))
                    warn(StrUtil.format("{}{}{}失败，岗位名称已存在", operate.getInfo(), getNodeName(), post.getName()));
                if (BaseConstants.Status.DISABLE == deptService.checkStatus(post.getId()))
                    post.setStatus(BaseConstants.Status.DISABLE.getCode());
            }
        }
    }

}
