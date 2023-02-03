package com.xueyi.system.authority.mapper;

import com.xueyi.common.datasource.annotation.Master;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.api.authority.domain.dto.SysClientDto;
import com.xueyi.system.api.authority.domain.po.SysClientPo;
import com.xueyi.system.api.authority.domain.query.SysClientQuery;

/**
 * 客户端管理 数据层
 *
 * @author xueyi
 */
@Master
public interface SysClientMapper extends BaseMapper<SysClientQuery, SysClientDto, SysClientPo> {
}
