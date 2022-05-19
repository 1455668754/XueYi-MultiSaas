package com.xueyi.system.notice.mapper;

import com.xueyi.common.datasource.annotation.Isolate;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.system.notice.domain.dto.SysNoticeDto;
import com.xueyi.system.notice.domain.po.SysNoticePo;
import com.xueyi.system.notice.domain.query.SysNoticeQuery;

/**
 * 通知公告管理 数据层
 *
 * @author xueyi
 */
@Isolate
public interface SysNoticeMapper extends BaseMapper<SysNoticeQuery, SysNoticeDto, SysNoticePo> {
}
