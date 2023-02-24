package com.xueyi.common.redis.service;

import com.xueyi.common.core.exception.UtilException;
import com.xueyi.common.core.utils.core.*;
import com.xueyi.common.redis.configure.FastJson2JsonRedisSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * spring redis 工具类
 *
 * @author xueyi
 **/
@Component
@SuppressWarnings(value = {"unchecked", "rawtypes"})
public class RedisService {

    @Autowired
    public RedisTemplate redisTemplate;

    private final FastJson2JsonRedisSerializer serializer = new FastJson2JsonRedisSerializer(Object.class);

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   Redis键
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      Redis键
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void setCacheObject(final String key, final T value, final Long timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等 | 指定序列化
     *
     * @param key        Redis键
     * @param value      缓存的值
     * @param serializer 序列化类型
     */
    public <T> void setCacheObject(final String key, final T value, RedisSerializer<?> serializer) {
        redisTemplate.setValueSerializer(serializer);
        setCacheObject(key, value);
        redisTemplate.setValueSerializer(this.serializer);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等 | 指定序列化
     *
     * @param key        Redis键
     * @param value      缓存的值
     * @param timeout    时间
     * @param timeUnit   时间颗粒度
     * @param serializer 序列化类型
     */
    public <T> void setCacheObject(final String key, final T value, final Long timeout, final TimeUnit timeUnit, RedisSerializer<?> serializer) {
        redisTemplate.setValueSerializer(serializer);
        setCacheObject(key, value, timeout, timeUnit);
        redisTemplate.setValueSerializer(this.serializer);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return true=设置成功；false=设置失败
     */
    public Boolean expire(final String key, final long timeout, final TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获取有效时间
     *
     * @param key Redis键
     * @return 有效时间
     */
    public Long getExpire(final String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 判断 key是否存在
     *
     * @param key Redis键
     * @return true 存在 false不存在
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 获得缓存的基本对象
     *
     * @param key Redis键
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 获得缓存的基本对象 | 指定序列化
     *
     * @param key        Redis键
     * @param serializer 序列化类型
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key, RedisSerializer<?> serializer) {
        redisTemplate.setValueSerializer(serializer);
        T obj = getCacheObject(key);
        redisTemplate.setValueSerializer(this.serializer);
        return obj;
    }

    /**
     * 删除单个对象
     *
     * @param key Redis键
     * @return true=删除成功；false=删除失败
     */
    public Boolean deleteObject(final String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return 结果
     */
    public Long deleteObject(final Collection collection) {
        return redisTemplate.delete(collection);
    }

    /**
     * 缓存List数据
     *
     * @param key      Redis键
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> long setCacheList(final String key, final List<T> dataList) {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null ? 0 : count;
    }

    /**
     * 缓存List数据
     *
     * @param key      Redis键
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> long setCacheList(final String key, final List<T> dataList, final long timeout, final TimeUnit unit) {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        if (ObjectUtil.isNotNull(count))
            expire(key, timeout, unit);
        return count == null ? 0 : count;
    }

    /**
     * 获得缓存的list对象
     *
     * @param key Redis键
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(final String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 缓存Set
     *
     * @param key     Redis键
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public <T> BoundSetOperations<String, T> setCacheSet(final String key, final Set<T> dataSet) {
        BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
        for (T t : dataSet) {
            setOperation.add(t);
        }
        return setOperation;
    }

    /**
     * 获得缓存的set
     *
     * @param key Redis键
     * @return 集合
     */
    public <T> Set<T> getCacheSet(final String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 缓存Map
     *
     * @param key     Redis键
     * @param dataMap map
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap) {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    /**
     * 缓存Map
     *
     * @param key     Redis键
     * @param dataMap map
     * @param timeout 超时时间
     * @param unit    时间单位
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap, final long timeout, final TimeUnit unit) {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
            expire(key, timeout, unit);
        }
    }


    /**
     * 获得缓存的Map
     *
     * @param key Redis键
     * @return 哈希
     */
    public <T> Map<String, T> getCacheMap(final String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 往Hash中存入数据
     *
     * @param key   Redis键
     * @param hKey  Hash键
     * @param value 值
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value) {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public <T> T getCacheMapValue(final String key, final String hKey) {
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hKey);
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key   Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys) {
        return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    /**
     * 删除Hash中的某条数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return 是否成功
     */
    public boolean deleteCacheMapValue(final String key, final String hKey) {
        return redisTemplate.opsForHash().delete(key, hKey) > 0;
    }

    /**
     * 删除Hash中的某条数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return 是否成功
     */
    public boolean deleteCacheMapValue(final String key, final Object[] hKey) {
        return redisTemplate.opsForHash().delete(key, hKey) > 0;
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Collection<String> keys(final String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 更新LIST缓存字典
     *
     * @param optionKey 集合缓存键值
     * @param dataList  数据集合
     */
    public <T> void refreshListCache(String optionKey, List<T> dataList) {
        setCacheList(optionKey, dataList, NumberUtil.Nine, TimeUnit.HOURS);
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
            resultMap = cacheList.stream()
                    .filter(item -> {
                        if (StrUtil.isEmpty(keyGet.apply(item)))
                            throw new UtilException("cache key cannot be empty");
                        return ObjectUtil.isNotNull(valueGet.apply(item));
                    }).collect(Collectors.toMap(keyGet, valueGet));
        if (MapUtil.isNotEmpty(resultMap)) {
            setCacheMap(mapKey, resultMap, NumberUtil.Nine, TimeUnit.HOURS);
        } else {
            deleteObject(mapKey);
        }
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
            setCacheMapValue(mapKey, keyGet.get().toString(), valueGet.get());
        else
            deleteCacheMapValue(mapKey, keyGet.get().toString());
        if (hasKey(mapKey))
            expire(mapKey, NumberUtil.Nine, TimeUnit.HOURS);
    }

    /**
     * 删除MAP缓存字典指定键数据
     *
     * @param mapKey 缓存键值
     * @param keys   键名
     */
    public void removeMapValueCache(String mapKey, Serializable... keys) {
        if (ArrayUtil.isNotEmpty(keys))
            deleteCacheMapValue(mapKey, keys);
    }
}
