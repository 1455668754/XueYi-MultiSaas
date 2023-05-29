package com.xueyi.system.api.dict.domain.model;

import com.xueyi.common.core.web.entity.model.BaseConverter;
import com.xueyi.system.api.dict.domain.dto.SysDictDataDto;
import com.xueyi.system.api.dict.domain.po.SysDictDataPo;
import com.xueyi.system.api.dict.domain.query.SysDictDataQuery;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * 系统服务 | 字典模块 | 字典数据 对象映射器
 *
 * @author xueyi
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SysDictDataConverter extends BaseConverter<SysDictDataQuery, SysDictDataDto, SysDictDataPo> {
}