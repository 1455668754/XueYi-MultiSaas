package com.xueyi.system.authority.controller.admin;

import com.xueyi.common.core.web.model.SysEnterprise;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.security.annotation.AdminAuth;
import com.xueyi.common.web.entity.controller.BasisController;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import com.xueyi.system.authority.service.ISysLoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 管理端登录 | 内部调用 业务处理
 *
 * @author xueyi
 */
@AdminAuth
@RestController
@RequestMapping("/admin/login/admin")
public class ASysAdminLoginController extends BasisController {

    @Autowired
    private ISysLoginService loginService;

    /**
     * 校验域名是否存在默认租户
     *
     * @param url 域名信息
     */
    @AdminAuth(isAnonymous = true)
    @GetMapping("/getEnterpriseByDomainName")
    public AjaxResult getDomainTenant(@RequestParam String url) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isNotBlank(url)) {
            SysEnterpriseDto enterpriseInfo = loginService.getDomainTenant(url);
            Optional.ofNullable(enterpriseInfo).map(SysEnterprise::getDomainName)
                    .ifPresent(domainName -> map.put("name", domainName));
        }
        return success(map);
    }
}
