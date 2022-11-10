package com.xueyi.system.dict.manager.impl;

import com.xueyi.common.core.utils.core.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.web.entity.manager.impl.SubBaseManagerImpl;
import com.xueyi.system.api.dict.domain.dto.SysDictDataDto;
import com.xueyi.system.api.dict.domain.dto.SysDictTypeDto;
import com.xueyi.system.api.dict.domain.model.SysDictDataConverter;
import com.xueyi.system.api.dict.domain.model.SysDictTypeConverter;
import com.xueyi.system.api.dict.domain.po.SysDictDataPo;
import com.xueyi.system.api.dict.domain.po.SysDictTypePo;
import com.xueyi.system.api.dict.domain.query.SysDictDataQuery;
import com.xueyi.system.api.dict.domain.query.SysDictTypeQuery;
import com.xueyi.system.dict.manager.ISysDictTypeManager;
import com.xueyi.system.dict.mapper.SysDictDataMapper;
import com.xueyi.system.dict.mapper.SysDictTypeMapper;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 字典类型管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class SysDictTypeManagerImpl extends SubBaseManagerImpl<SysDictTypeQuery, SysDictTypeDto, SysDictTypePo, SysDictTypeMapper, SysDictTypeConverter, SysDictDataQuery, SysDictDataDto, SysDictDataPo, SysDictDataMapper, SysDictDataConverter> implements ISysDictTypeManager {

    /**
     * 校验字典编码是否唯一
     *
     * @param Id   字典类型Id
     * @param code 字典类型编码
     * @return 字典类型对象
     */
    @Override
    public SysDictTypeDto checkDictCodeUnique(Long Id, String code) {
        SysDictTypePo dictType = baseMapper.selectOne(
                Wrappers.<SysDictTypePo>query().lambda()
                        .ne(SysDictTypePo::getId, Id)
                        .eq(SysDictTypePo::getCode, code)
                        .last(SqlConstants.LIMIT_ONE));
        return baseConverter.mapperDto(dictType);
    }

    /**
     * 设置主子表中子表外键值
     */
    @Override
    protected void setForeignKey(LambdaQueryWrapper<SysDictDataPo> queryWrapper, LambdaUpdateWrapper<SysDictDataPo> updateWrapper, SysDictTypeDto dictType, Serializable code) {
        Serializable Code = ObjectUtil.isNotNull(dictType) ? dictType.getCode() : code;
        if (ObjectUtil.isNotNull(queryWrapper))
            queryWrapper.eq(SysDictDataPo::getCode, Code);
        else
            updateWrapper.eq(SysDictDataPo::getCode, Code);
    }
}
