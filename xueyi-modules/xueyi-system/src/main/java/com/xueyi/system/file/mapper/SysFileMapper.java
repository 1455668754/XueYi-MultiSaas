package com.xueyi.system.file.mapper;

import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.file.domain.dto.SysFileDto;
import com.xueyi.system.file.domain.po.SysFilePo;
import com.xueyi.system.file.domain.query.SysFileQuery;

/**
 * 文件管理 数据层
 *
 * @author xueyi
 */
public interface SysFileMapper extends BaseMapper<SysFileQuery, SysFileDto, SysFilePo> {
}