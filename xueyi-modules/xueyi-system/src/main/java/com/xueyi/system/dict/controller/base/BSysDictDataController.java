package com.xueyi.system.dict.controller.base;

import cn.hutool.core.collection.CollUtil;
import com.xueyi.common.cache.utils.DictUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.api.dict.domain.dto.SysDictDataDto;
import com.xueyi.system.api.dict.domain.query.SysDictDataQuery;
import com.xueyi.system.dict.service.ISysDictDataService;

import java.util.List;

/**
 * 字典数据管理 业务处理
 *
 * @author xueyi
 */
public class BSysDictDataController extends BaseController<SysDictDataQuery, SysDictDataDto, ISysDictDataService> {

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "字典数据";
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    public AjaxResult listByCode(String code) {
        if (StrUtil.isEmpty(code)) {
            AjaxResult.error("请传入编码后再查询字典");
        }
        return AjaxResult.success(DictUtil.getDictCache(code));
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    public AjaxResult listByCodeList(List<String> codeList) {
        if (CollUtil.isEmpty(codeList)) {
            AjaxResult.error("请传入编码后再查询字典");
        }
        return AjaxResult.success(DictUtil.getDictCache(codeList));
    }

}
