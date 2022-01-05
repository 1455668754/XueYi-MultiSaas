package com.xueyi.common.datasource.processor;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.xueyi.common.core.utils.SpringUtils;
import com.xueyi.common.security.service.TokenService;
import com.xueyi.system.api.model.LoginUser;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;

import static com.xueyi.common.core.constant.TenantConstants.ISOLATE;

/**
 * 租户库源策略
 *
 * @author xueyi
 */
@Component
public class DsIsolateExpressionProcessor extends DsProcessor {

    @Override
    public boolean matches(String key) {
        return key.startsWith(ISOLATE);
    }

    @Override
    public String doDetermineDatasource(MethodInvocation invocation, String key) {

        // （待调整，将sourceName直接加入SecurityUtils中）
        TokenService tokenService = SpringUtils.getBean(TokenService.class);
        // 获取当前的用户
        LoginUser loginUser = tokenService.getLoginUser();
        return ObjectUtil.isNotNull(loginUser) ? loginUser.getMainSource() : null;
    }
}