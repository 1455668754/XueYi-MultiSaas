package com.xueyi.system.dict.controller.platform;

import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.security.annotation.PlatformAuth;
import com.xueyi.system.dict.controller.base.BSysConfigController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统服务 | 字典模块 | 参数管理 | 平台端 业务处理
 *
 * @author xueyi
 */
@PlatformAuth
@RestController
@RequestMapping("/platform/config")
public class PSysConfigController extends BSysConfigController {

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

}
