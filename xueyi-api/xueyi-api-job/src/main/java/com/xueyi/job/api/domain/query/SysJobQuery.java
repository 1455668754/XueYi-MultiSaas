package com.xueyi.job.api.domain.query;

import com.xueyi.job.api.domain.po.SysJobPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 调度任务 数据查询对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysJobQuery extends SysJobPo {

    @Serial
    private static final long serialVersionUID = 1L;

}
