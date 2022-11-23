package com.xueyi.job.api.domain.dto;

import com.xueyi.common.core.annotation.SubRelation;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.job.api.domain.po.SysJobPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

import static com.xueyi.job.api.constant.MergeConstants.JOB_LOG_GROUP;

/**
 * 调度任务 数据传输对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysJobDto extends SysJobPo {

    private static final long serialVersionUID = 1L;

    /** 任务记录数据集合 */
    @SubRelation(groupName = JOB_LOG_GROUP, keyType = OperateConstants.SubKeyType.RECEIVE_KEY)
    private List<SysJobLogDto> subList;
}
