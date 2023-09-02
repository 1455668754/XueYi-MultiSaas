package com.xueyi.system.file.mapper;

import com.xueyi.system.file.domain.query.SysFileQuery;
import com.xueyi.system.file.domain.dto.SysFileDto;
import com.xueyi.system.file.domain.po.SysFilePo;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.common.datasource.annotation.Isolate;

/**
 * 系统服务 | 素材模块 | 文件管理 数据层
 *
 * @author xueyi
 */
@Isolate
public interface SysFileMapper extends BaseMapper<SysFileQuery, SysFileDto, SysFilePo> {
}