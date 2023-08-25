package com.xueyi.system.authority.domain.correlate;

import com.xueyi.common.web.correlate.domain.BaseCorrelate;
import com.xueyi.common.web.correlate.service.CorrelateService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 系统服务 | 权限模块 | 客户端 关联映射
 *
 * @author xueyi
 */
@Getter
@AllArgsConstructor
public enum SysClientCorrelate implements CorrelateService {

    ;

    private final String info;
    private final List<? extends BaseCorrelate<?>> correlates;

}
