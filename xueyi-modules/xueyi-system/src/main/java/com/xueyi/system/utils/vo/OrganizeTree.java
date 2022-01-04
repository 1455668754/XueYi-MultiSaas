package com.xueyi.system.utils.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xueyi.common.core.constant.OrganizeConstants;
import com.xueyi.system.api.domain.organize.dto.SysDeptDto;
import com.xueyi.system.api.domain.organize.dto.SysPostDto;

import java.util.List;

/**
 * 组织对象 合成通用结构
 *
 * @author xueyi
 */
public class OrganizeTree {

    /** Id */
    private Long id;

    /** 父级Id（岗位父Id为归属部门） */
    private Long parentId;

    /** 名称 */
    private String label;

    /** 状态 */
    private String status;

    /** 类型（0 部门 1 岗位） */
    private String type;

    /** 子部门/岗位 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<OrganizeTree> children;

    public OrganizeTree() {
    }

    /**
     * 部门转换
     */
    public OrganizeTree(SysDeptDto dept) {
        this.id = dept.getId();
        this.parentId = dept.getParentId();
        this.label = "部门 | " + dept.getName();
        this.status = dept.getStatus();
        this.type = OrganizeConstants.OrganizeType.DEPT.getCode();
    }

    /**
     * 岗位转换 | 岗位的父级设置为部门
     */
    public OrganizeTree(SysPostDto post) {
        this.id = post.getId();
        this.parentId = post.getDeptId();
        this.label = "岗位 | " + post.getName();
        this.status = post.getStatus();
        this.type = OrganizeConstants.OrganizeType.POST.getCode();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<OrganizeTree> getChildren() {
        return children;
    }

    public void setChildren(List<OrganizeTree> children) {
        this.children = children;
    }
}
