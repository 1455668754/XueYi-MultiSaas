package com.xueyi.tenant.controller;

import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.api.source.feign.RemoteSourceService;
import com.xueyi.tenant.api.domain.source.dto.TeStrategyDto;
import com.xueyi.tenant.service.ITeStrategyService;
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
@RequestMapping("/strategy")
public class TeStrategyController extends BaseController<TeStrategyDto, ITeStrategyService> {

    @Autowired
    private ITeStrategyService tenantStrategyService;

    @Autowired
    private RemoteSourceService remoteSourceService;

    /** 定义节点名称 */
    protected String getNodeName() {
        return "策略";
    }

//    /**
//     * 查询数据源策略列表
//     */
//    @RequiresPermissions("tenant:strategy:list")
//    @GetMapping("/list")
//    public AjaxResult list(TeStrategyDto teStrategyDto) {
//        startPage();
//        List<TeStrategyDto> list = tenantStrategyService.mainSelectStrategyList(teStrategyDto);
//        return getDataTable(list);
//    }
//
//    /**
//     * 查询数据源策略列表（排除停用）
//     */
//    @GetMapping("/exclude")
//    public AjaxResult exclude(TeStrategyDto teStrategyDto) {
//        return AjaxResult.success(tenantStrategyService.mainSelectStrategyListExclude(teStrategyDto));
//    }
//
//    /**
//     * 获取数据源策略详细信息
//     */
//    @RequiresPermissions("tenant:strategy:query")
//    @GetMapping(value = "/byId")
//    public AjaxResult getInfo(TeStrategyDto teStrategyDto) {
//        return AjaxResult.success(tenantStrategyService.mainSelectStrategyById(teStrategyDto));
//    }
//
//    /**
//     * 新增数据源策略
//     */
//    @RequiresPermissions("tenant:strategy:add")
//    @Log(title = "数据源策略", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody TeStrategyDto teStrategyDto) {
//        int rows = tenantStrategyService.mainInsertStrategy(teStrategyDto);
//        if (rows > 0 && StringUtils.equals(BaseConstants.Status.NORMAL.getCode(), teStrategyDto.getStatus())) {
//            remoteSourceService.refreshSourceCache(teStrategyDto.getStrategyId(), SecurityConstants.INNER);
//        }
//        return toAjax(rows);
//    }
//
//    /**
//     * 修改数据源策略
//     */
//    @RequiresPermissions("tenant:strategy:edit")
//    @Log(title = "数据源策略", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody TeStrategyDto teStrategyDto) {
//        if (StringUtils.equals(BaseConstants.Default.YES.getCode(), teStrategyDto.getIsChange())) {
//            return AjaxResult.error("禁止操作默认策略");
//        }
//        int rows = tenantStrategyService.mainUpdateStrategy(teStrategyDto);
//        remoteSourceService.refreshSourceCache(teStrategyDto.getStrategyId(), SecurityConstants.INNER);
//        return toAjax(rows);
//    }
//
//    /**
//     * 修改数据源策略排序
//     */
//    @RequiresPermissions("tenant:strategy:edit")
//    @Log(title = "数据源策略", businessType = BusinessType.UPDATE)
//    @PutMapping(value = "/sort")
//    public AjaxResult updateSort(@RequestBody TeStrategyDto teStrategyDto) {
//        return toAjax(tenantStrategyService.mainUpdateStrategySort(teStrategyDto));
//    }
//
//    /**
//     * 删除数据源策略
//     */
//    @RequiresPermissions("tenant:strategy:remove")
//    @Log(title = "数据源策略", businessType = BusinessType.DELETE)
//    @DeleteMapping
//    public AjaxResult remove(@RequestBody TeStrategyDto teStrategyDto) {
//        int rows = tenantStrategyService.mainDeleteStrategyByIds(teStrategyDto);
//        SourceUtils.deleteSourceCaches(ParamsUtils.IdsObjectToLongList(teStrategyDto.getParams().get("Ids")));
//        return toAjax(rows);
//    }
}