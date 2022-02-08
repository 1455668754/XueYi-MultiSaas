package com.xueyi.common.web.entity.controller;

import com.xueyi.common.core.constant.BaseConstants;
import com.xueyi.common.core.web.entity.BaseEntity;
import com.xueyi.common.core.web.entity.SubBaseEntity;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.web.entity.controller.handle.SubBaseHandleController;
import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.common.web.entity.service.ISubBaseService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 操作层 主子基类通用数据处理
 *
 * @param <D>  Dto
 * @param <DS> DtoService
 * @param <S>  SubDto
 * @param <SS> SubService
 * @author xueyi
 */
public abstract class SubBaseController<D extends SubBaseEntity<S>, DS extends ISubBaseService<D, S>, S extends BaseEntity, SS extends IBaseService<S>> extends SubBaseHandleController<D, DS, S, SS> {

    /**
     * 查询详细 | 包含子数据
     */
    @GetMapping(value = "/sub/{id}")
    public AjaxResult getSubInfo(@PathVariable Serializable id) {
        return AjaxResult.success(baseService.selectSubById(id));
    }

    /**
     * 主子型 修改
     * 考虑归属数据状态
     *
     * @see #editSubStatusValidated(SubBaseEntity) 主子树型 归属数据状态逻辑校验
     */
    @Override
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody D d) {
        editSubStatusValidated(d);
        return super.edit(d);
    }

    /**
     * 主子型 修改
     * 考虑归属数据状态
     *
     * @see #editStatusSubStatusValidated(SubBaseEntity)  主子树型 归属数据状态逻辑校验
     */
    @PutMapping("/status")
    public AjaxResult editStatus(@Validated @RequestBody D d) {
        editStatusSubStatusValidated(d);
        return super.editStatus(d);
    }

    /**
     * 主子型 批量删除
     * 考虑归属数据存在与否&&子节点存在与否
     *
     * @see #removeNullValidated(List)   基类 空校验
     * @see #removeSubValidated(List)  主子型 归属数据存在与否校验
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        removeNullValidated(idList);
        baseRemoveValidated(BaseConstants.Operate.DELETE, idList);
        removeNullValidated(idList);
        removeSubValidated(idList);
        return toAjax(baseService.deleteByIds(idList));
    }
}
