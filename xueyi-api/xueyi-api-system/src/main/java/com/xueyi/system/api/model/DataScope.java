package com.xueyi.system.api.model;

import com.xueyi.common.core.constant.system.AuthorityConstants;
import com.xueyi.common.core.utils.core.StrUtil;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * 数据权限 基础数据对象
 *
 * @author xueyi
 */
@Data
public class DataScope implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 企业账号Id */
    private Long enterpriseId;

    /** 租户标识 */
    private String isLessor;

    /** 用户名Id */
    private Long userId;

    /** 用户标识 */
    private String userType;

    /** 权限列表 */
    private Set<String> permissions;

    /** 角色权限列表 */
    private Set<String> roles;

    /** 角色Id列表 */
    private Set<Long> roleIds;

    /** 数据范围（1全部数据权限 2自定数据权限 3本部门数据权限 4本部门及以下数据权限 5本岗位数据权限  6仅本人数据权限） */
    private String dataScope;

    /** 权限控制 - 部门 */
    private Set<Long> deptScope;

    /** 权限控制 - 岗位 */
    private Set<Long> postScope;

    /** 权限控制 - 用户 */
    private Set<Long> userScope;

    public boolean isLessor() {
        return StrUtil.equals(AuthorityConstants.TenantType.ADMIN.getCode(), isLessor);
    }

    public boolean isAdmin() {
        return StrUtil.equals(AuthorityConstants.UserType.ADMIN.getCode(), userType);
    }
}
