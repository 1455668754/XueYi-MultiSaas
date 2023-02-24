package com.xueyi.auth.service;

import com.xueyi.auth.form.RegisterBody;

/**
 * 登录校验 服务层
 *
 * @author xueyi
 */
public interface ISysLoginService {

    /**
     * 注册
     */
    void register(RegisterBody registerBody);

}
