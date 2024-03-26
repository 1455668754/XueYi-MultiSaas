package com.xueyi.common.core.config;

import cn.hutool.core.lang.Snowflake;
import com.xueyi.common.core.utils.core.IdUtil;

/**
 * Snowflake创建器
 *
 * @author xueyi
 */
public interface ISnowflakeCreator {

    /**
     * 创建Snowflake
     *
     * @return Snowflake
     */
    default Snowflake createSnowflake() {
        return IdUtil.getSnowflake();
    }
}
