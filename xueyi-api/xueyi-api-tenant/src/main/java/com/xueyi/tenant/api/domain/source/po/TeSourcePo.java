package com.xueyi.tenant.api.domain.source.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.common.core.web.entity.TreeEntity;

/**
 * 数据源 持久化对象
 *
 * @author xueyi
 */
public class TeSourcePo<D> extends TreeEntity<D> {

    private static final long serialVersionUID = 1L;

    /** 数据源编码 */
    @TableField("slave")
    private String slave;

    /** 驱动 */
    @TableField("driver_class_name")
    private String driverClassName;

    /** 连接地址 */
    @TableField("url_prepend")
    private String urlPrepend;

    /** 连接参数 */
    @TableField("url_append")
    private String urlAppend;

    /** 用户名 */
    @TableField("username")
    private String username;

    /** 密码 */
    @TableField("password")
    private String password;

    /** 读写类型（0读&写 1只读 2只写） */
    @TableField("type")
    private String type;

    /** 默认数据源（Y是 N否） */
    @TableField("is_default")
    private String isDefault;
    
    public String getSlave() {
        return slave;
    }

    public void setSlave(String slave) {
        this.slave = slave;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrlPrepend() {
        return urlPrepend;
    }

    public void setUrlPrepend(String urlPrepend) {
        this.urlPrepend = urlPrepend;
    }

    public String getUrlAppend() {
        return urlAppend;
    }

    public void setUrlAppend(String urlAppend) {
        this.urlAppend = urlAppend;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
}