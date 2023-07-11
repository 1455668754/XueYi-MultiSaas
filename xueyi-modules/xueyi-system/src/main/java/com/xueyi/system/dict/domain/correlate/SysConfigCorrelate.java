package com.xueyi.system.dict.domain.correlate;

import com.xueyi.common.web.correlate.domain.BaseCorrelate;
import com.xueyi.common.web.correlate.service.CorrelateService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 系统服务 | 字典模块 | 参数 关联映射
 *
 * @author xueyi
 */
@Getter
@AllArgsConstructor
public enum SysConfigCorrelate implements CorrelateService {

   ;

    private final String info;
    private final List<? extends BaseCorrelate<?>> correlates;

}
