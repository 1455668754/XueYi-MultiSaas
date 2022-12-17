package com.xueyi.system.api.organize.domain.dto;

import com.xueyi.common.core.annotation.Correlation;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.constant.system.AuthorityConstants;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.validate.V_A_E;
import com.xueyi.system.api.authority.domain.dto.SysRoleDto;
import com.xueyi.system.api.organize.domain.po.SysUserPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import java.io.Serial;
import java.util.List;

import static com.xueyi.system.api.organize.domain.merge.MergeGroup.USER_SysUserPostMerge_GROUP;

/**
 * 用户 数据传输对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserDto extends SysUserPo {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 岗位对象 */
    @Correlation(groupName = USER_SysUserPostMerge_GROUP, keyType = OperateConstants.SubKeyType.RECEIVE)
    private List<SysPostDto> posts;

    /** 角色对象 */
    private List<SysRoleDto> roles;

    /** 岗位组 */
    @NotEmpty(message = "归属岗位不能为空", groups = {V_A_E.class})
    @Correlation(groupName = USER_SysUserPostMerge_GROUP, keyType = OperateConstants.SubKeyType.RECEIVE_ARR)
    private Long[] postIds;

    /** 角色组 */
    private Long[] roleIds;

    /** 岗位Id - 查询 */
    private Long postId;

    /** 部门Id - 查询 */
    private Long deptId;

    public boolean isNotAdmin() {
        return !isAdmin(this.getUserType());
    }

    public boolean isAdmin() {
        return isAdmin(this.getUserType());
    }

    public static boolean isNotAdmin(String userType) {
        return !isAdmin(userType);
    }

    public static boolean isAdmin(String userType) {
        return StrUtil.equals(AuthorityConstants.UserType.ADMIN.getCode(), userType);
    }

}
