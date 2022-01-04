package com.xueyi.tenant.api.domain.source.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.tenant.api.domain.source.po.TeSourcePo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 数据源 数据传输对象
 *
 * @author xueyi
 */
@TableName(value = "te_source")
public class TeSourceDto extends TeSourcePo<TeSourceDto> {

    private static final long serialVersionUID = 1L;

    /** 地址 */
    private String url;

    /** 源同步策略（0不变 1刷新 2启动 3删除） */
    private String syncType;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSyncType() {
        return syncType;
    }

    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("Id", getId())
                .append("name", getName())
                .append("slave", getSlave())
                .append("driverClassName", getDriverClassName())
                .append("url", getUrl())
                .append("urlPrepend", getUrlPrepend())
                .append("urlAppend", getUrlAppend())
                .append("username", getUsername())
                .append("password", getPassword())
                .append("children", getChildren())
                .append("type", getType())
                .append("sort", getSort())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createName", getCreateName())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateName", getUpdateName())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}