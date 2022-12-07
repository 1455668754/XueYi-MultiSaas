package com.xueyi.system.api.authority.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.web.tenant.common.TCBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 模块 持久化对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_module")
public class SysModulePo extends TCBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 模块logo */
    protected String logo;

    /** 路由地址 */
    protected String path;

    /** 路由参数 */
    protected String paramPath;

    /** 模块类型（0常规 1内嵌 2外链） */
    protected String type;

    /** 模块显隐状态（Y隐藏 N显示） */
    protected String hideModule;

    /** 默认模块（Y是 N否） */
    protected String isDefault;

}
