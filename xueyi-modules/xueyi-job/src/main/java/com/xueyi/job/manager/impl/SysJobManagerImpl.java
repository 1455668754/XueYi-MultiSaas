package com.xueyi.job.manager.impl;

import com.xueyi.common.core.utils.core.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.web.annotation.TenantIgnore;
import com.xueyi.common.web.entity.manager.impl.SubBaseManagerImpl;
import com.xueyi.job.api.domain.dto.SysJobDto;
import com.xueyi.job.api.domain.dto.SysJobLogDto;
import com.xueyi.job.api.domain.model.SysJobConverter;
import com.xueyi.job.api.domain.model.SysJobLogConverter;
import com.xueyi.job.api.domain.po.SysJobLogPo;
import com.xueyi.job.api.domain.po.SysJobPo;
import com.xueyi.job.api.domain.query.SysJobLogQuery;
import com.xueyi.job.api.domain.query.SysJobQuery;
import com.xueyi.job.manager.ISysJobManager;
import com.xueyi.job.mapper.SysJobLogMapper;
import com.xueyi.job.mapper.SysJobMapper;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * 调度任务管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class SysJobManagerImpl extends SubBaseManagerImpl<SysJobQuery, SysJobDto, SysJobPo, SysJobMapper, SysJobConverter, SysJobLogQuery, SysJobLogDto, SysJobLogPo, SysJobLogMapper, SysJobLogConverter> implements ISysJobManager {

    /**
     * 项目启动时
     */
    @Override
    @TenantIgnore(tenantLine = true)
    public List<SysJobDto> initScheduler() {
        List<SysJobPo> jobList = baseMapper.selectList(Wrappers.query());
        return baseConverter.mapperDto(jobList);
    }

    /**
     * 设置主子表中子表外键值
     */
    @Override
    protected void setForeignKey(LambdaQueryWrapper<SysJobLogPo> queryWrapper, LambdaUpdateWrapper<SysJobLogPo> updateWrapper, SysJobDto job, Serializable key) {
        Serializable jobGroup = ObjectUtil.isNotNull(job) ? job.getJobGroup() : key;
        if (ObjectUtil.isNotNull(queryWrapper))
            queryWrapper.eq(SysJobLogPo::getJobGroup, jobGroup);
        else
            updateWrapper.eq(SysJobLogPo::getJobGroup, jobGroup);
    }
}
