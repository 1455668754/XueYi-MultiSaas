package com.xueyi.tenant.domain.po;

import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.web.entity.BaseEntity;

/**
 * 租户信息对象 xy_tenant
 *
 * @author xueyi
 */
public class TeTenantPo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 策略Id */
    private Long strategyId;

    /** 系统名称 */
    @Excel(name = "系统名称")
    private String systemName;

    /** 租户名称 */
    @Excel(name = "租户名称")
    private String nick;

    /** 租户logo */
    @Excel(name = "租户logo")
    private String logo;

    /** 租户账号修改次数 */
    @Excel(name = "租户账号修改次数")
    private Long nameFrequency;

    /** 超管租户（Y是 N否） */
    private String isLessor;

    public Long getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Long strategyId) {
        this.strategyId = strategyId;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Long getNameFrequency() {
        return nameFrequency;
    }

    public void setNameFrequency(Long nameFrequency) {
        this.nameFrequency = nameFrequency;
    }

    public String getIsLessor() {
        return isLessor;
    }

    public void setIsLessor(String isLessor) {
        this.isLessor = isLessor;
    }
}