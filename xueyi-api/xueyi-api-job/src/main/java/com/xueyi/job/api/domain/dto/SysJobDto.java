package com.xueyi.job.api.domain.dto;

import com.xueyi.common.core.annotation.Correlation;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.job.api.domain.po.SysJobPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
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

    @Serial
    private static final long serialVersionUID = 1L;

    /** 任务记录数据集合 */
    @Correlation(groupName = JOB_LOG_GROUP, keyType = OperateConstants.SubKeyType.RECEIVE)
    private List<SysJobLogDto> subList;
}
