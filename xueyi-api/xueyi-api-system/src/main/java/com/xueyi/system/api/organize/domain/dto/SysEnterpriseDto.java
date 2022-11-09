package com.xueyi.system.api.organize.domain.dto;

import com.xueyi.common.core.constant.system.AuthorityConstants;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.system.api.organize.domain.po.SysEnterprisePo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 企业 数据传输对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysEnterpriseDto extends SysEnterprisePo {

    private static final long serialVersionUID = 1L;

    public boolean isAdmin() {
        return isAdmin(getIsLessor());
    }

    public static boolean isAdmin(String isLessor) {
        return StrUtil.equals(AuthorityConstants.TenantType.ADMIN.getCode(), isLessor);
    }

}
