package com.xueyi.system.application.controller.inner;

import com.xueyi.common.core.constant.basic.AppConstants;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.web.result.R;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.common.web.annotation.TenantIgnore;
import com.xueyi.system.api.application.domain.dto.SysApplicationDto;
import com.xueyi.system.application.controller.base.BSysApplicationController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统服务 | 应用模块 | 应用管理 | 内部调用 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/inner/application")
public class ISysApplicationController extends BSysApplicationController {

    /**
     * 查询消息应用详细
     */
    @TenantIgnore
    @InnerAuth(isAnonymous = true)
    @GetMapping(value = "/id")
    public R<SysApplicationDto> getInfoByIdInner(@RequestParam Long id, @RequestParam AppConstants.AppType appType) {
        SysApplicationDto application = baseService.selectById(id);
        if (ObjectUtil.isNull(application) || ObjectUtil.notEqual(application.getType(), appType.getCode()))
            R.fail("应用不存在！");
        return R.ok(application);
    }

    /**
     * 查询消息应用详细
     */
    @TenantIgnore
    @InnerAuth(isAnonymous = true)
    @GetMapping(value = "/app")
    public R<SysApplicationDto> getInfoByAppIdInner(@RequestParam String appId, @RequestParam AppConstants.AppType appType) {
        SysApplicationDto application = baseService.selectByAppId(appId);
        if (ObjectUtil.isNull(application) || ObjectUtil.notEqual(application.getType(), appType.getCode()))
            R.fail("应用不存在！");
        return R.ok(application);
    }
}
