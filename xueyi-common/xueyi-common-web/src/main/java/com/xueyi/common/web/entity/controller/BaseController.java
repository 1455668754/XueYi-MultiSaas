package com.xueyi.common.web.entity.controller;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.utils.poi.ExcelUtil;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.result.R;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.web.entity.controller.handle.BaseHandleController;
import com.xueyi.common.web.entity.service.IBaseService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

/**
 * 操作层 基类通用数据处理
 *
 * @param <Q>   Query
 * @param <D>   Dto
 * @param <IDS> DtoService
 * @author xueyi
 */
public abstract class BaseController<Q extends BaseEntity, D extends BaseEntity, IDS extends IBaseService<Q, D>> extends BaseHandleController<Q, D, IDS> {

    /**
     * 查询列表
     */
    public AjaxResult list(Q query) {
        startPage();
        List<D> list = baseService.selectListScope(query);
        return getDataTable(list);
    }

    /**
     * 导出
     */
    public void export(HttpServletResponse response, Q query) {
        List<D> list = baseService.selectListScope(query);
        ExcelUtil<D> util = new ExcelUtil<>(getDClass());
        util.exportExcel(response, list, StrUtil.format("{}数据", getNodeName()));
    }

    /**
     * 导入
     */
    public AjaxResult importData(MultipartFile file, String importType) throws Exception {
        BaseConstants.ImportType importTypeEnum = BaseConstants.ImportType.getByCode(importType);
        ExcelUtil<D> util = new ExcelUtil<>(getDClass());
        List<D> list = util.importExcel(file.getInputStream());
        return success();
    }

    /**
     * 导入模板下载
     */
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<D> util = new ExcelUtil<>(getDClass());
        util.importTemplateExcel(response, StrUtil.format("{}数据", getNodeName()));
    }

    /**
     * 查询详细
     */
    public AjaxResult getInfo(@PathVariable Serializable id) {
        return success(baseService.selectById(id));
    }

    /**
     * 新增
     */
    public AjaxResult add(@Validated({V_A.class}) @RequestBody D d) {
        d.initOperate(BaseConstants.Operate.ADD);
        AEHandle(d.getOperate(), d);
        return toAjax(baseService.insert(d));
    }

    /**
     * 修改
     */
    public AjaxResult edit(@Validated({V_E.class}) @RequestBody D d) {
        d.initOperate(BaseConstants.Operate.EDIT);
        AEHandle(d.getOperate(), d);
        return toAjax(baseService.update(d));
    }

    /**
     * 修改状态
     */
    public AjaxResult editStatus(@RequestBody D d) {
        d.initOperate(BaseConstants.Operate.EDIT_STATUS);
        AEHandle(d.getOperate(), d);
        return toAjax(baseService.updateStatus(d));
    }

    /**
     * 批量删除
     *
     * @see #RHandleEmpty (List)  基类 空校验
     */
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        RHandleEmpty(idList);
        RHandle(BaseConstants.Operate.DELETE, idList);
        return toAjax(baseService.deleteByIds(idList));
    }

    /**
     * 强制批量删除
     *
     * @see #RHandleEmpty (List)  基类 空校验
     */
    public AjaxResult batchRemoveForce(@PathVariable List<Long> idList) {
        RHandleEmpty(idList);
        RHandle(BaseConstants.Operate.DELETE_FORCE, idList);
        return toAjax(baseService.deleteByIds(idList));
    }

    /**
     * 获取选择框列表
     */
    public AjaxResult option() {
        try {
            Q query = getQClass().getDeclaredConstructor().newInstance();
            query.setStatus(BaseConstants.Status.NORMAL.getCode());
            return list(query);
        } catch (Exception ignored) {
        }
        return error();
    }

    /**
     * 更新缓存数据 | 内部调用
     */
    public R<Boolean> refreshCacheInner() {
        baseService.refreshCache();
        return R.ok();
    }

    /**
     * 更新缓存数据
     */
    public AjaxResult refreshCache() {
        baseService.refreshCache();
        return AjaxResult.success();
    }
}
