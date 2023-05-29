package com.xueyi.system.api.log.domain.po;

import com.baomidou.mybatisplus.annotation.OrderBy;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.web.tenant.base.TBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 系统服务 | 监控模块 | 操作日志 持久化对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_operate_log",excludeProperty = {"name","sort","createBy","createTime","updateBy","updateTime","remark"})
public class SysOperateLogPo extends TBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 操作模块 */
    protected String title;

    /** 业务类型（0其它 1新增 2修改 3删除） */
    protected String businessType;

    /** 请求方法 */
    protected String method;

    /** 请求方式 */
    protected String requestMethod;

    /** 操作类别（0其它 1后台用户 2手机端用户） */
    protected String operateType;

    /** 操作Id */
    protected Long userId;

    /** 操作人员账号 */
    protected String userName;

    /** 操作人员名称 */
    protected String userNick;

    /** 请求url */
    protected String url;

    /** 操作地址 */
    protected String ip;

    /** 请求参数 */
    protected String param;

    /** 请求参数 */
    protected String location;
    
    /** 返回参数 */
    protected String jsonResult;

    /** 操作状态（0正常 1异常） */
    protected String status;

    /** 错误消息 */
    protected String errorMsg;

    /** 操作时间 */
    @OrderBy(sort = 10)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime operateTime;

    /** 消耗时间 */
    @Excel(name = "消耗时间", suffix = "毫秒")
    private Long costTime;
}
