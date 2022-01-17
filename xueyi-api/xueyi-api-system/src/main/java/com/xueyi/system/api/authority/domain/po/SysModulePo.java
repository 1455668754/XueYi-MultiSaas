package com.xueyi.system.api.authority.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.web.tenant.TSubBaseEntity;

/**
 * 模块 持久化对象
 *
 * @param <S> SubDto
 * @author xueyi
 */
public class SysModulePo<S> extends TSubBaseEntity<S> {

    private static final long serialVersionUID = 1L;

    /** 模块logo */
    @Excel(name = "模块logo")
    @TableField("logo")
    private String logo;

    /** 路由地址 */
    @Excel(name = "路由地址")
    @TableField("path")
    private String path;

    /** 路由参数 */
    @Excel(name = "路由参数")
    @TableField("param_path")
    private String paramPath;

    /** 模块类型（0常规 1内嵌 2外链） */
    @Excel(name = "模块类型", readConverterExp = "0=常规,1=内嵌,2=外链")
    @TableField("type")
    private String type;

    /** 模块显隐状态（Y隐藏 N显示） */
    @Excel(name = "模块显隐状态", readConverterExp = "Y=隐藏,N=显示")
    @TableField("hide_module")
    private String hideModule;

    /** 公共模块（Y是 N否） */
    @Excel(name = "公共模块", readConverterExp = "Y=是,N=否")
    @TableField("is_common")
    private String isCommon;

    /** 默认模块（Y是 N否） */
    @TableField("is_default")
    private String isDefault;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getParamPath() {
        return paramPath;
    }

    public void setParamPath(String paramPath) {
        this.paramPath = paramPath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHideModule() {
        return hideModule;
    }

    public void setHideModule(String hideModule) {
        this.hideModule = hideModule;
    }

    public String getIsCommon() {
        return isCommon;
    }

    public void setIsCommon(String isCommon) {
        this.isCommon = isCommon;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
}
