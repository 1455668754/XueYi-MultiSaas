package com.xueyi.tenant.api.source.domain.dto;

import com.xueyi.tenant.api.source.domain.po.TeSourcePo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 数据源 数据传输对象
 *
 * @author xueyi
 */
public class TeSourceDto extends TeSourcePo {

    private static final long serialVersionUID = 1L;

    /** 源同步策略（0不变 1刷新 2启动 3删除） */
    private String syncType;

    public String getSyncType() {
        return syncType;
    }

    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("slave", getSlave())
            .append("driverClassName", getDriverClassName())
            .append("urlPrepend", getUrlPrepend())
            .append("urlAppend", getUrlAppend())
            .append("username", getUsername())
            .append("password", getPassword())
            .append("sort", getSort())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createName", getCreateName())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateName", getUpdateName())
            .append("updateTime", getUpdateTime())
            .append("isDefault", getIsDefault())
            .toString();
    }
}