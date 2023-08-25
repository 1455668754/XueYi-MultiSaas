package com.xueyi.system.organize.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xueyi.common.core.constant.system.OrganizeConstants;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 系统服务 | 组织模块 | 组织 合成通用结构对象
 *
 * @author xueyi
 */
@Data
@NoArgsConstructor
public class SysOrganizeTree {

    /** Id */
    private Long id;

    /** 父级Id（岗位父Id为归属部门） */
    private Long parentId;

    /** 名称 */
    private String label;

    /** 状态（0正常 1停用） */
    private String status;

    /** 类型（0企业 1部门 2岗位 3用户） */
    private String type;

    /** 子部门/岗位 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<SysOrganizeTree> children;

    /**
     * 部门转换
     */
    public SysOrganizeTree(SysDeptDto dept) {
        this.id = dept.getId();
        this.parentId = dept.getParentId();
        this.label = "部门 | " + dept.getName();
        this.status = dept.getStatus();
        this.type = OrganizeConstants.OrganizeType.DEPT.getCode();
    }

    /**
     * 岗位转换 | 岗位的父级设置为部门
     */
    public SysOrganizeTree(SysPostDto post) {
        this.id = post.getId();
        this.parentId = post.getDeptId();
        this.label = "岗位 | " + post.getName();
        this.status = post.getStatus();
        this.type = OrganizeConstants.OrganizeType.POST.getCode();
    }
}
