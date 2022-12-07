package com.xueyi.common.core.exception.auth;

import java.io.Serial;

/**
 * 未能通过的登录认证异常
 *
 * @author xueyi
 */
public class NotLoginException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public NotLoginException(String message) {
        super(message);
    }
}
