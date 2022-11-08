package com.xueyi.job.api.domain.query;

import com.xueyi.job.api.domain.po.SysJobPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 调度任务 数据查询对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysJobQuery extends SysJobPo {

    private static final long serialVersionUID = 1L;

}
