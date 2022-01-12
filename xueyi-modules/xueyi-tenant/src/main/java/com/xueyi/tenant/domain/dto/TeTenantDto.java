package com.xueyi.tenant.domain.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.tenant.api.domain.source.dto.TeStrategyDto;
import com.xueyi.tenant.domain.po.TeTenantPo;

/**
 * 数据源 数据传输对象
 *
 * @author xueyi
 */
@TableName("te_source")
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


}