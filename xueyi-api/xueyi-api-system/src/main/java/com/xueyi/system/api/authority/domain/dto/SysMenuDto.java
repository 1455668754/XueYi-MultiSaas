package com.xueyi.system.api.authority.domain.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.system.api.authority.domain.po.SysMenuPo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 菜单 数据传输对象
 *
 * @author xueyi
 */
@TableName("sys_menu")
public class SysMenuDto extends SysMenuPo<SysMenuDto> {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("parentId", getParentId())
                .append("parentName", getParentName())
                .append("name", getName())
                .append("title", getTitle())
                .append("ancestors", getAncestors())
                .append("path", getPath())
                .append("frameSrc", getFrameSrc())
                .append("component", getComponent())
                .append("paramPath", getParamPath())
                .append("transitionName", getTransitionName())
                .append("ignoreRoute", getIgnoreRoute())
                .append("isCache", getIsCache())
                .append("isAffix", getIsAffix())
                .append("isDisabled", getIsDisabled())
                .append("frameType", getFrameType())
                .append("menuType", getMenuType())
                .append("hideTab", getHideTab())
                .append("hideMenu", getHideMenu())
                .append("hideBreadcrumb", getHideBreadcrumb())
                .append("hideChildren", getHideChildren())
                .append("hidePathForChildren", getHidePathForChildren())
                .append("dynamicLevel", getDynamicLevel())
                .append("realPath", getRealPath())
                .append("currentActiveMenu", getCurrentActiveMenu())
                .append("perms", getPerms())
                .append("icon", getIcon())
                .append("sort", getSort())
                .append("status", getStatus())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createName", getCreateName())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateName", getUpdateName())
                .append("updateTime", getUpdateTime())
                .append("isCommon", getIsCommon())
                .append("isDefault", getIsDefault())
                .append("moduleId", getModuleId())
                .append("children", getChildren())
                .toString();
    }
}
