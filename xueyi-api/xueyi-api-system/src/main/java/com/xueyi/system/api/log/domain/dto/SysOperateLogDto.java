package com.xueyi.system.api.log.domain.dto;

import com.xueyi.system.api.log.domain.po.SysOperateLogPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 系统服务 | 监控模块 | 操作日志    数据传输对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysOperateLogDto extends SysOperateLogPo {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 业务类型数组 */
    private Integer[] businessTypes;

}
