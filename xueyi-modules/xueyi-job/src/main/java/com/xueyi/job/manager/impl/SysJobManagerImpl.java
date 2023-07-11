package com.xueyi.job.manager.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.web.annotation.TenantIgnore;
import com.xueyi.common.web.entity.domain.SlaveRelation;
import com.xueyi.common.web.entity.manager.impl.BaseManagerImpl;
import com.xueyi.job.api.domain.dto.SysJobDto;
import com.xueyi.job.domain.model.SysJobConverter;
import com.xueyi.job.api.domain.po.SysJobPo;
import com.xueyi.job.api.domain.query.SysJobQuery;
import com.xueyi.job.manager.ISysJobLogManager;
import com.xueyi.job.manager.ISysJobManager;
import com.xueyi.job.mapper.SysJobMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.xueyi.job.api.constant.MergeConstants.JOB_LOG_GROUP;

/**
 * 调度任务管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class SysJobManagerImpl extends BaseManagerImpl<SysJobQuery, SysJobDto, SysJobPo, SysJobMapper, SysJobConverter> implements ISysJobManager {

    /**
     * 初始化从属关联关系
     *
     * @return 关系对象集合
     */
    protected List<SlaveRelation> subRelationInit() {
        return new ArrayList<>(){{
            add(new SlaveRelation(JOB_LOG_GROUP, ISysJobLogManager.class, OperateConstants.SubOperateLimit.ONLY_DEL));
        }};
    }

    /**
     * 项目启动时
     */
    @Override
    @TenantIgnore
    public List<SysJobDto> initScheduler() {
        List<SysJobPo> jobList = baseMapper.selectList(Wrappers.query());
        return baseConverter.mapperDto(jobList);
    }

}
