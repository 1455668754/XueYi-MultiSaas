package com.xueyi.tenant.controller;

import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.tenant.api.domain.source.dto.TeSourceDto;
import com.xueyi.tenant.service.ITeSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

;
/**
 * 角色管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/source")
public class TeSourceController extends BaseController<TeSourceDto, ITeSourceService> {

    @Autowired
    private ITeSourceService sourceService;

    /** 定义节点名称 */
    protected String getNodeName() {
        return "数据源";
    }

//    /**
//     * 新增数据源
//     */
//    @Override
//    @RequiresPermissions("tenant:source:add")
//    @Log(title = "数据源", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody TeSourceDto teSourceDto) {
//        DSUtils.testDs(teSourceDto);
//        return toAjax(sourceService.mainInsertSource(teSourceDto));
//    }
//
//    /**
//     * 修改数据源
//     */
//    @Override
//    @RequiresPermissions("tenant:source:edit")
//    @Log(title = "数据源", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody TeSourceDto teSourceDto) {
//        DSUtils.testDs(teSourceDto);
//        return toAjax(sourceService.mainUpdateSource(teSourceDto));
//    }
//
//    /**
//     * 修改数据源状态
//     */
//    @RequiresPermissions("tenant:source:edit")
//    @Log(title = "数据源", businessType = BusinessType.UPDATE)
//    @PutMapping("/status")
//    public AjaxResult editStatus(@RequestBody TeSourceDto teSourceDto) {
//        TeSourceDto check = new TeSourceDto(teSourceDto.getSourceId());
//        TeSourceDto oldTeSourceDto = sourceService.mainSelectSourceBySourceId(check);
//        DSUtils.testDs(oldTeSourceDto);
//        if (StringUtils.equals(BaseConstants.Status.NORMAL.getCode(), teSourceDto.getStatus())) {
//            if (StringUtils.equals(TenantConstants.SourceType.WRITE.getCode(), oldTeSourceDto.getType()) && sourceService.mainCheckSeparationSourceByWriteId(check)) {
//                return AjaxResult.error("该数据源未配置读数据源,请先进行读写配置再启用！");
//            }
//        } else if (StringUtils.equals(BaseConstants.Status.DISABLE.getCode(), teSourceDto.getStatus())) {
//            if (StringUtils.equals(TenantConstants.SourceType.READ.getCode(), oldTeSourceDto.getType()) && sourceService.mainCheckSeparationSourceByReadId(check)) {
//                return AjaxResult.error("该数据源已被应用于读写配置,请先从对应读写配置中取消关联后再禁用！");
//            } else if ((StringUtils.equals(TenantConstants.SourceType.READ_WRITE.getCode(), oldTeSourceDto.getType()) || StringUtils.equals(TenantConstants.SourceType.WRITE.getCode(), oldTeSourceDto.getType())) && sourceService.mainCheckStrategySourceBySourceId(check)) {
//                return AjaxResult.error("该数据源已被应用于数据源策略,请先从对应策略中取消关联后再禁用！");
//            }
//        }
//        if (StringUtils.equals(teSourceDto.getStatus(), oldTeSourceDto.getStatus())) {
//            teSourceDto.setSyncType(TenantConstants.SyncType.UNCHANGED.getCode());
//            return AjaxResult.error("无状态调整！");
//        } else {
//            if (StringUtils.equals(BaseConstants.Status.DISABLE.getCode(), teSourceDto.getStatus())) {
//                teSourceDto.setSyncType(TenantConstants.SyncType.DELETE.getCode());
//            } else if (StringUtils.equals(BaseConstants.Status.NORMAL.getCode(), teSourceDto.getStatus())) {
//                teSourceDto.setSyncType(TenantConstants.SyncType.ADD.getCode());
//                teSourceDto.setDriverClassName(oldTeSourceDto.getDriverClassName());
//                teSourceDto.setUrl(oldTeSourceDto.getUrlPrepend().concat(oldTeSourceDto.getUrlAppend()));
//                teSourceDto.setUsername(oldTeSourceDto.getUsername());
//                teSourceDto.setPassword(oldTeSourceDto.getPassword());
//            }
//            teSourceDto.setSlave(oldTeSourceDto.getSlave());
//        }
//        return toAjax(sourceService.mainUpdateSourceStatus(teSourceDto));
//    }
//
//    /**
//     * 修改数据源排序
//     */
//    @RequiresPermissions("tenant:source:edit")
//    @Log(title = "数据源", businessType = BusinessType.UPDATE)
//    @PutMapping(value = "/sort")
//    public AjaxResult updateSort(@RequestBody TeSourceDto teSourceDto) {
//        return toAjax(sourceService.mainUpdateSourceSort(teSourceDto));
//    }
//
//    /**
//     * 删除数据源 | 单个删除
//     */
//    @RequiresPermissions("tenant:source:remove")
//    @Log(title = "数据源", businessType = BusinessType.DELETE)
//    @DeleteMapping
//    public AjaxResult remove(@RequestBody TeSourceDto teSourceDto) {
//        TeSourceDto check = sourceService.mainSelectSourceBySourceId(teSourceDto);
//        if (StringUtils.equals(BaseConstants.Status.NORMAL.getCode(), check.getStatus())) {
//            return AjaxResult.error("请先停用数据源后再删除！");
//        } else if (StringUtils.equals(BaseConstants.Default.YES.getCode(), check.getIsChange())) {
//            return AjaxResult.error("系统默认数据源无法被删除！");
//        }
//        return toAjax(sourceService.mainDeleteSourceById(check));
//    }
}