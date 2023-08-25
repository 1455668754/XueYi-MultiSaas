package com.xueyi.tenant.source.domain.correlate;

import com.xueyi.common.web.correlate.domain.BaseCorrelate;
import com.xueyi.common.web.correlate.service.CorrelateService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 数据源 关联映射
 *
 * @author xueyi
 */
@Getter
@AllArgsConstructor
public enum TeSourceCorrelate implements CorrelateService {

    ;

    private final String info;
    private final List<? extends BaseCorrelate<?>> correlates;

}
