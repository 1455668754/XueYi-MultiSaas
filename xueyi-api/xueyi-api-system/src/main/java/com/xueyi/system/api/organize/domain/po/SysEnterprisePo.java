package com.xueyi.system.api.organize.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.web.entity.BaseEntity;

/**
 * 企业 持久化对象
 *
 * @author xueyi
 */
public class SysEnterprisePo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 策略Id */
    @TableField("strategy_id")
    private Long strategyId;

    /** 企业账号 */
    @TableField("name")
    private String name;

    /** 系统名称 */
    @TableField("system_name")
    private String systemName;

    /** 企业昵称 */
    @TableField("nick")
    private String nick;

    /** 企业logo */
    @TableField("logo")
    private String logo;

    /** 超管租户（Y是 N否） */
    @TableField("is_lessor")
    private String isLessor;

    /** 企业账号修改次数 */
    @TableField("name_frequency")
    private Long nameFrequency;

    /** 默认企业（Y是 N否） */
    @TableField("is_default")
    private String isDefault;

    public Long getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Long strategyId) {
        this.strategyId = strategyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getIsLessor() {
        return isLessor;
    }

    public void setIsLessor(String isLessor) {
        this.isLessor = isLessor;
    }

    public Long getNameFrequency() {
        return nameFrequency;
    }

    public void setNameFrequency(Long nameFrequency) {
        this.nameFrequency = nameFrequency;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
}
