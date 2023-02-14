package com.xueyi.system.dict.manager.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.web.entity.manager.impl.BaseManagerImpl;
import com.xueyi.system.api.dict.domain.dto.SysDictDataDto;
import com.xueyi.system.api.dict.domain.model.SysDictDataConverter;
import com.xueyi.system.api.dict.domain.po.SysDictDataPo;
import com.xueyi.system.api.dict.domain.query.SysDictDataQuery;
import com.xueyi.system.dict.manager.ISysDictDataManager;
import com.xueyi.system.dict.mapper.SysDictDataMapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 字典数据管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class SysDictDataManagerImpl extends BaseManagerImpl<SysDictDataQuery, SysDictDataDto, SysDictDataPo, SysDictDataMapper, SysDictDataConverter> implements ISysDictDataManager {

    /**
     * 查询字典数据对象列表
     *
     * @param code 字典编码
     * @return 字典数据对象集合
     */
    @Override
    public List<SysDictDataDto> selectListByCode(String code) {
        List<SysDictDataPo> dictDataList = baseMapper.selectList(
                Wrappers.<SysDictDataPo>query().lambda()
                        .eq(SysDictDataPo::getCode, code));
        return baseConverter.mapperDto(dictDataList);
    }

    /**
     * 批量查询字典数据对象列表
     *
     * @param codes 字典编码集合
     * @return 字典数据对象集合
     */
    @Override
    public List<SysDictDataDto> selectListByCodes(Collection<String> codes) {
        List<SysDictDataPo> dictDataList = baseMapper.selectList(
                Wrappers.<SysDictDataPo>query().lambda()
                        .in(SysDictDataPo::getCode, codes));
        return baseConverter.mapperDto(dictDataList);
    }
}
