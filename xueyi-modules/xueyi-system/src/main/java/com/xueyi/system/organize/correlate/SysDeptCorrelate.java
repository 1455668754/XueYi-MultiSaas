package com.xueyi.system.organize.correlate;

import com.xueyi.common.web.correlate.domain.BaseCorrelate;
import com.xueyi.common.web.correlate.service.CorrelateService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 系统服务 | 组织模块 | 部门 关联映射
 */
@Getter
@AllArgsConstructor
public enum SysDeptCorrelate implements CorrelateService {

    ;

    private final String info;
    private final List<? extends BaseCorrelate<?>> correlates;

}