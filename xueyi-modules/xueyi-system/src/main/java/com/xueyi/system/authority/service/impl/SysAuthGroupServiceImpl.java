package com.xueyi.system.authority.service.impl;

import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.web.correlate.contant.CorrelateConstants;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.api.authority.domain.po.SysMenuPo;
import com.xueyi.system.authority.domain.correlate.SysAuthGroupCorrelate;
import com.xueyi.system.authority.domain.dto.SysAuthGroupDto;
import com.xueyi.system.authority.domain.query.SysAuthGroupQuery;
import com.xueyi.system.authority.manager.ISysAuthGroupManager;
import com.xueyi.system.authority.service.ISysAuthGroupService;
import com.xueyi.system.authority.service.ISysMenuService;
import com.xueyi.system.authority.service.ISysModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 系统服务 | 权限模块 | 企业权限组管理 服务层处理
 *
 * @author xueyi
 */
@Service
public class SysAuthGroupServiceImpl extends BaseServiceImpl<SysAuthGroupQuery, SysAuthGroupDto, SysAuthGroupCorrelate, ISysAuthGroupManager> implements ISysAuthGroupService {

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private ISysModuleService moduleService;

    /**
     * 默认方法关联配置定义
     */
    protected Map<CorrelateConstants.ServiceType, SysAuthGroupCorrelate> defaultCorrelate() {
        return new HashMap<>() {{
            put(CorrelateConstants.ServiceType.ADD, SysAuthGroupCorrelate.BASE_ADD);
            put(CorrelateConstants.ServiceType.EDIT, SysAuthGroupCorrelate.BASE_EDIT);
            put(CorrelateConstants.ServiceType.DELETE, SysAuthGroupCorrelate.BASE_DEL);
        }};
    }

    /**
     * 查询系统服务 | 权限模块 | 企业权限组对象列表 | 数据权限
     *
     * @param authGroup 系统服务 | 权限模块 | 企业权限组对象
     * @return 系统服务 | 权限模块 | 企业权限组对象集合
     */
    @Override
    //@DataScope(userAlias = "createBy", mapperScope = {"SysAuthGroupMapper"})
    public List<SysAuthGroupDto> selectListScope(SysAuthGroupQuery authGroup) {
        return super.selectListScope(authGroup);
    }

    /**
     * 根据Id查询单条数据对象
     *
     * @param id Id
     * @return 数据对象
     */
    @Override
    public SysAuthGroupDto selectInfoById(Serializable id) {
        return subCorrelates(selectById(id), SysAuthGroupCorrelate.INFO_LIST);
    }
}