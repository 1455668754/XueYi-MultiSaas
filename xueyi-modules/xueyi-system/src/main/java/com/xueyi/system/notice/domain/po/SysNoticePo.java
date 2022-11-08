package com.xueyi.system.notice.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通知公告 持久化对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_notice",excludeProperty = {"sort"})
public class SysNoticePo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 公告类型（0通知 1公告） */
    @Excel(name = "公告类型", readConverterExp = "0=通知,1=公告")
    protected String type;

    /** 公告内容 */
    @Excel(name = "公告内容")
    protected String content;

    /** 公告状态（0未发送 1已发送） */
    @Excel(name = "公告状态", readConverterExp = "0=未发送,1=已发送")
    protected String status;

}
