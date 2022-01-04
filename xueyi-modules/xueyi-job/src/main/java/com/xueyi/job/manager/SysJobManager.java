package com.xueyi.job.manager;

import com.xueyi.common.web.entity.manager.BaseManager;
import com.xueyi.job.domain.dto.SysJobDto;
import com.xueyi.job.mapper.SysJobMapper;
import org.springframework.stereotype.Component;

/**
 * 调度任务管理 数据封装层
 *
 * @author xueyi
 */
@Component
public class SysJobManager extends BaseManager<SysJobDto, SysJobMapper> {
}
