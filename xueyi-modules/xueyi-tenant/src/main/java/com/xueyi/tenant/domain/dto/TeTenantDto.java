package com.xueyi.tenant.domain.dto;

import com.xueyi.tenant.api.domain.source.dto.TeStrategyDto;
import com.xueyi.tenant.domain.po.TeTenantPo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 租户信息对象 xy_tenant
 *
 * @author xueyi
 */
public class TeTenantDto extends TeTenantPo {

    private static final long serialVersionUID = 1L;

    /** 策略信息 */
    private TeStrategyDto strategy;

    /** 系统-菜单组（菜单权限） */
    private Long[] systemMenuIds;

    public TeTenantDto() {
    }

    public TeTenantDto(Long id) {
        this.setId(id);
    }

    public TeStrategyDto getStrategy() {
        return strategy;
    }

    public void setStrategy(TeStrategyDto strategy) {
        this.strategy = strategy;
    }

    public Long[] getSystemMenuIds() {
        return systemMenuIds;
    }

    public void setSystemMenuIds(Long[] systemMenuIds) {
        this.systemMenuIds = systemMenuIds;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("Id",getId())
                .append("strategyId",getStrategyId())
                .append("name", getName())
                .append("systemName", getSystemName())
                .append("nick", getNick())
                .append("logo", getLogo())
                .append("nameFrequency", getNameFrequency())
                .append("isLessor", getIsLessor())
                .append("strategy", getStrategy())
                .append("sort", getSort())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createName", getCreateName())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateName", getUpdateName())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}