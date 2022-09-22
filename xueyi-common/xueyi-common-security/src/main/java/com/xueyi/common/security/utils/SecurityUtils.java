package com.xueyi.common.security.utils;

import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.system.AuthorityConstants;
import com.xueyi.common.core.utils.SpringUtils;
import com.xueyi.common.security.service.TokenService;
import com.xueyi.common.security.utils.base.BaseSecurityUtils;
import com.xueyi.system.api.model.DataScope;
import com.xueyi.system.api.model.LoginUser;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.system.api.source.domain.Source;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 管理端 - 权限获取工具类
 *
 * @author xueyi
 */
public class SecurityUtils extends BaseSecurityUtils {

    /**
     * 获取企业信息
     */
    public static SysEnterpriseDto getEnterprise() {
        return SpringUtils.getBean(TokenService.class).getEnterprise();
    }

    /**
     * 获取用户信息
     */
    public static SysUserDto getUser() {
        return SpringUtils.getBean(TokenService.class).getUser();
    }

    /**
     * 获取源策略信息
     */
    public static Source getSource() {
        return SpringUtils.getBean(TokenService.class).getSource();
    }

    /**
     * 获取数据权限信息
     */
    public static DataScope getDataScope() {
        return SpringUtils.getBean(TokenService.class).getDataScope();
    }

    /**
     * 获取登录用户信息
     */
    public static LoginUser getLoginUser() {
        return SpringUtils.getBean(TokenService.class).getLoginUser();
    }

    /**
     * 是否为超管租户
     */
    public static boolean isAdminTenant() {
        return StrUtil.equals(AuthorityConstants.TenantType.ADMIN.getCode(), getIsLessor());
    }

    /**
     * 是否不为超管租户
     */
    public static boolean isNotAdminTenant() {
        return !isAdminTenant();
    }

    /**
     * 是否为超管用户
     */
    public static boolean isAdminUser() {
        return StrUtil.equals(AuthorityConstants.UserType.ADMIN.getCode(), getUserType());
    }

    /**
     * 是否不为超管用户
     */
    public static boolean isNotAdminUser() {
        return !isAdminUser();
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
