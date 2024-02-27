package com.xueyi.common.core.utils.jwt;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.TokenConstants;
import com.xueyi.common.core.utils.core.ConvertUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Map;

/**
 * Jwt工具类
 *
 * @author xueyi
 */
public class JwtUtil {

    public static String secret = TokenConstants.SECRET;

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    public static Claims parseToken(String token) {
        return Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(secret.getBytes())).build().parseClaimsJws(token).getBody();
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    public static String createToken(Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims).signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS512).compact();
    }

    /**
     * 根据令牌获取企业Id
     *
     * @param token 令牌
     * @return 企业Id
     */

    public static String getEnterpriseId(String token) {
        Claims claims = parseToken(token);
        return getEnterpriseId(claims);
    }

    /**
     * 根据身份信息获取企业Id
     *
     * @param claims 身份信息
     * @return 企业Id
     */
    public static String getEnterpriseId(Claims claims) {
        return getValue(claims, SecurityConstants.BaseSecurity.ENTERPRISE_ID.getCode());
    }

    /**
     * 根据令牌获取企业账号
     *
     * @param token 令牌
     * @return 企业账号
     */
    public static String getEnterpriseName(String token) {
        Claims claims = parseToken(token);
        return getEnterpriseName(claims);
    }

    /**
     * 根据身份信息获取企业账号
     *
     * @param claims 身份信息
     * @return 企业账号
     */
    public static String getEnterpriseName(Claims claims) {
        return getValue(claims, SecurityConstants.BaseSecurity.ENTERPRISE_NAME.getCode());
    }

    /**
     * 根据令牌获取企业类型
     *
     * @param token 令牌
     * @return 企业类型
     */
    public static String getIsLessor(String token) {
        Claims claims = parseToken(token);
        return getIsLessor(claims);
    }

    /**
     * 根据身份信息获取企业类型
     *
     * @param claims 身份信息
     * @return 企业类型
     */
    public static String getIsLessor(Claims claims) {
        return getValue(claims, SecurityConstants.BaseSecurity.IS_LESSOR.getCode());
    }

    /**
     * 根据令牌获取用户Id
     *
     * @param token 令牌
     * @return 用户Id
     */
    public static String getUserId(String token) {
        Claims claims = parseToken(token);
        return getUserId(claims);
    }

    /**
     * 根据身份信息获取用户Id
     *
     * @param claims 身份信息
     * @return 用户Id
     */
    public static String getUserId(Claims claims) {
        return getValue(claims, SecurityConstants.BaseSecurity.USER_ID.getCode());
    }

    /**
     * 根据令牌获取用户账号
     *
     * @param token 令牌
     * @return 用户账号
     */
    public static String getUserName(String token) {
        Claims claims = parseToken(token);
        return getUserName(claims);
    }

    /**
     * 根据身份信息获取用户账号
     *
     * @param claims 身份信息
     * @return 用户账号
     */
    public static String getUserName(Claims claims) {
        return getValue(claims, SecurityConstants.BaseSecurity.USER_NAME.getCode());
    }

    /**
     * 根据令牌获取用户账号
     *
     * @param token 令牌
     * @return 用户账号
     */
    public static String getNickName(String token) {
        Claims claims = parseToken(token);
        return getNickName(claims);
    }

    /**
     * 根据身份信息获取用户账号
     *
     * @param claims 身份信息
     * @return 用户账号
     */
    public static String getNickName(Claims claims) {
        return getValue(claims, SecurityConstants.BaseSecurity.NICK_NAME.getCode());
    }

    /**
     * 根据令牌获取用户类型
     *
     * @param token 令牌
     * @return 用户类型
     */
    public static String getUserType(String token) {
        Claims claims = parseToken(token);
        return getUserType(claims);
    }

    /**
     * 根据身份信息获取用户类型
     *
     * @param claims 身份信息
     * @return 用户类型
     */
    public static String getUserType(Claims claims) {
        return getValue(claims, SecurityConstants.BaseSecurity.USER_TYPE.getCode());
    }

    /**
     * 根据令牌获取账户类型
     *
     * @param token 令牌
     * @return 账户类型
     */
    public static String getAccountType(String token) {
        Claims claims = parseToken(token);
        return getAccountType(claims);
    }

    /**
     * 根据令牌获取账户类型
     *
     * @param claims 身份信息
     * @return 账户类型
     */
    public static String getAccountType(Claims claims) {
        return getValue(claims, SecurityConstants.BaseSecurity.ACCOUNT_TYPE.getCode());
    }

    /**
     * 根据令牌获取租户策略源
     *
     * @param token 令牌
     * @return 租户策略源
     */
    public static String getSourceName(String token) {
        Claims claims = parseToken(token);
        return getSourceName(claims);
    }

    /**
     * 根据令牌获取租户策略源
     *
     * @param claims 身份信息
     * @return 租户策略源
     */
    public static String getSourceName(Claims claims) {
        return getValue(claims, SecurityConstants.BaseSecurity.SOURCE_NAME.getCode());
    }

    /**
     * 根据令牌获取用户令牌
     *
     * @param token 令牌
     * @return 用户令牌
     */
    public static String getUserKey(String token) {
        Claims claims = parseToken(token);
        return getUserKey(claims);
    }

    /**
     * 根据令牌获取用户令牌
     *
     * @param claims 身份信息
     * @return 用户令牌
     */
    public static String getUserKey(Claims claims) {
        String accessToken = getValue(claims, SecurityConstants.BaseSecurity.REFRESH_TOKEN.getCode());
        if (StrUtil.isNotEmpty(accessToken) && StrUtil.startWith(accessToken, TokenConstants.PREFIX))
            return StrUtil.replaceFirst(accessToken, TokenConstants.PREFIX, StrUtil.EMPTY);
        return null;
    }

    /**
     * 根据令牌获取访问令牌
     *
     * @param token 令牌
     * @return 访问令牌
     */
    public static String getAccessKey(String token) {
        Claims claims = parseToken(token);
        return getUserKey(claims);
    }

    /**
     * 根据令牌获取访问令牌
     *
     * @param claims 身份信息
     * @return 访问令牌
     */
    public static String getAccessKey(Claims claims) {
        return getValue(claims, SecurityConstants.BaseSecurity.ACCESS_TOKEN.getCode());
    }

    /**
     * 根据身份信息获取键值
     *
     * @param claims 身份信息
     * @param key    键
     * @return 值
     */
    public static String getValue(Claims claims, String key) {
        return ConvertUtil.toStr(claims.get(key), StrUtil.EMPTY);
    }
}
