package com.xueyi.common.core.utils.core;

/**
 * 加密算法工具类
 *
 * @author xueyi
 */
public class SecureUtil extends cn.hutool.crypto.SecureUtil {

    static {
        // 关闭hutool 强制关闭Bouncy Castle库的依赖
        SecureUtil.disableBouncyCastle();
    }
}