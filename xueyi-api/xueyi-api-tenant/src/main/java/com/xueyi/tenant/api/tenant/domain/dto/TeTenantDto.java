package com.xueyi.tenant.api.tenant.domain.dto;

import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.system.AuthorityConstants;
import com.xueyi.tenant.api.tenant.domain.po.TeTenantPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户 数据传输对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TeTenantDto extends TeTenantPo {

    private static final long serialVersionUID = 1L;

    /** 策略信息 */
    private TeStrategyDto strategy;

    /** 权限Ids */
    private Long[] authIds;

    /** 校验是否非租管租户 */
    public boolean isNotAdmin() {
        return !isAdmin(this.getIsLessor());
    }

    /** 校验是否为租管租户 */
    public boolean isAdmin() {
        return isAdmin(this.getIsLessor());
    }

    /** 校验是否为租管租户 */
    public static boolean isAdmin(String isLessor) {
        return StrUtil.equals(AuthorityConstants.TenantType.ADMIN.getCode(), isLessor);
    }

}