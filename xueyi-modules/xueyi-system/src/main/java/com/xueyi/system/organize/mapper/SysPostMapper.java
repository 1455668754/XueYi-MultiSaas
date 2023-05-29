package com.xueyi.system.organize.mapper;

import com.xueyi.common.datasource.annotation.Isolate;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.api.organize.domain.po.SysPostPo;
import com.xueyi.system.api.organize.domain.query.SysPostQuery;

/**
 * 系统服务 | 组织模块 | 岗位管理 数据层
 *
 * @author xueyi
 */
@Isolate
public interface SysPostMapper extends BaseMapper<SysPostQuery, SysPostDto, SysPostPo> {
}
