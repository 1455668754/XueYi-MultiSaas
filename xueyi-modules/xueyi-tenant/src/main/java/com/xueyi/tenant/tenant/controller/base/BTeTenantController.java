package com.xueyi.tenant.tenant.controller.base;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.system.OrganizeConstants;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.tenant.api.tenant.domain.dto.TeTenantDto;
import com.xueyi.tenant.api.tenant.domain.query.TeTenantQuery;
import com.xueyi.tenant.tenant.domain.dto.TeTenantRegister;
import com.xueyi.tenant.tenant.service.ITeTenantService;

import java.util.List;

/**
 * 租户服务 | 租户模块 | 租户管理 | 通用 业务处理
 *
 * @author xueyi
 */
public class BTeTenantController extends BaseController<TeTenantQuery, TeTenantDto, ITeTenantService> {

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "租户";
    }

    /**
     * 前置校验 （强制）增加/修改
     */
    @Override
    protected void AEHandle(BaseConstants.Operate operate, TeTenantDto tenant) {
        if (baseService.checkNameUnique(tenant.getId(), tenant.getName()))
            warn(StrUtil.format("{}{}{}失败，{}名称已存在", operate.getInfo(), getNodeName(), tenant.getName(), getNodeName()));
    }

    /**
     * 前置校验 （强制）删除
     */
    @Override
    protected void RHandle(BaseConstants.Operate operate, List<Long> idList) {
        int size = idList.size();
        for (int i = idList.size() - 1; i >= 0; i--)
            if (baseService.checkIsDefault(idList.get(i)))
                idList.remove(i);
        if (CollUtil.isEmpty(idList)) {
            warn(StrUtil.format("删除失败，默认{}不允许删除！", getNodeName()));
        } else if (idList.size() != size) {
            baseService.deleteByIds(idList);
            warn(StrUtil.format("默认{}不允许删除，其余{}删除成功！", getNodeName(), getNodeName()));
        }
    }

    /**
     * 租户新增/注册校验
     */
    protected void registerValidated(TeTenantRegister tenantRegister) {
        String enterpriseName = tenantRegister.getTenant().getName();
        String userName = tenantRegister.getUser().getUserName();
        String password = tenantRegister.getUser().getPassword();
        if (StrUtil.isBlank(enterpriseName))
            warn("企业账号必须填写");
        else if (enterpriseName.length() < OrganizeConstants.ENTERPRISE_NAME_MIN_LENGTH || enterpriseName.length() > OrganizeConstants.ENTERPRISE_NAME_MAX_LENGTH)
            warn("企业账号长度必须在" + OrganizeConstants.ENTERPRISE_NAME_MIN_LENGTH + "到" + OrganizeConstants.ENTERPRISE_NAME_MAX_LENGTH + "个字符之间");
        else if (StrUtil.hasBlank(userName, password))
            warn("用户账号/密码必须填写");
        else if (userName.length() < OrganizeConstants.USERNAME_MIN_LENGTH || userName.length() > OrganizeConstants.USERNAME_MAX_LENGTH)
            warn("用户账号长度必须在" + OrganizeConstants.USERNAME_MIN_LENGTH + "到" + OrganizeConstants.USERNAME_MAX_LENGTH + "个字符之间");
        else if (password.length() < OrganizeConstants.PASSWORD_MIN_LENGTH || password.length() > OrganizeConstants.PASSWORD_MAX_LENGTH)
            warn("用户密码长度必须在" + OrganizeConstants.PASSWORD_MIN_LENGTH + "到" + OrganizeConstants.PASSWORD_MAX_LENGTH + "个字符之间");
        else if (baseService.checkNameUnique(tenantRegister.getTenant().getId(), tenantRegister.getTenant().getName()))
            warn("企业账号已存在，请更换后再提交！");
    }
}