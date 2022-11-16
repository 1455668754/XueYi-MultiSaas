package com.xueyi.common.redis.service;

import cn.hutool.core.collection.CollUtil;
import com.xueyi.common.core.utils.core.ArrayUtil;
import com.xueyi.common.core.utils.core.NumberUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 缓存字典工具类
 *
 * @author xueyi
 **/
@Component
public class DictService {

    @Autowired
    public RedisService redisService;

    /**
     * 更新LIST缓存字典
     *
     * @param optionKey 集合缓存键值
     * @param dataList  数据集合
     */
    public <T> void refreshListCache(String optionKey, List<T> dataList) {
        redisService.setCacheList(optionKey, dataList, NumberUtil.Nine, TimeUnit.HOURS);
    }

    /**
     * 全量更新MAP缓存字典
     *
     * @param mapKey    缓存键值
     * @param cacheList 缓存数据集合
     * @param keyGet    键名
     * @param valueGet  值名
     */
    public <T, K> void refreshMapCache(String mapKey, List<T> cacheList, Function<? super T, String> keyGet, Function<? super T, K> valueGet) {
        Map<String, K> resultMap = new HashMap<>();
        if (CollUtil.isNotEmpty(cacheList))
            resultMap = cacheList.stream().collect(Collectors.toMap(keyGet, valueGet));
        redisService.setCacheMap(mapKey, resultMap);
        redisService.expire(mapKey, NumberUtil.Nine, TimeUnit.HOURS);
    }

    /**
     * 更新MAP缓存字典
     *
     * @param mapKey   缓存键值
     * @param keyGet   键名
     * @param valueGet 值名
     */
    public <T> void refreshMapValueCache(String mapKey, Supplier<Serializable> keyGet, Supplier<T> valueGet) {
        if (ObjectUtil.isNotNull(valueGet.get()))
            redisService.setCacheMapValue(mapKey, keyGet.get().toString(), valueGet.get());
        else
            redisService.deleteCacheMapValue(mapKey, keyGet.get().toString());
        redisService.expire(mapKey, NumberUtil.Nine, TimeUnit.HOURS);
    }

    /**
     * 删除MAP缓存字典指定键数据
     *
     * @param mapKey 缓存键值
     * @param keys   键名
     */
    public void removeMapValueCache(String mapKey, Serializable... keys) {
        if (ArrayUtil.isNotEmpty(keys))
            redisService.deleteCacheMapValue(mapKey, keys);
    }
}
