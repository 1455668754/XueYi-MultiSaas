package com.xueyi.system.api.log.domain.dto;

import com.xueyi.system.api.log.domain.po.SysLoginLogPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 访问日志 数据传输对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysLoginLogDto extends SysLoginLogPo {

    private static final long serialVersionUID = 1L;

}
