package com.xueyi.system.api.authority.feign;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.ServiceConstants;
import com.xueyi.common.core.web.result.R;
import com.xueyi.system.api.authority.feign.factory.RemoteLoginFallbackFactory;
import com.xueyi.system.api.model.LoginUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 登录服务
 *
 * @author xueyi
 */
@FeignClient(contextId = "remoteLoginService", value = ServiceConstants.SYSTEM_SERVICE, fallbackFactory = RemoteLoginFallbackFactory.class)
public interface RemoteLoginService {

    /**
     * 查询登录登录信息
     *
     * @param enterpriseName 企业账号
     * @param userName       员工账号
     * @param password       密码
     * @return 结果
     */
    @GetMapping(value = "/login/inner/loginInfo/{enterpriseName}/{userName}/{password}", headers = SecurityConstants.FROM_SOURCE_INNER)
    R<LoginUser> getLoginInfoInner(@PathVariable("enterpriseName") String enterpriseName, @PathVariable("userName") String userName, @PathVariable("password") String password);

}
