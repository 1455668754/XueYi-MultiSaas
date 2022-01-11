package com.xueyi.system.api.log.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.annotation.Excel;
import com.xueyi.system.api.log.domain.po.SysOperationLogPo;

/**
 * 操作日志 数据传输对象
 *
 * @author xueyi
 */
@TableName(value = "sys_operation_log",excludeProperty = {"name","sort","createBy","createTime","updateBy","updateTime","remark"})
public class SysOperationLogDto extends SysOperationLogPo {

    private static final long serialVersionUID = 1L;

    /** 业务类型数组 */
    @TableField(exist = false)
    private Integer[] businessTypes;

    /** 操作账号 */
    @Excel(name = "操作账号")
    @TableField(exist = false)
    private String userName;

    /** 操作人员 */
    @Excel(name = "操作人员")
    @TableField(exist = false)
    private String userNick;

    public Integer[] getBusinessTypes() {
        return businessTypes;
    }

    public void setBusinessTypes(Integer[] businessTypes) {
        this.businessTypes = businessTypes;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }
}
