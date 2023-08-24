package com.xueyi.system.authority.controller.admin;

import com.xueyi.common.core.constant.basic.DictConstants;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.AdminAuth;
import com.xueyi.common.security.service.TokenUserService;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.api.authority.domain.query.SysModuleQuery;
import com.xueyi.system.api.model.DataScope;
import com.xueyi.system.authority.controller.base.BSysModuleController;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.stream.Collectors;

/**
 * 系统服务 | 权限模块 | 模块管理 | 管理端 业务处理
 *
 * @author xueyi
 */
@AdminAuth
@RestController
@RequestMapping("/admin/module")
public class ASysModuleController extends BSysModuleController {

    @Autowired
    private TokenUserService tokenService;

    /**
     * 查询首页可展示模块信息列表
     */
    @GetMapping("/getRouters")
    public AjaxResult getRoutes() {
        Object moduleRoute = tokenService.getModuleRoute();
        if (ObjectUtil.isNull(moduleRoute)) {
            DataScope dataScope = tokenService.getDataScope();
            List<SysModuleDto> moduleList = baseService.selectListByIds(dataScope.getModuleIds());
            moduleRoute = moduleList.stream().filter(item -> ObjectUtil.equals(DictConstants.DicShowHide.SHOW.getCode(), item.getHideModule()))
                    .collect(Collectors.toList());
            tokenService.setModuleRoute(moduleRoute);
        }
        return success(moduleRoute);
    }

    /**
     * 查询模块列表
     */
    @Override
    @GetMapping("/list")
    @PreAuthorize("@ss.hasAnyAuthority(@Auth.SYS_MODULE_LIST, @Auth.GEN_GENERATE_LIST, @Auth.SYS_MENU_LIST)")
    public AjaxResult list(SysModuleQuery module) {
        return super.list(module);
    }

    /**
     * 查询模块详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_MODULE_SINGLE)")
    public AjaxResult getInfo(@PathVariable Serializable id) {
        return super.getInfo(id);
    }

    /**
     * 模块新增
     */
    @Override
    @PostMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_MODULE_ADD)")
    @Log(title = "模块管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated({V_A.class}) @RequestBody SysModuleDto module) {
        return super.add(module);
    }

    /**
     * 模块修改
     */
    @Override
    @PutMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_MODULE_EDIT)")
    @Log(title = "模块管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated({V_E.class}) @RequestBody SysModuleDto module) {
        return super.edit(module);
    }

    /**
     * 模块修改状态
     */
    @Override
    @PutMapping("/status")
    @PreAuthorize("@ss.hasAnyAuthority(@Auth.SYS_MODULE_EDIT, @Auth.SYS_MODULE_ES)")
    @Log(title = "模块管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@RequestBody SysModuleDto module) {
        return super.editStatus(module);
    }

    /**
     * 模块批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_MODULE_DEL)")
    @Log(title = "模块管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }

}
