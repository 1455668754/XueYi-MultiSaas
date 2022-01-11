package com.xueyi.system.organize.controller;

import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.BaseConstants;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.organize.service.ISysDeptService;
import com.xueyi.system.organize.service.ISysPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 岗位管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/post")
public class SysPostController extends BaseController<SysPostDto, ISysPostService> {

    @Autowired
    private ISysDeptService deptService;

    /** 定义节点名称 */
    protected String getNodeName() {
        return "岗位";
    }

    /** 定义父数据名称 */
    protected String getParentName() {
        return "部门";
    }

//    /**
//     * 修改岗位-角色关系
//     */
//    @RequiresPermissions("system:role:set")
//    @Log(title = "岗位管理", businessType = BusinessType.UPDATE)
//    @PutMapping("/changePostRole")
//    public AjaxResult editPostRole(@Validated @RequestBody SysPost post) {
//        return toAjax(postService.updatePostRole(post));
//    }

    /**
     * 获取下拉树列表
     */
    @GetMapping("/treeSelect")
    public AjaxResult treeSelect() {
        return AjaxResult.success(baseService.buildDeptPostTreeSelect());
    }

    /**
     * 前置校验 （强制）增加/修改
     */
    @Override
    protected void baseRefreshValidated(BaseConstants.Operate operate, SysPostDto post) {
        if (baseService.checkPostCodeUnique(post.getId(), post.getCode()))
            throw new ServiceException(StrUtil.format("{}{}{}失败，岗位编码已存在", operate.getInfo(), getNodeName(), post.getName()));
        else if (baseService.checkNameUnique(post.getId(), post.getName()))
            throw new ServiceException(StrUtil.format("{}{}{}失败，岗位名称已存在", operate.getInfo(), getNodeName(), post.getName()));
        if (BaseConstants.Status.DISABLE == deptService.checkStatus(post.getId()))
            post.setStatus(BaseConstants.Status.DISABLE.getCode());
    }

    /**
     * 前置校验 （强制）修改状态
     *
     * @param post 部门对象
     */
    @Override
    protected void baseEditStatusValidated(BaseConstants.Operate operate, SysPostDto post) {
        if (StringUtils.equals(BaseConstants.Status.NORMAL.getCode(), post.getStatus())) {
            SysPostDto original = baseService.selectById(post.getId());
            if (BaseConstants.Status.DISABLE == deptService.checkStatus(original.getDeptId()))
                AjaxResult.error(StrUtil.format("启用失败，该{}归属的{}已被禁用！", getNodeName(), getParentName()));
        }
    }

}
