package com.xueyi.tenant.controller;

import com.xueyi.common.web.entity.controller.BasisController;
import com.xueyi.tenant.service.ITeSeparationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

;

/**
 * 数据源 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/separation")
public class SeparationController extends BasisController {

    @Autowired
    private ITeSeparationService separationService;

//    /**
//     * 查询 只读 数据源集合
//     */
//    @GetMapping("/containRead")
//    public AjaxResult containRead(TeSourceDto teSourceDto) {
//        return AjaxResult.success(separationService.mainSelectContainReadList(teSourceDto));
//    }
//
//    /**
//     * 查询 含写 数据源集合
//     */
//    @GetMapping("/containWrite")
//    public AjaxResult containWrite(TeSourceDto teSourceDto) {
//        return AjaxResult.success(separationService.mainSelectContainWriteList(teSourceDto));
//    }
//
//    /**
//     * 获取数据源及其分离策略详细信息
//     */
//    @RequiresPermissions("tenant:separation:edit")
//    @GetMapping(value = "/byId")
//    public AjaxResult getInfo(TeSourceDto teSourceDto) {
//        return AjaxResult.success(separationService.mainSelectSeparationById(teSourceDto));
//    }
//
//    /**
//     * 修改数据源
//     */
//    @RequiresPermissions("tenant:separation:edit")
//    @Log(title = "数据源读写分离", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody TeSourceDto teSourceDto) {
//        return toAjax(separationService.mainUpdateSeparation(teSourceDto));
//    }
}