package com.xueyi.system.authority.controller;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.Logical;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.common.security.auth.Auth;
import com.xueyi.common.security.service.TokenService;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.api.authority.domain.query.SysModuleQuery;
import com.xueyi.system.api.model.DataScope;
import com.xueyi.system.authority.service.ISysModuleService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 模块管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/module")
public class SysModuleController extends BaseController<SysModuleQuery, SysModuleDto, ISysModuleService> {

    @Autowired
    private TokenService tokenService;

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "模块";
    }

    /**
     * 查询首页可展示模块信息列表
     */
    @GetMapping("/getRouters")
    public AjaxResult getRoutes() {
        Object moduleRoute = tokenService.getModuleRoute();
        if (ObjectUtil.isNull(moduleRoute)) {
            DataScope dataScope = tokenService.getDataScope();
            moduleRoute = baseService.getRoutes(dataScope.getRoleIds());
            tokenService.setModuleRoute(moduleRoute);
        }
        return success(moduleRoute);
    }

    /**
     * 查询模块列表
     */
    @Override
    @GetMapping("/list")
    @RequiresPermissions(Auth.SYS_MODULE_LIST)
    public AjaxResult list(SysModuleQuery module) {
        return super.list(module);
    }

    /**
     * 查询模块详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @RequiresPermissions(Auth.SYS_MODULE_SINGLE)
    public AjaxResult getInfo(@PathVariable Serializable id) {
        return super.getInfo(id);
    }

    /**
     * 模块导出
     */
    @Override
    @PostMapping("/export")
    @RequiresPermissions(Auth.SYS_MODULE_EXPORT)
    @Log(title = "模块管理", businessType = BusinessType.EXPORT)
    public void export(HttpServletResponse response, SysModuleQuery module) {
        super.export(response, module);
    }

    /**
     * 模块新增
     */
    @Override
    @PostMapping
    @RequiresPermissions(Auth.SYS_MODULE_ADD)
    @Log(title = "模块管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated({V_A.class}) @RequestBody SysModuleDto module) {
        return super.add(module);
    }

    /**
     * 模块修改
     */
    @Override
    @PutMapping
    @RequiresPermissions(Auth.SYS_MODULE_EDIT)
    @Log(title = "模块管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated({V_E.class}) @RequestBody SysModuleDto module) {
        return super.edit(module);
    }

    /**
     * 模块修改状态
     */
    @Override
    @PutMapping("/status")
    @RequiresPermissions(value = {Auth.SYS_MODULE_EDIT, Auth.SYS_MODULE_ES}, logical = Logical.OR)
    @Log(title = "模块管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@RequestBody SysModuleDto module) {
        return super.editStatus(module);
    }

    /**
     * 模块批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @RequiresPermissions(Auth.SYS_MODULE_DEL)
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
    protected void AEHandle(BaseConstants.Operate operate, SysModuleDto module) {
        if (baseService.checkNameUnique(module.getId(), module.getName()))
            warn(StrUtil.format("{}{}{}失败，{}名称已存在！", operate.getInfo(), getNodeName(), module.getName(), getNodeName()));
        if (operate.isAdd() && SecurityUtils.isNotAdminTenant() && module.isCommon())
            warn(StrUtil.format("{}{}{}失败，无操作权限！", operate.getInfo(), getNodeName(), module.getName()));
        if (operate.isEdit()) {
            SysModuleDto original = baseService.selectById(module.getId());
            if (ObjectUtil.isNull(original))
                warn("数据不存在！");
            if (SecurityUtils.isNotAdminTenant() && original.isCommon())
                warn(StrUtil.format("{}{}{}失败，无操作权限！", operate.getInfo(), getNodeName(), module.getName()));
            if (!StrUtil.equals(module.getIsCommon(), original.getIsCommon()))
                warn(StrUtil.format("{}{}{}失败，公共{}属性禁止变更！", operate.getInfo(), getNodeName(), module.getName(), getNodeName()));
        }
    }
}
