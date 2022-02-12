package com.xueyi.system.authority.controller;

import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.BaseConstants;
import com.xueyi.common.core.constant.DictConstants;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.Logical;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.common.security.auth.Auth;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.web.entity.controller.SubBaseController;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.authority.service.ISysMenuService;
import com.xueyi.system.authority.service.ISysModuleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

/**
 * 模块管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/module")
public class SysModuleController extends SubBaseController<SysModuleDto, ISysModuleService, SysMenuDto, ISysMenuService> {

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "模块";
    }

    /** 定义子数据名称 */
    @Override
    protected String getSubName() {
        return "菜单";
    }

//    /**
//     * 查询首页可展示模块信息列表
//     */
//    @GetMapping("/getRoutes")
//    public AjaxResult getRoutes() {
//        return AjaxResult.success(baseService.getRoutes());
//    }

    /**
     * 查询模块列表
     */
    @Override
    @GetMapping("/list")
    @RequiresPermissions(Auth.SYS_MODULE_LIST)
    public AjaxResult listExtra(SysModuleDto module) {
        return super.listExtra(module);
    }

    /**
     * 查询模块详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @RequiresPermissions(Auth.SYS_MODULE_SINGLE)
    public AjaxResult getInfoExtra(@PathVariable Serializable id) {
        return super.getInfoExtra(id);
    }

    /**
     * 模块导出
     */
    @Override
    @PostMapping("/export")
    @RequiresPermissions(Auth.SYS_MODULE_EXPORT)
    public void export(HttpServletResponse response, SysModuleDto module) {
        super.export(response, module);
    }

    /**
     * 模块新增
     */
    @Override
    @PostMapping
    @RequiresPermissions(Auth.SYS_MODULE_ADD)
    @Log(title = "模块管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated @RequestBody SysModuleDto module) {
        return super.add(module);
    }

    /**
     * 模块修改
     */
    @Override
    @PutMapping
    @RequiresPermissions(Auth.SYS_MODULE_EDIT)
    @Log(title = "模块管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated @RequestBody SysModuleDto module) {
        return super.edit(module);
    }

    /**
     * 模块修改状态
     */
    @Override
    @PutMapping("/status")
    @RequiresPermissions(value = {Auth.SYS_MODULE_EDIT, Auth.SYS_MODULE_EDIT_STATUS}, logical = Logical.OR)
    @Log(title = "模块管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@Validated @RequestBody SysModuleDto module) {
        return super.editStatus(module);
    }

    /**
     * 模块批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @RequiresPermissions(Auth.SYS_MODULE_DELETE)
    @Log(title = "模块管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }

    /**
     * 获取模块选择框列表
     */
    @Override
    @GetMapping("/option")
    public AjaxResult option() {
        return super.option();
    }

    /**
     * 新增/修改 前置校验
     */
    @Override
    protected void baseRefreshValidated(BaseConstants.Operate operate, SysModuleDto module) {
        if (StringUtils.equals(DictConstants.DicCommonPrivate.COMMON.getCode(), module.getIsCommon()) && !SecurityUtils.isAdminTenant())
            throw new ServiceException(StrUtil.format("{}{}{}失败，没有操作权限", operate.getInfo(), getNodeName(), module.getName()));
    }

}
