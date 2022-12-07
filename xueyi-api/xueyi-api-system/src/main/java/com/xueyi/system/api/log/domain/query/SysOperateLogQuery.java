package com.xueyi.system.api.log.domain.query;

import com.xueyi.system.api.log.domain.po.SysOperateLogPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 操作日志 数据查询对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysOperateLogQuery extends SysOperateLogPo {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 操作时间 - 起始 */
    private LocalDateTime operateTimeStart;

    /** 操作时间 - 终止 */
    private LocalDateTime operateTimeEnd;

}
