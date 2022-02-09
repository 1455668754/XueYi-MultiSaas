package com.xueyi.common.web.entity.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.BaseConstants;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.utils.TreeUtils;
import com.xueyi.common.core.web.entity.TreeEntity;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.web.entity.controller.handle.TreeHandleController;
import com.xueyi.common.web.entity.service.ITreeService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * 操作层 树型通用数据处理
 *
 * @param <D>  Dto
 * @param <DS> DtoService
 * @author xueyi
 */
public abstract class TreeController<D extends TreeEntity<D>, DS extends ITreeService<D>> extends TreeHandleController<D, DS> {

    /**
     * 查询树列表
     */
    @Override
    @GetMapping("/list")
    public AjaxResult list(D d) {
        List<D> list = baseService.selectList(d);
        return AjaxResult.success(TreeUtils.buildTree(list));
    }

    /**
     * 查询树列表（排除节点）
     */
    @GetMapping("/list/exclude")
    public AjaxResult listExNodes(D d) {
        Serializable id = d.getId();
        d.setId(null);
        List<D> list = baseService.selectList(d);
        Iterator<D> it = list.iterator();
        while (it.hasNext()) {
            D next = (D) it.next();
            if (ObjectUtil.equals(next.getId(),id) ||
                    ArrayUtils.contains(StringUtils.split(next.getAncestors(), StrUtil.COMMA), id + StrUtil.EMPTY))
                it.remove();
        }
        return AjaxResult.success(TreeUtils.buildTree(list));
    }

    /**
     * 树型 新增
     * 考虑父节点状态
     *
     * @see #addTreeStatusValidated(D) 树型 父节点逻辑校验
     */
    @Override
    @PostMapping
    public AjaxResult add(@Validated @RequestBody D d) {
        baseRefreshValidated(BaseConstants.Operate.ADD, d);
        addTreeStatusValidated(d);
        return toAjax(baseService.insert(d));
    }

    /**
     * 树型 修改
     * 考虑子节点状态
     *
     * @see #editTreeLoopValidated(D) 树型 父节点不能为自己或自己的子节点
     * @see #editTreeStatusValidated(D) 树型 父子节点逻辑校验
     */
    @Override
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody D d) {
        baseRefreshValidated(BaseConstants.Operate.EDIT, d);
        editTreeLoopValidated(d);
        editTreeStatusValidated(d);
        return toAjax(baseService.update(d));
    }

    /**
     * 树型 强制修改
     *
     * @see #editTreeLoopValidated(D) 树型 父节点不能为自己或自己的子节点
     */
    @Override
    @PutMapping("/force")
    public AjaxResult editForce(@Validated @RequestBody D d) {
        baseRefreshValidated(BaseConstants.Operate.EDIT_FORCE, d);
        editTreeLoopValidated(d);
        return toAjax(baseService.update(d));
    }

    /**
     * 树型 修改状态
     * 考虑子节点状态
     *
     * @see #editStatusTreeStatusValidated(D) 树型 父子节点逻辑校验
     */
    @Override
    @PutMapping("/status")
    public AjaxResult editStatus(@Validated @RequestBody D d) {
        editStatusTreeStatusValidated(d);
        return toAjax(baseService.updateStatus(d.getId(), d.getStatus()));
    }

    /**
     * 树型 批量删除
     * 考虑子节点存在与否
     *
     * @see #removeNullValidated(List)  基类 空校验
     * @see #removeTreeValidated(List)  树型 子节点存在与否校验
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        removeNullValidated(idList);
        baseRemoveValidated(BaseConstants.Operate.DELETE, idList);
        removeNullValidated(idList);
        removeTreeValidated(idList);
        return toAjax(baseService.deleteByIds(idList));
    }
}
