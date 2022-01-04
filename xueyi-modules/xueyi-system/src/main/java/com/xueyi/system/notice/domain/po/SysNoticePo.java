package com.xueyi.system.notice.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.common.core.web.entity.BaseEntity;

/**
 * 通知公告 持久化对象
 *
 * @author xueyi
 */
public class SysNoticePo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 公告标题 */
    @TableField("title")
    private String title;

    /** 公告类型（1通知 2公告） */
    @TableField("type")
    private String type;

    /** 公告内容 */
    @TableField("content")
    private String content;

    /** 公告状态（0未发送 1已发送） */
    @TableField("status")
    private String status;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }
}
