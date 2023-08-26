package com.xueyi.system.api.authority.domain.po;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xueyi.common.core.web.tenant.common.TCTreeEntity;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 系统服务 | 权限模块 | 菜单 持久化对象
 *
 * @author xueyi
 */
@Data
@TableName("sys_menu")
@EqualsAndHashCode(callSuper = true)
public class SysMenuPo extends TCTreeEntity<SysMenuDto> {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 名称 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    protected String name;

    /** 模块Id */
    @NotNull(message = "模块Id不能为空")
    protected Long moduleId;

    /** 菜单标题 | 多语言 */
    protected String title;

    /** 路由地址 */
    protected String path;

    /** 外链地址 | 仅页面类型为外链时生效 */
    protected String frameSrc;

    /** 组件路径 */
    protected String component;

    /** 路由参数 */
    protected String paramPath;

    /** 路由切换动画 */
    protected String transitionName;

    /** 是否忽略路由（Y是 N否） */
    @TableField(updateStrategy = FieldStrategy.NOT_NULL)
    protected String ignoreRoute;

    /** 是否缓存（Y是 N否） */
    @TableField(updateStrategy = FieldStrategy.NOT_NULL)
    protected String isCache;

    /** 是否固定标签（Y是 N否） */
    @TableField(updateStrategy = FieldStrategy.NOT_NULL)
    protected String isAffix;

    /** 是否禁用（Y是 N否） */
    @TableField(updateStrategy = FieldStrategy.NOT_NULL)
    protected String isDisabled;

    /** 页面类型（0常规 1内嵌 2外链） */
    @TableField(updateStrategy = FieldStrategy.NOT_NULL)
    protected String frameType;

    /** 菜单类型（M目录 C菜单 X详情 F按钮） */
    @TableField(updateStrategy = FieldStrategy.NOT_NULL)
    protected String menuType;

    /** 标签显隐状态（Y隐藏 N显示） */
    @TableField(updateStrategy = FieldStrategy.NOT_NULL)
    protected String hideTab;

    /** 菜单显隐状态（Y隐藏 N显示） */
    @TableField(updateStrategy = FieldStrategy.NOT_NULL)
    protected String hideMenu;

    /** 面包屑路由显隐状态（Y隐藏 N显示） */
    @TableField(updateStrategy = FieldStrategy.NOT_NULL)
    protected String hideBreadcrumb;

    /** 子菜单显隐状态（Y隐藏 N显示） */
    @TableField(updateStrategy = FieldStrategy.NOT_NULL)
    protected String hideChildren;

    /** 是否在子级菜单的完整path中忽略本级path（Y隐藏 N显示） */
    @TableField(updateStrategy = FieldStrategy.NOT_NULL)
    protected String hidePathForChildren;

    /** 详情页可打开Tab页数 */
    @TableField(updateStrategy = FieldStrategy.NOT_NULL)
    protected Integer dynamicLevel;

    /** 详情页的实际Path */
    protected String realPath;

    /** 权限标识 */
    protected String perms;

    /** 菜单图标 */
    protected String icon;

    /** 默认菜单（Y是 N否） */
    @TableField(updateStrategy = FieldStrategy.NOT_NULL)
    protected String isDefault;

    /** 租户Id */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    protected Long tenantId;

}
