package com.xueyi.common.core.exception.auth;

import com.xueyi.common.core.utils.core.ArrayUtil;
import com.xueyi.common.core.utils.core.StrUtil;

import java.io.Serial;

/**
 * 未能通过的角色认证异常
 *
 * @author xueyi
 */
public class NotRoleException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public NotRoleException(String role) {
        super(role);
    }

    public NotRoleException(String[] roles) {
        super(ArrayUtil.join(roles, StrUtil.COMMA));
    }
}
