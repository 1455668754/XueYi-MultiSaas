package com.xueyi.common.web.entity.controller;

import com.xueyi.common.core.web.entity.BaseEntity;
import com.xueyi.common.core.web.entity.SubTreeEntity;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.web.entity.controller.handle.SubTreeHandleController;
import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.common.web.entity.service.ISubTreeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 操作层 主子树型通用数据处理
 *
 * @param <D>  Dto
 * @param <DS> DtoService
 * @param <S>  SubDto
 * @param <SS> SubService
 * @author xueyi
 */
public abstract class SubTreeController<D extends SubTreeEntity<D, S>, DS extends ISubTreeService<D, S>, S extends BaseEntity, SS extends IBaseService<S>> extends SubTreeHandleController<D, DS, S, SS> {

    /**
     * 查询详细 | 包含子数据
     */
    @GetMapping(value = "/sub/{id}")
    public AjaxResult getSubInfo(@PathVariable Serializable id) {
        return AjaxResult.success(baseService.selectSubById(id));
    }

    /**
     * 主子树型 修改
     * 考虑归属数据状态&&子节点状态
     *
     * @see #editSubStatusValidated(SubTreeEntity) 主子树型 归属数据状态逻辑校验
     */
    @Override
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody D d) {
        editSubStatusValidated(d);
        return super.edit(d);
    }

    /**
     * 主子树型 修改
     * 考虑归属数据状态&&子节点状态
     *
     * @see #editStatusSubStatusValidated(SubTreeEntity)  主子树型 归属数据状态逻辑校验
     */
    @PutMapping("/status")
    public AjaxResult editStatus(@Validated @RequestBody D d) {
        editStatusSubStatusValidated(d);
        return super.editStatus(d);
    }

    /**
     * 主子树型 删除
     * 考虑归属数据存在与否&&子节点存在与否
     *
     * @see #removeNullValidated(List)   基类 空校验
     * @see #removeTreeSubValidated(List)  主子树型 子节点存在与否校验&&归属数据存在与否校验
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        removeNullValidated(idList);
        removeTreeSubValidated(idList);
        return super.batchRemove(idList);
    }
}