package com.xueyi.common.core.utils.core;

import org.springframework.boot.autoconfigure.thread.Threading;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * spring工具类
 *
 * @author xueyi
 */
@Component
public class SpringUtil extends cn.hutool.extra.spring.SpringUtil {

    public static boolean isVirtual() {
        return Threading.VIRTUAL.isActive(getBean(Environment.class));
    }
}