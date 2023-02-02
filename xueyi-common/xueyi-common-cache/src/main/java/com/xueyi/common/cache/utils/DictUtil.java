package com.xueyi.common.cache.utils;

import com.xueyi.common.cache.service.CacheService;
import com.xueyi.common.core.constant.basic.CacheConstants;
import com.xueyi.common.core.utils.core.SpringUtil;
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
     * 获取字典缓存
     *
     * @param code 字典编码
     * @return 字典数据列表
     */
    public static List<SysDictDataDto> getConfigCache(String code) {
        return SpringUtil.getBean(CacheService.class).getCacheObject(CacheConstants.CacheType.SYS_CONFIG_KEY, code);
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
