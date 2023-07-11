package com.xueyi.system.organize.domain.model;

import com.xueyi.common.core.web.entity.model.TreeConverter;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.po.SysDeptPo;
import com.xueyi.system.api.organize.domain.query.SysDeptQuery;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * 系统服务 | 组织模块 | 部门 对象映射器
 *
 * @author xueyi
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SysDeptConverter extends TreeConverter<SysDeptQuery, SysDeptDto, SysDeptPo> {
}
