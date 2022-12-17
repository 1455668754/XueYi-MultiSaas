package com.xueyi.system.api.authority.domain.po;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.annotation.Correlation;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.web.tenant.common.TCTreeEntity;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serial;

import static com.xueyi.system.api.authority.domain.merge.MergeGroup.MODULE_SysMenu_GROUP;

/**
 * 菜单 持久化对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
public class SysMenuPo extends TCTreeEntity<SysMenuDto> {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 模块Id */
    @NotNull(message = "模块Id不能为空")
    @Correlation(groupName = MODULE_SysMenu_GROUP, keyType = OperateConstants.SubKeyType.SLAVE)
    protected Long moduleId;

    /** 菜单标题 | 多语言 */
    protected String title;

    /** 路由地址 */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    protected String path;

    /** 外链地址 | 仅页面类型为外链时生效 */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    protected String frameSrc;

    /** 组件路径 */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    protected String component;

    /** 路由参数 */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    protected String paramPath;

    /** 路由切换动画 */
    protected String transitionName;

    /** 是否忽略路由（Y是 N否） */
    protected String ignoreRoute;

    /** 是否缓存（Y是 N否） */
    protected String isCache;

    /** 是否固定标签（Y是 N否） */
    protected String isAffix;

    /** 是否禁用（Y是 N否） */
    protected String isDisabled;

    /** 页面类型（0常规 1内嵌 2外链） */
    protected String frameType;

    /** 菜单类型（M目录 C菜单 X详情 F按钮） */
    protected String menuType;

    /** 标签显隐状态（Y隐藏 N显示） */
    protected String hideTab;

    /** 菜单显隐状态（Y隐藏 N显示） */
    protected String hideMenu;

    /** 面包屑路由显隐状态（Y隐藏 N显示） */
    protected String hideBreadcrumb;

    /** 子菜单显隐状态（Y隐藏 N显示） */
    protected String hideChildren;

    /** 是否在子级菜单的完整path中忽略本级path（Y隐藏 N显示） */
    protected String hidePathForChildren;

    /** 详情页可打开Tab页数 */
    protected Integer dynamicLevel;

    /** 详情页的实际Path */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    protected String realPath;

    /** 权限标识 */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    protected String perms;

    /** 菜单图标 */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    protected String icon;

    /** 默认菜单（Y是 N否） */
    protected String isDefault;

}
