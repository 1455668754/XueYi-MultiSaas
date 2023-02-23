package com.xueyi.common.security.utils;

import com.xueyi.common.core.utils.core.SpringUtil;
import com.xueyi.common.security.service.TokenService;
import com.xueyi.common.security.utils.base.BaseSecurityUtils;
import com.xueyi.system.api.model.DataScope;

/**
 * 管理端 - 权限获取工具类
 *
 * @author xueyi
 */
public class SecurityUtils extends BaseSecurityUtils {

    /**
     * 获取数据权限信息
     */
    public static DataScope getDataScope() {
        return SpringUtil.getBean(TokenService.class).getDataScope();
    }
}
