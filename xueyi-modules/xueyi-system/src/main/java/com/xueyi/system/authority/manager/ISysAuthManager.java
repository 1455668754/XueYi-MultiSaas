package com.xueyi.system.authority.manager;

import com.xueyi.system.authority.domain.vo.SysAuthTree;

import java.util.List;

/**
 * 系统服务 | 权限模块 | 权限管理 数据封装层
 *
 * @author xueyi
 */
public interface ISysAuthManager {

    /**
     * 获取公共模块 | 菜单权限树
     *
     * @return 权限对象集合
     */
    List<SysAuthTree> selectCommonAuthScope();

    /**
     * 获取企业模块 | 菜单权限树 | 用户范围内
     *
     * @return 权限对象集合
     */
    List<SysAuthTree> selectEnterpriseAuthScope();

}
