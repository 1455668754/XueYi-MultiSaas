package com.xueyi.system.dict.mapper;

import com.xueyi.common.datasource.annotation.Master;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.api.dict.domain.dto.SysImExDto;
import com.xueyi.system.api.dict.domain.po.SysImExPo;
import com.xueyi.system.api.dict.domain.query.SysImExQuery;

/**
 * 导入导出配置管理 数据层
 *
 * @author xueyi
 */
@Master
public interface SysImExMapper extends BaseMapper<SysImExQuery, SysImExDto, SysImExPo> {
}