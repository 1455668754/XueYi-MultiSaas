package com.xueyi.job.domain.correlate;

import com.xueyi.common.web.correlate.domain.BaseCorrelate;
import com.xueyi.common.web.correlate.domain.Direct;
import com.xueyi.common.web.correlate.service.CorrelateService;
import com.xueyi.job.api.domain.dto.SysJobDto;
import com.xueyi.job.api.domain.dto.SysJobLogDto;
import com.xueyi.job.service.ISysJobLogService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.xueyi.common.web.correlate.contant.CorrelateConstants.SubOperate.DELETE;

/**
 * 调度任务 关联映射
 *
 * @author xueyi
 */
@Getter
@AllArgsConstructor
public enum SysJobCorrelate implements CorrelateService {

    BASE_DEL("默认删除|（调度日志）", new ArrayList<>() {{
        // 调度任务 | 调度日志
        add(new Direct<>(DELETE, ISysJobLogService.class, SysJobDto::getId, SysJobLogDto::getJobId));
    }});

    private final String info;
    private final List<? extends BaseCorrelate<?>> correlates;

}
