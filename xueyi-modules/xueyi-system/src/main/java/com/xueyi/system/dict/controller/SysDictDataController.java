package com.xueyi.system.dict.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.security.utils.DictUtils;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.api.dict.domain.dto.SysDictDataDto;
import com.xueyi.system.dict.service.ISysDictDataService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 字典数据管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/dict/data")
public class SysDictDataController extends BaseController<SysDictDataDto, ISysDictDataService> {

    /** 定义节点名称 */
    protected String getNodeName() {
        return "字典数据";
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/type/{code}")
    public AjaxResult listByCode(@PathVariable String code) {
        List<SysDictDataDto> data = baseService.selectListByCode(code);
        return AjaxResult.success(ObjectUtil.isNotNull(data) ? data : new ArrayList<SysDictDataDto>());
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/types/{codeList}")
    public AjaxResult mapByCodeList(@PathVariable List<String> codeList) {
        if (CollUtil.isEmpty(codeList))
            AjaxResult.error("请传入编码后再查询字典");
        HashMap<String, List<SysDictDataDto>> map = new HashMap<>();
        for (String code : codeList)
            map.put(code, DictUtils.getDictCache(code));
        return AjaxResult.success(map);
    }
}
