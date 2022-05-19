package com.xueyi.system.api.organize.domain.model;

import com.xueyi.common.core.web.entity.model.SubTreeConverter;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.po.SysDeptPo;
import com.xueyi.system.api.organize.domain.query.SysDeptQuery;
import org.mapstruct.Mapper;

/**
 * 部门 对象映射器
 *
 * @author xueyi
 */
@Mapper(componentModel = "spring")
public interface SysDeptConverter extends SubTreeConverter<SysDeptQuery, SysDeptDto, SysDeptPo> {
}
