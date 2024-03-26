package com.xueyi.common.redis.handler;

import cn.hutool.core.lang.Snowflake;
import com.xueyi.common.core.config.ISnowflakeCreator;
import com.xueyi.common.core.utils.core.IdUtil;
import com.xueyi.common.core.utils.core.NumberUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.SpringUtil;
import com.xueyi.common.redis.constant.RedisConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;
import java.util.Collections;

/**
 * Snowflake创建器 Redis实现
 *
 * @author xueyi
 */
@Slf4j
@Component
public class SnowflakeCreatorHandler implements ISnowflakeCreator {

    /**
     * 创建Snowflake
     *
     * @return Snowflake
     */
    @Override
    public Snowflake createSnowflake() {
        StringRedisTemplate redisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis/snowflake_id.lua")));
        redisScript.setResultType(Long.class);
        Long sortId = redisTemplate.execute(redisScript, Collections.singletonList(RedisConstants.CacheKey.SNOWFLAKE_CREATOR_KEY.getCode()));
        double datacenterId = ObjectUtil.isNotNull(sortId) ? NumberUtil.div(sortId.intValue(), 32, 0, RoundingMode.DOWN) : NumberUtil.Zero;
        int workerId = ObjectUtil.isNotNull(sortId) ? sortId.intValue() % 32 : NumberUtil.Zero;
        log.info("雪花创建成功，sortId:{}, workId:{}, datacenterId：{}", sortId, workerId, datacenterId);
        return IdUtil.getSnowflake(workerId, (int) datacenterId);
    }
}