package com.xueyi.auth.form;

import lombok.Data;

/**
 * 用户登录对象
 *
 * @author xueyi
 */
@Data
public class LoginBody {

    /** 企业账户 */
    private String enterpriseName;

    /** 用户名 */
    private String userName;

    /** 用户密码 */
    private String password;

}