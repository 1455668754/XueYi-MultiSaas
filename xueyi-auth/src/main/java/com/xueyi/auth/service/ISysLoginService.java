package com.xueyi.auth.service;

import com.xueyi.auth.form.RegisterBody;
import com.xueyi.system.api.model.LoginUser;

/**
 * 登录校验 服务层
 *
 * @author xueyi
 */
public interface ISysLoginService {

    /**
     * 登录
     *
     * @param enterpriseName 企业名称
     * @param userName       用户名
     * @param password       密码
     */
    LoginUser login(String enterpriseName, String userName, String password);

    /**
     * 注册
     */
    void register(RegisterBody registerBody);

}
