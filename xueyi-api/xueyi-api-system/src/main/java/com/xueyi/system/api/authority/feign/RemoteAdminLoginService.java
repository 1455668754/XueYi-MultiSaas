package com.xueyi.system.api.authority.feign;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.ServiceConstants;
import com.xueyi.common.core.web.result.R;
import com.xueyi.system.api.authority.feign.factory.RemoteAdminLoginFallbackFactory;
import com.xueyi.system.api.model.LoginUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 登录服务 | 管理端
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteLoginService", path = "/inner/login/admin", value = ServiceConstants.SYSTEM_SERVICE, fallbackFactory = RemoteAdminLoginFallbackFactory.class)
public interface RemoteAdminLoginService {

    /**
     * 查询登录登录信息
     *
     * @param enterpriseName 企业账号
     * @param userName       员工账号
     * @param password       密码
     * @return 结果
     */
    @GetMapping(headers = SecurityConstants.FROM_SOURCE_INNER)
    R<LoginUser> getLoginInfoInner(@RequestParam("enterpriseName") String enterpriseName, @RequestParam("userName") String userName, @RequestParam("password") String password);

}
