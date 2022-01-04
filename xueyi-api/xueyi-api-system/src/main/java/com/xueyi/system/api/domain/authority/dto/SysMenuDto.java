package com.xueyi.system.api.domain.authority.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.system.api.domain.authority.po.SysMenuPo;
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

    public SysMenuDto() {
    }

    public SysMenuDto(Long Id) {
        this.setId(Id);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("moduleId", getModuleId())
                .append("parentId", getParentId())
                .append("name", getName())
                .append("ancestors", getAncestors())
                .append("path", getPath())
                .append("component", getComponent())
                .append("query", getParamPath())
                .append("transitionName", getTransitionName())
                .append("ignoreRoute", getIgnoreRoute())
                .append("isCommon", getIsCommon())
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
                .append("children", getChildren())
                .append("icon", getIcon())
                .append("sort", getSort())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createName", getCreateName())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateName", getUpdateName())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
