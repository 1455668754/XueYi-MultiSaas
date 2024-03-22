package com.xueyi.common.redis.handler;

import com.xueyi.common.core.utils.core.StrUtil;
import org.redisson.api.NameMapper;

/**
 * 缓存key前缀处理
 *
 * @author xueyi
 */
public class CachePrefixHandler implements NameMapper {

    private final String keyPrefix;

    public CachePrefixHandler(String keyPrefix) {
        this.keyPrefix = StrUtil.isBlank(keyPrefix) ? StrUtil.EMPTY : keyPrefix + StrUtil.COLON;
    }

    /**
     * 增加前缀
     */
    @Override
    public String map(String name) {
        return StrUtil.isBlank(name) ? null : StrUtil.isNotBlank(keyPrefix) && !name.startsWith(keyPrefix) ? keyPrefix + name : name;
    }

    /**
     * 去除前缀
     */
    @Override
    public String unmap(String name) {
        return StrUtil.isBlank(name) ? null : StrUtil.isNotBlank(keyPrefix) && name.startsWith(keyPrefix) ? name.substring(keyPrefix.length()) : name;
    }
}
