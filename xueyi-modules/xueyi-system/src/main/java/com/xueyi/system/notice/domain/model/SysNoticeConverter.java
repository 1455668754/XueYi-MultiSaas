package com.xueyi.system.notice.domain.model;

import com.xueyi.common.core.web.entity.model.BaseConverter;
import com.xueyi.system.notice.domain.dto.SysNoticeDto;
import com.xueyi.system.notice.domain.po.SysNoticePo;
import com.xueyi.system.notice.domain.query.SysNoticeQuery;
import org.mapstruct.Mapper;

/**
 * 通知公告 对象映射器
 *
 * @author xueyi
 */
@Mapper(componentModel = "spring")
public interface SysNoticeConverter extends BaseConverter<SysNoticeQuery, SysNoticeDto, SysNoticePo> {
}
