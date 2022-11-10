package com.xueyi.system.notice.manager.impl;

import com.xueyi.common.web.entity.manager.impl.BaseManagerImpl;
import com.xueyi.system.notice.domain.dto.SysNoticeDto;
import com.xueyi.system.notice.domain.model.SysNoticeConverter;
import com.xueyi.system.notice.domain.po.SysNoticePo;
import com.xueyi.system.notice.domain.query.SysNoticeQuery;
import com.xueyi.system.notice.manager.ISysNoticeManager;
import com.xueyi.system.notice.mapper.SysNoticeMapper;
import org.springframework.stereotype.Component;

/**
 * 通知公告管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class SysNoticeManagerImpl extends BaseManagerImpl<SysNoticeQuery, SysNoticeDto, SysNoticePo, SysNoticeMapper, SysNoticeConverter> implements ISysNoticeManager {
}
