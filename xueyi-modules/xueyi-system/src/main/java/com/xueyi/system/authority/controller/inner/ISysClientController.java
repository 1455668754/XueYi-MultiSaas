package com.xueyi.system.authority.controller.inner;

import com.xueyi.common.core.web.result.R;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.system.api.authority.domain.dto.SysClientDto;
import com.xueyi.system.authority.controller.base.BSysClientController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统服务 | 权限模块 | 客户端管理 | 内部调用 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/inner/client")
public class ISysClientController extends BSysClientController {

    /**
     * 获取登录信息
     */
    @InnerAuth(isAnonymous = true)
    @GetMapping("/clientId")
    public R<SysClientDto> getInfoByClientIdInner(@RequestParam String clientId) {
        return R.ok(baseService.selectByClientId(clientId));
    }
}
