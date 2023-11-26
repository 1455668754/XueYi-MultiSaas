package com.xueyi.common.core.web.model;

import com.xueyi.common.core.constant.system.AuthorityConstants;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.xss.Xss;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 企业 基础数据对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysEnterprise extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 策略Id */
    protected Long strategyId;

    /** 名称 */
    protected String name;

    /** 系统名称 */
    protected String systemName;

    /** 企业名称 */
    protected String nick;

    /** 企业logo */
    protected String logo;

    /** 超管租户（Y是 N否） */
    protected String isLessor;

    /** 企业账号修改次数 */
    protected Long nameFrequency;

    /** 默认企业（Y是 N否） */
    protected String isDefault;

    /**
     * 企业自定义域名
     */
    @Xss(message = "企业自定义域名不能包含脚本字符")
    @Size(max = 30, message = "企业自定义域名长度不能超过30个字符")
    protected  String doMain;

    public static boolean isNotLessor(String userType) {
        return !isLessor(userType);
    }

    public boolean isLessor() {
        return isLessor(getIsLessor());
    }

    public static boolean isLessor(String isLessor) {
        return StrUtil.equals(AuthorityConstants.TenantType.ADMIN.getCode(), isLessor);
    }
}