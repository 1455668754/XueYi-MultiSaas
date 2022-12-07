package com.xueyi.job.api.domain.query;

import com.xueyi.job.api.domain.po.SysJobLogPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 调度日志 数据查询对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysJobLogQuery extends SysJobLogPo {

    @Serial
    private static final long serialVersionUID = 1L;

}
