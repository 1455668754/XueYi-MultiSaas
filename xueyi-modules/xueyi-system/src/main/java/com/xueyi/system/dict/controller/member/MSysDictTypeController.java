package com.xueyi.system.dict.controller.member;

import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.security.annotation.MemberAuth;
import com.xueyi.system.dict.controller.base.BSysDictTypeController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统服务 | 字典模块 | 字典类型管理 | 会员端 业务处理
 *
 * @author xueyi
 */
@MemberAuth
@RestController
@RequestMapping("/member/dict")
public class MSysDictTypeController extends BSysDictTypeController {

    /**
     * 根据字典类型查询字典数据信息
     */
    @Override
    @GetMapping(value = "/type")
    public AjaxResult listByCode(@RequestParam("code") String code) {
        return super.listByCode(code);
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @Override
    @GetMapping(value = "/types")
    public AjaxResult listByCodeList(@RequestParam("codeList") List<String> codeList) {
        return super.listByCodeList(codeList);
    }
}
