package com.xueyi.system.dict.controller;

import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.BaseConstants;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.common.web.entity.controller.SubBaseController;
import com.xueyi.system.api.dict.domain.dto.SysDictDataDto;
import com.xueyi.system.api.dict.domain.dto.SysDictTypeDto;
import com.xueyi.system.dict.service.ISysDictDataService;
import com.xueyi.system.dict.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 字典类型管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/dict/type")
public class SysDictTypeController extends SubBaseController<SysDictTypeDto, ISysDictTypeService, SysDictDataDto, ISysDictDataService> {

    @Autowired
    private ISysDictTypeService dictTypeService;

    /** 定义节点名称 */
    protected String getNodeName() {
        return "字典类型";
    }

    /** 定义子数据名称 */
    protected String getSubName() {
        return "字典数据";
    }

    /**
     * 刷新字典缓存
     */
    @RequiresPermissions("system:dict:remove")
    @Log(title = "字典类型", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public AjaxResult refreshCache() {
        dictTypeService.resetDictCache();
        return AjaxResult.success();
    }

    /**
     * 获取字典选择框列表
     */
    @GetMapping("/optionSelect")
    public AjaxResult optionSelect() {
        return AjaxResult.success(dictTypeService.selectList(null));
    }

    /**
     * 前置校验 （强制）增加/修改
     */
    @Override
    protected void baseRefreshValidated(BaseConstants.Operate operate, SysDictTypeDto dictType) {
        if (baseService.checkDictCodeUnique(dictType.getId(), dictType.getCode()))
            throw new ServiceException(StrUtil.format("{}{}{}失败，字典编码已存在", operate.getInfo(), getNodeName(), dictType.getName()));
    }
}
