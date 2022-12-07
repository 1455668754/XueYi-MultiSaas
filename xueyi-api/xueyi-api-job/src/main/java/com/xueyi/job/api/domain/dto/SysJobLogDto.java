package com.xueyi.job.api.domain.dto;

import com.xueyi.job.api.domain.po.SysJobLogPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 调度日志 数据传输对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysJobLogDto extends SysJobLogPo {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 开始时间 */
    private LocalDateTime startTime;

    /** 停止时间 */
    private LocalDateTime stopTime;

}
