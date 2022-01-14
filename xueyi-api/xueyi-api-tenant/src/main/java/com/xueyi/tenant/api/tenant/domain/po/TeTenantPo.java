package com.xueyi.tenant.api.tenant.domain.po;

import com.xueyi.common.core.web.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.common.core.annotation.Excel;

/**
 * 租户 持久化对象
 *
 * @author xueyi
 */
public class TeTenantPo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 策略Id */
    @Excel(name = "策略Id")
    @TableField("strategy_id")
    private Long strategyId;

    /** 系统名称 */
    @Excel(name = "系统名称")
    @TableField("system_name")
    private String systemName;

    /** 租户名称 */
    @Excel(name = "租户名称")
    @TableField("nick")
    private String nick;

    /** 租户logo */
    @Excel(name = "租户logo")
    @TableField("logo")
    private String logo;

    /** 账号修改次数 */
    @Excel(name = "账号修改次数")
    @TableField("name_frequency")
    private Integer nameFrequency;

    /** 超管租户（Y是 N否） */
    @Excel(name = "超管租户", readConverterExp = "Y=是,N=否")
    @TableField("is_lessor")
    private String isLessor;

    /** 默认租户（Y是 N否） */
    @Excel(name = "默认租户", readConverterExp = "Y=是,N=否")
    @TableField("is_default")
    private String isDefault;

    public void setStrategyId(Long strategyId) {
        this.strategyId = strategyId;
    }

    public Long getStrategyId() {
        return strategyId;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getNick() {
        return nick;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLogo() {
        return logo;
    }

    public void setNameFrequency(Integer nameFrequency) {
        this.nameFrequency = nameFrequency;
    }

    public Integer getNameFrequency() {
        return nameFrequency;
    }

    public void setIsLessor(String isLessor) {
        this.isLessor = isLessor;
    }

    public String getIsLessor() {
        return isLessor;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getIsDefault() {
        return isDefault;
    }

}