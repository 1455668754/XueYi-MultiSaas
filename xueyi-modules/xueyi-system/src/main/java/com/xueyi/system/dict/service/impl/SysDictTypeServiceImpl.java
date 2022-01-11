package com.xueyi.system.dict.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.BaseConstants;
import com.xueyi.common.core.constant.CacheConstants;
import com.xueyi.common.redis.service.RedisService;
import com.xueyi.common.web.entity.service.impl.SubBaseServiceImpl;
import com.xueyi.system.api.dict.domain.dto.SysDictDataDto;
import com.xueyi.system.api.dict.domain.dto.SysDictTypeDto;
import com.xueyi.system.dict.manager.SysDictDataManager;
import com.xueyi.system.dict.manager.SysDictTypeManager;
import com.xueyi.system.dict.mapper.SysDictDataMapper;
import com.xueyi.system.dict.mapper.SysDictTypeMapper;
import com.xueyi.system.dict.service.ISysDictDataService;
import com.xueyi.system.dict.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xueyi.common.core.constant.TenantConstants.MASTER;

/**
 * 字典类型管理 业务层处理
 *
 * @author xueyi
 */
@Service
@DS(MASTER)
public class SysDictTypeServiceImpl extends SubBaseServiceImpl<SysDictTypeDto, SysDictTypeManager, SysDictTypeMapper, SysDictDataDto, ISysDictDataService, SysDictDataMapper> implements ISysDictTypeService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private SysDictDataManager dictDataManager;

    /**
     * 项目启动时，初始化字典到缓存
     */
    @PostConstruct
    public void init() {
        loadingDictCache();
    }

    /**
     * 根据Id删除参数对象
     *
     * @param id Id
     * @return 结果
     */
    @Override
    public int deleteById(Serializable id) {
        SysDictTypeDto configDto = baseManager.selectById(id);
        deleteDictCache(configDto.getCode());
        return baseManager.deleteById(id);
    }

    /**
     * 根据Id集合删除参数对象
     *
     * @param idList Id集合
     * @return 结果
     */
    @Override
    public int deleteByIds(Collection<? extends Serializable> idList) {
        List<SysDictTypeDto> configList = baseManager.selectListByIds(idList);
        for (SysDictTypeDto dictType : configList)
            deleteDictCache(dictType.getCode());
        return baseManager.deleteByIds(idList);
    }

    /**
     * 加载字典缓存数据
     */
    @Override
    public void loadingDictCache() {
        List<SysDictTypeDto> dictTypeList = baseManager.selectList(null);
        Map<String, List<SysDictDataDto>> dataMap = new HashMap<>();
        for (SysDictTypeDto dictType : dictTypeList)
            dataMap.put(dictType.getCode(), dictDataManager.selectListByCode(dictType.getCode()));
        setDictCache(dataMap);
    }

    /**
     * 清空字典缓存数据
     */
    @Override
    public void clearDictCache() {
        deleteAllDictCache();
    }

    /**
     * 重置字典缓存数据
     */
    @Override
    public void resetDictCache() {
        clearDictCache();
        loadingDictCache();
    }

    /**
     * 校验字典编码是否唯一
     *
     * @param Id       字典类型Id
     * @param dictCode 字典类型编码
     * @return 结果 | true/false 唯一/不唯一
     */
    @Override
    public boolean checkDictCodeUnique(Long Id, String dictCode) {
        return ObjectUtil.isNotNull(baseManager.checkDictCodeUnique(ObjectUtil.isNull(Id) ? BaseConstants.NONE_ID : Id, dictCode));
    }

    /**
     * 字典缓存存储
     *
     * @param dictMap 字典Map
     */
    private void setDictCache(Map<String, List<SysDictDataDto>> dictMap) {
        redisService.setCacheMap(CacheConstants.SYS_DICT_KEY, dictMap);
    }

    /**
     * 删除全部字典缓存
     */
    private void deleteAllDictCache() {
        redisService.deleteObject(CacheConstants.SYS_DICT_KEY);
    }

    /**
     * 根据编码删除字典缓存
     */
    private void deleteDictCache(String code) {
        redisService.deleteCacheMapHKey(CacheConstants.SYS_DICT_KEY, code);
    }

    /**
     * 设置子数据的外键值
     */
    @Override
    protected void setForeignKey(Collection<SysDictDataDto> dictDataList, SysDictDataDto dictData, SysDictTypeDto dictType, Serializable code) {
        String dictCode = ObjectUtil.isNotNull(dictType) ? dictType.getCode() : (String) code;
        if (ObjectUtil.isNotNull(dictData))
            dictData.setCode(dictCode);
        else
            dictDataList.forEach(sub -> sub.setCode(dictCode));
    }
}