package com.xueyi.system.authority.controller.inner;

import com.xueyi.common.cache.utils.SourceUtil;
import com.xueyi.common.core.context.SecurityContextHolder;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.web.model.SysSource;
import com.xueyi.common.core.web.result.R;
import com.xueyi.common.security.annotation.InnerAuth;
import com.xueyi.common.web.entity.controller.BasisController;
import com.xueyi.system.api.application.domain.dto.SysApplicationDto;
import com.xueyi.system.api.model.LoginPlatform;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import com.xueyi.system.application.service.ISysApplicationService;
import com.xueyi.system.organize.service.ISysEnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 平台端登录 | 内部调用 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/inner/login/platform")
public class ISysPlatformLoginController extends BasisController {

    @Autowired
    private ISysApplicationService applicationService;

    @Autowired
    private ISysEnterpriseService enterpriseService;

    /**
     * 获取登录信息
     */
    @InnerAuth(isAnonymous = true)
    @GetMapping
    public R<LoginPlatform> getLoginInfoInner(@RequestParam String appId) {
        return getLoginInfoInner(appId, null);
    }

    /**
     * 获取登录信息
     */
    @InnerAuth(isAnonymous = true)
    @GetMapping("/enterprise")
    public R<LoginPlatform> getLoginInfoInner(@RequestParam String appId, @RequestParam Long enterpriseId) {
        SysApplicationDto application = applicationService.login(appId, enterpriseId);
        Long tenantId = application.getTenantId();
        SecurityContextHolder.setEnterpriseId(tenantId.toString());
        SysEnterpriseDto enterprise = enterpriseService.selectById(tenantId);
        // 不存在直接返回空数据 | 与网络调用错误区分
        if (ObjectUtil.isNull(enterprise))
            return R.ok(null, "企业账号不存在");

        SecurityContextHolder.setIsLessor(enterprise.getIsLessor());
        SysSource source = SourceUtil.getSourceCache(enterprise.getStrategyId());
        // 不存在直接返回空数据 | 与网络调用错误区分
        if (ObjectUtil.isNull(source))
            return R.ok(null, "数据源不存在");
        SecurityContextHolder.setSourceName(source.getMaster());

        LoginPlatform loginUser = new LoginPlatform();

        loginUser.initEnterprise(enterprise);
        loginUser.initSource(source);
        loginUser.setUser(application);
        loginUser.setUserId(application.getId());
        loginUser.setUserName(application.getAppId());
        loginUser.setNickName(application.getName());
        loginUser.setUserType(application.getType());
        loginUser.setAppId(application.getAppId());
        return R.ok(loginUser);
    }
}
