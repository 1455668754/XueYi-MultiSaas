package com.xueyi.system.dict.controller;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.result.R;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.api.dict.domain.dto.SysConfigDto;
import com.xueyi.system.api.dict.domain.query.SysConfigQuery;
import com.xueyi.system.dict.service.ISysConfigService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 参数配置管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/config")
public class SysConfigController extends BaseController<SysConfigQuery, SysConfigDto, ISysConfigService> {

    /** 定义节点名称 */
    protected String getNodeName() {
        return "参数";
    }

    /**
     * 刷新参数缓存 | 内部调用
     */
    @Override
    @InnerAuth(isAnonymous = true)
    @GetMapping("/inner/refresh")
    @Log(title = "参数管理", businessType = BusinessType.REFRESH)
    public R<Boolean> refreshCacheInner() {
        return super.refreshCacheInner();
    }

    /**
     * 刷新参数缓存
     */
    @Override
    @GetMapping("/refresh")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_CONFIG_EDIT)")
    @Log(title = "参数管理", businessType = BusinessType.REFRESH)
    public AjaxResult refreshCache() {
        return super.refreshCache();
    }

    /**
     * 查询参数值
     */
    @GetMapping(value = "/innerCode/{configCode}")
    public R<String> getCode(@PathVariable String configCode) {
        return R.ok(baseService.selectConfigByCode(configCode));
    }

    /**
     * 查询参数值
     */
    @GetMapping(value = "/code/{configCode}")
    public AjaxResult getConfigCode(@PathVariable String configCode) {
        return success(baseService.selectConfigByCode(configCode));
    }

    /**
     * 查询参数列表
     */
    @Override
    @GetMapping("/list")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_CONFIG_LIST)")
    public AjaxResult list(SysConfigQuery config) {
        return super.list(config);
    }

    /**
     * 查询参数详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_CONFIG_SINGLE)")
    public AjaxResult getInfo(@PathVariable Serializable id) {
        return super.getInfo(id);
    }

    /**
     * 参数导出
     */
    @Override
    @PostMapping("/export")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_CONFIG_EXPORT)")
    @Log(title = "参数管理", businessType = BusinessType.EXPORT)
    public void export(HttpServletResponse response, SysConfigQuery config) {
        super.export(response, config);
    }

    /**
     * 参数新增
     */
    @Override
    @PostMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_CONFIG_ADD)")
    @Log(title = "参数管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated({V_A.class}) @RequestBody SysConfigDto config) {
        return super.add(config);
    }

    /**
     * 参数修改
     */
    @Override
    @PutMapping
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_CONFIG_EDIT)")
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated({V_E.class}) @RequestBody SysConfigDto config) {
        return super.edit(config);
    }

    /**
     * 参数修改状态
     */
    @Override
    @PutMapping("/status")
    @PreAuthorize("@ss.hasAnyAuthority(@Auth.SYS_CONFIG_EDIT, @Auth.SYS_CONFIG_ES)")
    @Log(title = "参数管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@RequestBody SysConfigDto config) {
        return super.editStatus(config);
    }

    /**
     * 参数批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_CONFIG_DEL)")
    @Log(title = "参数管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }

    /**
     * 参数强制批量删除
     */
    @Override
    @DeleteMapping("/batch/force/{idList}")
    @PreAuthorize("@ss.hasAuthority(@Auth.SYS_CONFIG_DEL_FORCE)")
    @Log(title = "参数管理", businessType = BusinessType.DELETE_FORCE)
    public AjaxResult batchRemoveForce(@PathVariable List<Long> idList) {
        return super.batchRemoveForce(idList);
    }

    /**
     * 前置校验 （强制）增加/修改
     */
    @Override
    protected void AEHandle(BaseConstants.Operate operate, SysConfigDto config) {
        if (baseService.checkConfigCodeUnique(config.getId(), config.getCode()))
            warn(StrUtil.format("{}{}{}失败，参数编码已存在", operate.getInfo(), getNodeName(), config.getName()));
    }

    /**
     * 前置校验 （强制）删除
     *
     * @param idList Id集合
     */
    @Override
    protected void RHandle(BaseConstants.Operate operate, List<Long> idList) {
        if (operate.isDelete()) {
            int size = idList.size();
            // remove oneself or admin
            for (int i = idList.size() - 1; i >= 0; i--) {
                if (baseService.checkIsBuiltIn(idList.get(i)))
                    idList.remove(i);
            }
            if (CollUtil.isEmpty(idList)) {
                warn(StrUtil.format("{}失败，不能删除内置参数！", operate.getInfo()));
            } else if (idList.size() != size) {
                baseService.deleteByIds(idList);
                warn(StrUtil.format("成功{}除内置参数外的所有参数！", operate.getInfo()));
            }
        }
    }
}
