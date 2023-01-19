package com.xueyi.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 身份验证 服务层
 *
 * @author xueyi
 */
public interface IUserDetailsService {

    UserDetails loadUser(String enterpriseName, String userName, String password) throws UsernameNotFoundException;

}

