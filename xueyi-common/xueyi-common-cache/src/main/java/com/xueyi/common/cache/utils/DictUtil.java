package com.xueyi.common.cache.utils;

import com.xueyi.common.cache.constant.CacheConstants;
import com.xueyi.common.cache.service.CacheService;
import com.xueyi.common.core.constant.basic.DictConstants;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.context.SecurityContextHolder;
import com.xueyi.common.core.exception.UtilException;
import com.xueyi.common.core.utils.core.ConvertUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.SpringUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.system.api.dict.domain.dto.SysConfigDto;
import com.xueyi.system.api.dict.domain.dto.SysDictDataDto;
import com.xueyi.system.api.dict.domain.dto.SysDictTypeDto;
import com.xueyi.system.api.dict.domain.dto.SysImExDto;
import com.xueyi.system.api.dict.feign.RemoteConfigService;
import com.xueyi.system.api.dict.feign.RemoteDictService;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典缓存管理工具类
 *
 * @author xueyi
 */
public class DictUtil {

    private static CacheService cacheService;

    private static CacheService getCacheService() {
        if (ObjectUtil.isNull(cacheService)) {
            cacheService = SpringUtil.getBean(CacheService.class);
        }
        return cacheService;
    }

    /**
     * 获取参数缓存
     *
     * @param type 参数类型
     * @return 参数数据
     */
    public static <T> T getConfigCache(CacheConstants.ConfigType type) {
        return getCusConfigCache(type.getCode(), type.getClazz(), type.getDefaultValue());
    }

    /**
     * 获取参数缓存
     *
     * @param code 参数编码
     * @return 参数数据
     */
    public static <T> T getCusConfigCache(String code) {
        return getCusConfigCache(code, String.class, StrUtil.EMPTY);
    }

    /**
     * 获取参数缓存
     *
     * @param code         参数编码
     * @param clazz        数据类型
     * @param defaultValue 默认值
     * @return 参数数据
     */
    @SuppressWarnings(value = {"unchecked"})
    public static <T> T getCusConfigCache(String code, Class<?> clazz, Object defaultValue) {
        SysConfigDto config = getCacheService().getCacheObject(CacheConstants.CacheType.ROUTE_CONFIG_KEY, code);
        if (ObjectUtil.isNull(config)) {
            return null;
        }
        String value;
        if (StrUtil.equals(DictConstants.DicCacheType.TENANT.getCode(), config.getCacheType())) {
            value = getCacheService().getCacheObject(CacheConstants.CacheType.SYS_CONFIG_KEY, code);
            if (ObjectUtil.isNull(value)) {
                SpringUtil.getBean(RemoteConfigService.class).syncCacheInner();
                value = getCacheService().getCacheObject(CacheConstants.CacheType.SYS_CONFIG_KEY, code);
            }
        } else {
            value = SecurityContextHolder.setEnterpriseIdFun(SecurityConstants.COMMON_TENANT_ID.toString(), () -> getCacheService().getCacheObject(CacheConstants.CacheType.TE_CONFIG_KEY, code));
        }
        return (T) ConvertUtil.convert(clazz, value, defaultValue);
    }

    /**
     * 获取字典缓存
     *
     * @param codes 字典编码集合
     * @return 字典数据列表
     */
    public static Map<String, List<SysDictDataDto>> getDictCache(Collection<String> codes) {
        Map<String, List<SysDictDataDto>> map = new HashMap<>();
        codes.forEach(code -> map.put(code, getDictCache(code)));
        return map;
    }

    /**
     * 获取字典缓存
     *
     * @param code 字典编码
     * @return 字典数据列表
     */
    public static List<SysDictDataDto> getDictCache(String code) {
        SysDictTypeDto type = getCacheService().getCacheObject(CacheConstants.CacheType.ROUTE_DICT_KEY, code);
        if (ObjectUtil.isNull(type)) {
            return null;
        }
        List<SysDictDataDto> dictList;
        if (StrUtil.equals(DictConstants.DicCacheType.TENANT.getCode(), type.getCacheType())) {
            dictList = getCacheService().getCacheObject(CacheConstants.CacheType.SYS_DICT_KEY, code);
            if (ObjectUtil.isNull(dictList)) {
                SpringUtil.getBean(RemoteDictService.class).syncCacheInner();
                dictList = getCacheService().getCacheObject(CacheConstants.CacheType.SYS_DICT_KEY, code);
            }
        } else {
            dictList = SecurityContextHolder.setEnterpriseIdFun(SecurityConstants.COMMON_TENANT_ID.toString(), () -> getCacheService().getCacheObject(CacheConstants.CacheType.TE_DICT_KEY, code));
        }
        return dictList;
    }

    /**
     * 获取导入导出配置缓存
     *
     * @param exCode 配置编码
     * @return 配置数据
     */
    public static SysImExDto getCusExCache(String exCode) {
        SysImExDto exInfo = getCacheService().getCacheObject(CacheConstants.CacheType.ROUTE_IM_EX_KEY, exCode);
        if (ObjectUtil.isNull(exInfo)) {
            return null;
        }
        SysImExDto value;
        if (StrUtil.equals(DictConstants.DicCacheType.TENANT.getCode(), exInfo.getCacheType())) {
            value = getCacheService().getCacheObject(CacheConstants.CacheType.SYS_IM_EX_KEY, exCode);
            if (ObjectUtil.isNull(value)) {
                SpringUtil.getBean(RemoteConfigService.class).syncCacheInner();
                value = getCacheService().getCacheObject(CacheConstants.CacheType.SYS_IM_EX_KEY, exCode);
            }
        } else {
            value = SecurityContextHolder.setEnterpriseIdFun(SecurityConstants.COMMON_TENANT_ID.toString(), () -> getCacheService().getCacheObject(CacheConstants.CacheType.TE_IM_EX_KEY, exCode));
        }
        return value;
    }


    /**
     * 获取导入配置缓存
     *
     * @param exCode 配置编码
     * @return 配置数据
     */
    public static String getCusExImportCache(String exCode) {
        SysImExDto exInfo = getCusExCache(exCode);
        if (ObjectUtil.isNotNull(exInfo) && StrUtil.isNotBlank(exInfo.getImportConfig())) {
            return exInfo.getImportConfig();
        } else {
            throw new UtilException("编码{}对应的导入配置不存在", exCode);
        }
    }

    /**
     * 获取导出配置缓存
     *
     * @param exCode 配置编码
     * @return 配置数据
     */
    public static String getCusExExportCache(String exCode) {
        SysImExDto exInfo = getCusExCache(exCode);
        if (ObjectUtil.isNotNull(exInfo) && StrUtil.isNotBlank(exInfo.getExportConfig())) {
            return exInfo.getExportConfig();
        } else {
            throw new UtilException("编码{}对应的导出配置不存在", exCode);
        }
    }
}
