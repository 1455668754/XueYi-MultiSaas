package com.xueyi.system.monitor.domain.correlate;

import com.xueyi.common.web.correlate.domain.BaseCorrelate;
import com.xueyi.common.web.correlate.service.CorrelateService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 系统服务 | 监控模块 | 访问日志 关联映射
 *
 * @author xueyi
 */
@Getter
@AllArgsConstructor
public enum SysLoginLogCorrelate implements CorrelateService {

   ;

    private final String info;
    private final List<? extends BaseCorrelate<?>> correlates;

}
