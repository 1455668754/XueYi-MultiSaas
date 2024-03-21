package com.xueyi.system.authority.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.constant.basic.TenantConstants;
import com.xueyi.common.core.context.SecurityContextHolder;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.IdUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.security.utils.SecurityUserUtils;
import com.xueyi.common.web.correlate.contant.CorrelateConstants;
import com.xueyi.common.web.entity.service.impl.TreeServiceImpl;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.api.authority.domain.query.SysMenuQuery;
import com.xueyi.system.authority.domain.correlate.SysMenuCorrelate;
import com.xueyi.system.authority.manager.ISysMenuManager;
import com.xueyi.system.authority.service.ISysMenuService;
import com.xueyi.system.authority.service.ISysModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 系统服务 | 权限模块 | 菜单管理 服务层处理
 *
 * @author xueyi
 */
@Service
public class SysMenuServiceImpl extends TreeServiceImpl<SysMenuQuery, SysMenuDto, SysMenuCorrelate, ISysMenuManager> implements ISysMenuService {

    @Autowired
    protected ISysModuleService moduleService;

    /**
     * 默认方法关联配置定义
     */
    @Override
    protected Map<CorrelateConstants.ServiceType, SysMenuCorrelate> defaultCorrelate() {
        return new HashMap<>() {{
            put(CorrelateConstants.ServiceType.DELETE, SysMenuCorrelate.BASE_DEL);
        }};
    }

    /**
     * 查询菜单对象列表 | 数据权限 | 附加数据
     *
     * @param menu 菜单对象
     * @return 菜单对象集合
     */
    @Override
//    @DataScope(userAlias = CREATE_BY, mapperScope = {"SysMenuMapper"})
    public List<SysMenuDto> selectListScope(SysMenuQuery menu) {
        boolean isAdminTenant = SecurityUserUtils.isAdminTenant();
        return SecurityContextHolder.setTenantIgnoreFun(() -> {
            List<SysMenuDto> list = super.selectListScope(menu);
            subCorrelates(list, SysMenuCorrelate.EN_INFO_SELECT);
            subCorrelates(list, SysMenuCorrelate.INFO_LIST);
            return list;
        }, isAdminTenant);
    }

    /**
     * 根据Id查询菜单信息
     *
     * @param id Id
     * @return 菜单数据对象
     */
    @Override
    public SysMenuDto selectInfoById(Serializable id) {
        boolean isAdminTenant = SecurityUserUtils.isAdminTenant();
        return SecurityContextHolder.setTenantIgnoreFun(() -> {
            SysMenuDto menu = selectById(id);
            subCorrelates(menu, SysMenuCorrelate.EN_INFO_SELECT);
            subCorrelates(menu, SysMenuCorrelate.INFO_LIST);
            return menu;
        }, isAdminTenant);
    }

    /**
     * 根据模块Id查询菜单路由
     *
     * @param moduleId 模块Id
     * @param menuIds  菜单Ids
     * @return 菜单列表
     */
    @Override
    public List<SysMenuDto> getRoutes(Long moduleId, Collection<Long> menuIds) {
        return baseManager.getRoutes(moduleId, menuIds);
    }

    /**
     * 获取企业有权限且状态正常的菜单
     *
     * @param authGroupIds 企业权限组Id集合
     * @param roleIds      角色Id集合
     * @param isLessor     租户标识
     * @param userType     用户标识
     * @return 菜单对象集合
     */
    @Override
    public List<SysMenuDto> selectEnterpriseList(Set<Long> authGroupIds, Set<Long> roleIds, String isLessor, String userType) {
        return baseManager.selectEnterpriseList(authGroupIds, roleIds, isLessor, userType);
    }

    /**
     * 新增菜单对象
     *
     * @param menu 菜单对象
     * @return 结果
     */
    @Override
    @DSTransactional
    public int insert(SysMenuDto menu) {
        menu.setName(IdUtil.simpleUUID());
        return super.insert(menu);
    }

    /**
     * 新增菜单对象（批量）
     *
     * @param menuList 菜单对象集合
     * @return 结果
     */
    @Override
    @DSTransactional
    public int insertBatch(Collection<SysMenuDto> menuList) {
        if (CollUtil.isNotEmpty(menuList)) {
            menuList.forEach(menu -> menu.setName(IdUtil.simpleUUID()));
        }
        return super.insertBatch(menuList);
    }

