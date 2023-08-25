package com.xueyi.system.notice.correlate;

import com.xueyi.common.web.correlate.domain.BaseCorrelate;
import com.xueyi.common.web.correlate.service.CorrelateService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 系统服务 | 消息模块 | 通知公告 关联映射
 *
 * @author xueyi
 */
@Getter
@AllArgsConstructor
public enum SysNoticeCorrelate implements CorrelateService {

    ;

    private final String info;
    private final List<? extends BaseCorrelate<?>> correlates;

}
