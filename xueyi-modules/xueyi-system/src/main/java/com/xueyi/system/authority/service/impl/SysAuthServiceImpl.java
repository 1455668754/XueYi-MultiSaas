package com.xueyi.system.authority.service.impl;

import com.xueyi.system.authority.domain.vo.SysAuthTree;
import com.xueyi.system.authority.manager.ISysAuthManager;
import com.xueyi.system.authority.service.ISysAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 权限管理 服务层处理
 *
 * @author xueyi
 */
@Service
public class SysAuthServiceImpl implements ISysAuthService {

    @Autowired
    private ISysAuthManager authManager;

    /**
     * 获取公共模块|菜单权限树
     *
     * @return 权限对象集合
     */
    @Override
    public List<SysAuthTree> selectCommonAuthScope() {
        return authManager.selectCommonAuthScope();
    }

    /**
     * 获取企业模块|菜单权限树
     *
     * @return 权限对象集合
     */
    @Override
    public List<SysAuthTree> selectEnterpriseAuthScope() {
        return authManager.selectEnterpriseAuthScope();
    }
}
