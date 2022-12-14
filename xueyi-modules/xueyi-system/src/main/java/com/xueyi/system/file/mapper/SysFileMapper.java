package com.xueyi.system.file.mapper;

import com.xueyi.common.datasource.annotation.Isolate;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.file.api.domain.dto.SysFileDto;
import com.xueyi.file.api.domain.po.SysFilePo;
import com.xueyi.file.api.domain.query.SysFileQuery;

/**
 * 文件管理 数据层
 *
 * @author xueyi
 */
@Isolate
public interface SysFileMapper extends BaseMapper<SysFileQuery, SysFileDto, SysFilePo> {
}