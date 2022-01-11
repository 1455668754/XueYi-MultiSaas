package com.xueyi.system.authority.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.BaseConstants;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.api.authority.domain.dto.SysRoleDto;
import com.xueyi.system.authority.manager.SysRoleManager;
import com.xueyi.system.authority.mapper.SysRoleMapper;
import com.xueyi.system.authority.service.ISysRoleService;
import org.springframework.stereotype.Service;

import static com.xueyi.common.core.constant.TenantConstants.ISOLATE;

/**
 * 角色管理 服务层处理
 *
 * @author xueyi
 */
@Service
@DS(ISOLATE)
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleDto, SysRoleManager, SysRoleMapper> implements ISysRoleService {


    /**
     * 校验角色编码是否唯一
     *
     * @param Id   角色Id
     * @param code 角色编码
     * @return 结果 | true/false 唯一/不唯一
     */
    @Override
    public boolean checkRoleCodeUnique(Long Id, String code) {
        return ObjectUtil.isNotNull(baseManager.checkRoleCodeUnique(ObjectUtil.isNull(Id) ? BaseConstants.NONE_ID : Id, code));
    }

    /**
     * 校验角色权限是否唯一
     *
     * @param Id      角色Id
     * @param roleKey 角色权限
     * @return 结果 | true/false 唯一/不唯一
     */
    @Override
    public boolean checkRoleKeyUnique(Long Id, String roleKey) {
        return ObjectUtil.isNotNull(baseManager.checkRoleKeyUnique(ObjectUtil.isNull(Id) ? BaseConstants.NONE_ID : Id, roleKey));
    }

}
