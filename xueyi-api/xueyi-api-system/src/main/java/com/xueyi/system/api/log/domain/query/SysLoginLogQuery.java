package com.xueyi.system.api.log.domain.query;

import com.xueyi.system.api.log.domain.po.SysLoginLogPo;

import java.time.LocalDateTime;

/**
 * 访问日志 数据查询对象
 *
 * @author xueyi
 */
public class SysLoginLogQuery extends SysLoginLogPo {

    private static final long serialVersionUID = 1L;

    /** 访问时间 - 起始 */
    private LocalDateTime accessTimeStart;

    /** 访问时间 - 终止 */
    private LocalDateTime accessTimeEnd;

    public LocalDateTime getAccessTimeStart() {
        return accessTimeStart;
    }

    public void setAccessTimeStart(LocalDateTime accessTimeStart) {
        this.accessTimeStart = accessTimeStart;
    }

    public LocalDateTime getAccessTimeEnd() {
        return accessTimeEnd;
    }

    public void setAccessTimeEnd(LocalDateTime accessTimeEnd) {
        this.accessTimeEnd = accessTimeEnd;
    }

}
