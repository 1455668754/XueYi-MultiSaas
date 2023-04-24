package com.xueyi.system.dict.controller.member;

import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.system.dict.controller.base.BSysDictTypeController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 字典类型管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/member/dict")
public class MSysDictTypeController extends BSysDictTypeController {

    /**
     * 根据字典类型查询字典数据信息
     */
    @Override
    @GetMapping(value = "/type/{code}")
    public AjaxResult listByCode(@PathVariable String code) {
        return super.listByCode(code);
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @Override
    @GetMapping(value = "/types/{codeList}")
    public AjaxResult listByCodeList(@PathVariable List<String> codeList) {
        return super.listByCodeList(codeList);
    }
}
