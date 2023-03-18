package com.xueyi.common.cache.utils;

import com.xueyi.common.cache.service.CacheService;
import com.xueyi.common.core.constant.basic.CacheConstants;
import com.xueyi.common.core.utils.core.ConvertUtil;
import com.xueyi.common.core.utils.core.SpringUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.system.api.dict.domain.dto.SysDictDataDto;

import java.util.Collection;
import java.util.List;

/**
 * 字典缓存管理工具类
 *
 * @author xueyi
 */
public class DictUtil {

    /**
     * 获取参数缓存 | 字符串
     *
     * @param code 参数编码
     * @return 参数数据
     */
    public static String getConfigCache(String code) {
        return getConfigCache(code, String.class);
    }

    /**
     * 获取参数缓存 | 数值
     *
     * @param code 参数编码
     * @return 参数数据
     */
    public static Integer getConfigCacheToInt(String code) {
        return getConfigCache(code, Integer.class);
    }

    /**
     * 获取参数缓存
     *
     * @param code 参数编码
     * @return 参数数据
     */
    public static <T> T getConfigCache(String code, Class<T> clazz) {
        String value = SpringUtil.getBean(CacheService.class).getCacheObject(CacheConstants.CacheType.SYS_CONFIG_KEY, code);
        return StrUtil.isNotEmpty(value) ? ConvertUtil.convert(clazz, value) : null;
    }

    /**
     * 获取字典缓存
     *
     * @param code 字典编码
     * @return 字典数据列表
     */
    public static List<SysDictDataDto> getDictCache(String code) {
        return SpringUtil.getBean(CacheService.class).getCacheObject(CacheConstants.CacheType.SYS_DICT_KEY, code);
    }

    /**
     * 获取多个字典缓存
     *
     * @param codeList 字典编码集合
     * @return 字典数据列表
     */
    public static List<List<SysDictDataDto>> getDictListCache(Collection<Object> codeList) {
        return SpringUtil.getBean(CacheService.class).getMultiCacheObject(CacheConstants.CacheType.SYS_DICT_KEY, codeList);
    }
}
