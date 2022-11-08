package com.xueyi.job.api.domain.dto;

import com.xueyi.job.api.domain.po.SysJobPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 调度任务 数据传输对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysJobDto extends SysJobPo {

    private static final long serialVersionUID = 1L;

}
