package com.xueyi.system.authority.domain.vo;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.system.AuthorityConstants;
import com.xueyi.common.core.web.entity.base.BasisEntity;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.organize.domain.vo.SysOrganizeTree;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * 权限对象 通用结构
 *
 * @author xueyi
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysAuthTree extends BasisEntity {

    /** Id */
    private Long id;

    /** 父级Id */
    private Long parentId;

    /** 名称 */
    private String label;

    /** 状态 */
    private String status;

    /** 类型（0 模块 1 菜单） */
    private String type;

    /** 子部门/岗位 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<SysOrganizeTree> children;

    /**
     * 模块转换
     */
    public SysAuthTree(SysModuleDto module) {
        this.id = module.getId();
        this.parentId = BaseConstants.TOP_ID;
        this.label = module.getName();
        this.status = module.getStatus();
        this.type = AuthorityConstants.AuthorityType.MODULE.getCode();
    }

    /**
     * 菜单转换
     */
    public SysAuthTree(SysMenuDto menu) {
        this.id = menu.getId();
        this.parentId = ObjectUtil.equals(menu.getParentId(), AuthorityConstants.MENU_TOP_NODE) ? menu.getModuleId(): menu.getParentId();
        this.label = menu.getTitle();
        this.status = menu.getStatus();
        this.type = AuthorityConstants.AuthorityType.MENU.getCode();
    }
}
