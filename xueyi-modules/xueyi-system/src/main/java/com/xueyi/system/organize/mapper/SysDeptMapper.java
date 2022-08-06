package com.xueyi.system.organize.mapper;

import com.xueyi.common.datasource.annotation.Isolate;
import com.xueyi.common.web.entity.mapper.SubTreeMapper;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.api.organize.domain.po.SysDeptPo;
import com.xueyi.system.api.organize.domain.po.SysPostPo;
import com.xueyi.system.api.organize.domain.query.SysDeptQuery;
import com.xueyi.system.api.organize.domain.query.SysPostQuery;

/**
 * 部门管理 数据层
 *
 * @author xueyi
 */
@Isolate
public interface SysDeptMapper extends SubTreeMapper<SysDeptQuery, SysDeptDto, SysDeptPo, SysPostQuery, SysPostDto, SysPostPo> {
}
