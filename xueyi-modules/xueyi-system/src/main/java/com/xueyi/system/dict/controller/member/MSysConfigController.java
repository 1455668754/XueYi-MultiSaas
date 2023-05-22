package com.xueyi.system.dict.controller.member;

import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.system.api.dict.domain.dto.SysConfigDto;
import com.xueyi.system.dict.controller.base.BSysConfigController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 参数配置管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/member/config")
public class MSysConfigController extends BSysConfigController {

    /**
     * 查询参数对象
     *
     * @param code 参数编码
     * @return 参数对象
     */
    @GetMapping("/code")
    public AjaxResult getConfig(@RequestParam("code") String code) {
        return success(baseService.selectConfigByCode(code));
    }

    /**
     * 查询参数
     *
     * @param code 参数编码
     * @return 参数
     */
    @Override
    @GetMapping("/value")
    public AjaxResult getValueByCode(@RequestParam("code") String code) {
        return super.getValueByCode(code);
    }

    /**
     * 参数修改
     */
    @Override
    @PutMapping
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated({V_E.class}) @RequestBody SysConfigDto config) {
        return super.edit(config);
    }

}