    /**
     * 单条操作 - 开始处理
     *
     * @param operate 服务层 - 操作类型
     * @param newDto  新数据对象（删除时不存在）
     * @param id      Id集合（非删除时不存在）
     */
    @Override
    protected SysMenuDto startHandle(OperateConstants.ServiceType operate, SysMenuDto newDto, Serializable id) {
        SysMenuDto originDto = SecurityContextHolder.setTenantIgnoreFun(() -> super.startHandle(operate, newDto, id));
        switch (operate) {
            case ADD -> {
                if (newDto.isCommon()) {
                    newDto.setTenantId(TenantConstants.COMMON_TENANT_ID);
                } else if (ObjectUtil.isNull(newDto.getTenantId())) {
                    newDto.setTenantId(SecurityUserUtils.getEnterpriseId());
                }
            }
            case EDIT -> {
                if (ObjectUtil.isNull(originDto)) {
                    throw new ServiceException("数据不存在！");
                } else if (ObjectUtil.notEqual(originDto.getIsCommon(), newDto.getIsCommon())) {
                    throw new ServiceException(StrUtil.format("{}菜单{}失败，不允许变更公共类型！", operate.getInfo(), newDto.getName()));
                }
                newDto.setIsCommon(originDto.getIsCommon());
                newDto.setTenantId(originDto.getTenantId());
            }
            case EDIT_STATUS -> {
                newDto.setIsCommon(originDto.getIsCommon());
                newDto.setTenantId(originDto.getTenantId());
            }
            case DELETE -> {
                if (SecurityUserUtils.isNotAdminTenant() && (originDto.isCommon() || ObjectUtil.notEqual(originDto.getTenantId(), SecurityUserUtils.getEnterpriseId()))) {
                    throw new ServiceException("无操作权限，公共菜单不允许删除！");
                }
            }
        }

        switch (operate) {
            case ADD, EDIT -> {
                if (newDto.isCommon()) {
                    SysModuleDto module = SecurityContextHolder.setTenantIgnoreFun(() -> moduleService.selectById(newDto.getModuleId()));
                    if (ObjectUtil.isNull(module)) {
                        throw new ServiceException("数据不存在！");
                    }
                    if (module.isNotCommon()) {
                        throw new ServiceException(StrUtil.format("{}菜单{}失败，公共菜单必须挂载在公共模块下！", operate.getInfo(), newDto.getTitle()));
                    }

                    if (ObjectUtil.notEqual(BaseConstants.TOP_ID, newDto.getParentId())) {
                        SysMenuDto parentMenu = SecurityContextHolder.setTenantIgnoreFun(() -> baseManager.selectById(newDto.getParentId()));
                        if (ObjectUtil.isNull(parentMenu)) {
                            throw new ServiceException("数据不存在！");
                        }
                        if (parentMenu.isNotCommon()) {
                            throw new ServiceException(StrUtil.format("{}菜单{}失败，公共菜单必须挂载在公共菜单下！", operate.getInfo(), newDto.getTitle()));
                        }
                    }
                }
            }
        }

        switch (operate) {
            case ADD, EDIT, EDIT_STATUS -> {
                if (newDto.isNotCommon() && SecurityUserUtils.isNotAdminTenant() && ObjectUtil.notEqual(SecurityUserUtils.getEnterpriseId(), newDto.getTenantId())) {
                    throw new ServiceException(StrUtil.format("{}菜单{}失败，仅允许配置本企业私有菜单！", operate.getInfo(), newDto.getName()));
                }
                if (SecurityUserUtils.isAdminTenant()) {
                    SecurityContextHolder.setEnterpriseId(newDto.getTenantId().toString());
                }
            }
            case DELETE -> {
                if (SecurityUserUtils.isAdminTenant()) {
                    SecurityContextHolder.setTenantIgnore();
                }
            }
        }
        return originDto;
    }

    /**
     * 单条操作 - 结束处理
     *
     * @param operate   服务层 - 操作类型
     * @param row       操作数据条数
     * @param originDto 源数据对象（新增时不存在）
     * @param newDto    新数据对象（删除时不存在）
     */
    @Override
    protected void endHandle(OperateConstants.ServiceType operate, int row, SysMenuDto originDto, SysMenuDto newDto) {
        switch (operate) {
            case DELETE -> {
                if (SecurityUserUtils.isAdminTenant()) {
                    SecurityContextHolder.clearTenantIgnore();
                }
            }
            case ADD, EDIT, EDIT_STATUS -> {
                if (SecurityUserUtils.isAdminTenant()) {
                    SecurityContextHolder.rollLastEnterpriseId();
                }
            }
        }
        super.endHandle(operate, row, originDto, newDto);
    }

    /**
     * 批量操作 - 开始处理
     *
     * @param operate 服务层 - 操作类型
     * @param newList 新数据对象集合（删除时不存在）
     * @param idList  Id集合（非删除时不存在）
     */
    @Override
    protected List<SysMenuDto> startBatchHandle(OperateConstants.ServiceType operate, Collection<SysMenuDto> newList, Collection<? extends Serializable> idList) {
        List<SysMenuDto> originList = SecurityContextHolder.setTenantIgnoreFun(() -> super.startBatchHandle(operate, newList, idList));
        if (operate == OperateConstants.ServiceType.BATCH_DELETE) {
            boolean isAdminTenant = SecurityUserUtils.isAdminTenant();
            Long enterpriseId = SecurityUserUtils.getEnterpriseId();
            originList = originList.stream().filter(item -> isAdminTenant || (item.isNotCommon() && ObjectUtil.equals(item.getTenantId(), enterpriseId)))
                    .collect(Collectors.toList());
            if (CollUtil.isEmpty(originList)) {
                throw new ServiceException("无待删除菜单！");
            }

            if (SecurityUserUtils.isAdminTenant()) {
                SecurityContextHolder.setTenantIgnore();
            }
        }
        return originList;
    }

    /**
     * 批量操作 - 结束处理
     *
     * @param operate    服务层 - 操作类型
     * @param rows       操作数据条数
     * @param originList 源数据对象集合（新增时不存在）
     * @param newList    新数据对象集合（删除时不存在）
     */
    @Override
    protected void endBatchHandle(OperateConstants.ServiceType operate, int rows, Collection<SysMenuDto> originList, Collection<SysMenuDto> newList) {
        if (operate == OperateConstants.ServiceType.BATCH_DELETE) {
            if (SecurityUserUtils.isAdminTenant()) {
                SecurityContextHolder.clearTenantIgnore();
            }
        }
        super.endBatchHandle(operate, rows, originList, newList);
    }
}
