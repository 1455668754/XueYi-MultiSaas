package com.xueyi.gen.correlate;

import com.xueyi.common.web.correlate.domain.BaseCorrelate;
import com.xueyi.common.web.correlate.service.CorrelateService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 业务 关联映射
 *
 * @author xueyi
 */
@Getter
@AllArgsConstructor
public enum GenTableCorrelate implements CorrelateService {

   ;

    private final String info;
    private final List<? extends BaseCorrelate<?>> correlates;

}
