package com.xueyi.tenant.tenant.domain.dto;

import com.xueyi.tenant.api.domain.source.dto.TeStrategyDto;
import com.xueyi.tenant.tenant.domain.po.TeTenantPo;
import com.baomidou.mybatisplus.annotation.TableName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 租户 数据传输对象
 *
 * @author xueyi
 */
@TableName("te_tenant")
public class TeTenantDto extends TeTenantPo {

    private static final long serialVersionUID = 1L;

    /** 策略信息 */
    private TeStrategyDto strategy;

    public TeStrategyDto getStrategy() {
        return strategy;
    }

    public void setStrategy(TeStrategyDto strategy) {
        this.strategy = strategy;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("strategyId", getStrategyId())
                .append("strategy", getStrategy())
                .append("name", getName())
                .append("systemName", getSystemName())
                .append("nick", getNick())
                .append("logo", getLogo())
                .append("nameFrequency", getNameFrequency())
                .append("isLessor", getIsLessor())
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