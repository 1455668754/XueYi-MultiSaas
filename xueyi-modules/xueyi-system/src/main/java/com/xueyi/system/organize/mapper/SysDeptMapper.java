package com.xueyi.system.organize.mapper;

import com.xueyi.common.web.entity.mapper.SubTreeMapper;
import com.xueyi.system.api.domain.organize.dto.SysDeptDto;
import com.xueyi.system.api.domain.organize.dto.SysPostDto;

/**
 * 部门管理 数据层
 *
 * @author xueyi
 */
public interface SysDeptMapper extends SubTreeMapper<SysDeptDto, SysPostDto> {

}
