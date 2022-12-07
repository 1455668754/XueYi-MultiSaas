package com.xueyi.system.notice.domain.dto;

import com.xueyi.system.notice.domain.po.SysNoticePo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 通知公告 数据传输对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysNoticeDto extends SysNoticePo {

    @Serial
    private static final long serialVersionUID = 1L;

}
