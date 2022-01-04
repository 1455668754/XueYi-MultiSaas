package com.xueyi.tenant.api.domain.source.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.tenant.api.domain.source.merge.TeStrategySourceMerge;
import com.xueyi.tenant.api.domain.source.po.TeStrategyPo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 源策略 数据传输对象
 *
 * @author xueyi
 */
@TableName(value = "te_strategy")
public class TeStrategyDto extends TeStrategyPo {

    private static final long serialVersionUID = 1L;

    /** 数据源信息 */
    private List<TeStrategySourceMerge> values;

    public TeStrategyDto() {
    }

    public TeStrategyDto(Long Id) {
        setId(Id);
    }

    public List<TeStrategySourceMerge> getValues() {
        return values;
    }

    public void setValues(List<TeStrategySourceMerge> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("Id", getId())
                .append("name", getName())
                .append("values", getValues())
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