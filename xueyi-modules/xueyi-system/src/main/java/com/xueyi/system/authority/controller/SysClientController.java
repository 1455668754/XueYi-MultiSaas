package com.xueyi.system.authority.controller;

import com.xueyi.common.core.web.result.R;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.api.authority.domain.dto.SysClientDto;
import com.xueyi.system.api.authority.domain.query.SysClientQuery;
import com.xueyi.system.authority.service.ISysClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统服务 | 权限模块 | 客户端管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/client")
public class SysClientController extends BaseController<SysClientQuery, SysClientDto, ISysClientService> {

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "客户端";
    }

    /**
     * 获取登录信息 | 内部调用
     */
    @InnerAuth(isAnonymous = true)
    @GetMapping("/inner/clientId/{clientId}")
    public R<SysClientDto> getInfoByClientIdInner(@PathVariable String clientId) {
        return R.ok(baseService.selectByClientId(clientId));
    }
}
