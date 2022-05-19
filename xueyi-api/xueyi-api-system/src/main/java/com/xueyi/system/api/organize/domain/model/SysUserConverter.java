package com.xueyi.system.api.organize.domain.model;

import com.xueyi.common.core.web.entity.model.BaseConverter;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.system.api.organize.domain.po.SysUserPo;
import com.xueyi.system.api.organize.domain.query.SysUserQuery;
import org.mapstruct.Mapper;

/**
 * 用户 对象映射器
 *
 * @author xueyi
 */
@Mapper(componentModel = "spring")
public interface SysUserConverter extends BaseConverter<SysUserQuery, SysUserDto, SysUserPo> {
}
