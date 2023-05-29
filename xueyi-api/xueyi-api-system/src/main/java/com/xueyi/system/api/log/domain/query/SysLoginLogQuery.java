package com.xueyi.system.api.log.domain.query;

import com.xueyi.system.api.log.domain.po.SysLoginLogPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 系统服务 | 监控模块 | 访问日志 数据查询对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysLoginLogQuery extends SysLoginLogPo {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 访问时间 - 起始 */
    private LocalDateTime accessTimeStart;

    /** 访问时间 - 终止 */
    private LocalDateTime accessTimeEnd;

}
