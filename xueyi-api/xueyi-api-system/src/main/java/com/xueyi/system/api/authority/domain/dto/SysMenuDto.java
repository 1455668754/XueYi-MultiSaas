package com.xueyi.system.api.authority.domain.dto;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.constant.AuthorityConstants;
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

    /** 菜单全路径 */
    @TableField(exist = false)
    private String fullPath;

    /** 详情页激活的菜单 */
    @TableField(exist = false)
    private String currentActiveMenu;

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public void setCurrentActiveMenu(String currentActiveMenu) {
        this.currentActiveMenu = currentActiveMenu;
    }

    public String getCurrentActiveMenu() {
        return currentActiveMenu;
    }

    /**
     * 校验菜单类型是否为详情
     */
    public boolean isDetails(){
        return StrUtil.equals(AuthorityConstants.MenuType.DETAILS.getCode(), getMenuType());
    }

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
                .append("fullPath", getFullPath())
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
