package com.xueyi.job.manager.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.web.entity.manager.impl.BaseManager;
import com.xueyi.job.api.domain.dto.SysJobLogDto;
import com.xueyi.job.api.domain.model.SysJobLogConverter;
import com.xueyi.job.api.domain.po.SysJobLogPo;
import com.xueyi.job.api.domain.query.SysJobLogQuery;
import com.xueyi.job.manager.ISysJobLogManager;
import com.xueyi.job.mapper.SysJobLogMapper;
import org.springframework.stereotype.Component;

/**
 * 调度任务日志管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class SysJobLogManager extends BaseManager<SysJobLogQuery, SysJobLogDto, SysJobLogPo, SysJobLogMapper, SysJobLogConverter> implements ISysJobLogManager {

    /**
     * 清空任务日志
     */
    @Override
    public void cleanLog() {
        baseMapper.delete(Wrappers.update());
    }
}
