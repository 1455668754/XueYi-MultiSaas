package com.xueyi.common.web.entity.controller;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.web.entity.base.SubTreeEntity;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.web.entity.controller.handle.SubTreeHandleController;
import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.common.web.entity.service.ISubTreeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 操作层 主子树型通用数据处理
 *
 * @param <Q>   Query
 * @param <D>   Dto
 * @param <IDS> DtoService
 * @param <SQ>  SubQuery
 * @param <SD>  SubDto
 * @param <ISS> SubService
 * @author xueyi
 */
public abstract class SubTreeController<Q extends SubTreeEntity<D, SD>, D extends SubTreeEntity<D, SD>, IDS extends ISubTreeService<Q, D, SQ, SD>, SQ extends BaseEntity, SD extends BaseEntity, ISS extends IBaseService<SQ, SD>> extends SubTreeHandleController<Q, D, IDS, SQ, SD, ISS> {

    /**
     * 主子树型 修改
     * 考虑归属数据状态&&子节点状态
     *
     * @see #EHandleSubStatusValidated(SubTreeEntity) 主子树型 归属数据状态逻辑校验
     */
    @Override
    public AjaxResult edit(@Validated({V_E.class}) @RequestBody D d) {
        EHandleSubStatusValidated(d);
        return super.edit(d);
    }

    /**
     * 主子树型 修改
     * 考虑归属数据状态&&子节点状态
     *
     * @see #ESHandleSubStatusValidated(SubTreeEntity)  主子树型 归属数据状态逻辑校验
     */
    public AjaxResult editStatus(@RequestBody D d) {
        ESHandleSubStatusValidated(d);
        return super.editStatus(d);
    }

    /**
     * 主子树型 批量删除
     * 考虑归属数据存在与否&&子节点存在与否
     *
     * @see #RHandleEmptyValidated(List)   基类 空校验
     * @see #RHandleTreeSubValidated(List)  主子树型 子节点存在与否校验&&归属数据存在与否校验
     */
    @Override
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        RHandleEmptyValidated(idList);
        RHandleValidated(BaseConstants.Operate.DELETE, idList);
        RHandleEmptyValidated(idList);
        RHandleTreeSubValidated(idList);
        return toAjax(baseService.deleteByIds(idList));
    }
}
