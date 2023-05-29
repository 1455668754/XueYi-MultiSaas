package com.xueyi.system.api.model;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.web.model.BaseLoginUser;
import com.xueyi.system.api.application.domain.dto.SysApplicationDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 平台端 - 平台信息
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LoginPlatform extends BaseLoginUser<SysApplicationDto> {

    @Serial
    private static final long serialVersionUID = 1L;

    /** appId */
    private String appId;

    /** 账户类型 */
    private SecurityConstants.AccountType accountType = SecurityConstants.AccountType.PLATFORM;

}
