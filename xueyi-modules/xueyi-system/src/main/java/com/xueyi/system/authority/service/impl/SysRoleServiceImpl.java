package com.xueyi.system.authority.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.constant.system.AuthorityConstants;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.common.web.correlate.contant.CorrelateConstants;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.api.authority.domain.dto.SysRoleDto;
import com.xueyi.system.api.authority.domain.query.SysRoleQuery;
import com.xueyi.system.authority.domain.correlate.SysRoleCorrelate;
import com.xueyi.system.authority.manager.ISysRoleManager;
import com.xueyi.system.authority.service.ISysRoleService;
import com.xueyi.system.organize.service.ISysOrganizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.xueyi.common.core.constant.basic.SecurityConstants.CREATE_BY;

/**
 * 系统服务 | 权限模块 | 角色管理 服务层处理
 *
 * @author xueyi
 */
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleQuery, SysRoleDto, SysRoleCorrelate, ISysRoleManager> implements ISysRoleService {

    @Autowired
    private ISysOrganizeService organizeService;

    /**
     * 默认方法关联配置定义
     */
    @Override
    protected Map<CorrelateConstants.ServiceType, SysRoleCorrelate> defaultCorrelate() {
        return new HashMap<>() {{
            put(CorrelateConstants.ServiceType.ADD, SysRoleCorrelate.BASE_ADD);
            put(CorrelateConstants.ServiceType.DELETE, SysRoleCorrelate.BASE_DEL);
        }};
    }

    /**
     * 查询角色对象列表 | 数据权限 | 附加数据
     *
     * @param role 角色对象
     * @return 角色对象集合
     */
    @Override
    @DataScope(userAlias = CREATE_BY, mapperScope = {"SysRoleMapper"})
    public List<SysRoleDto> selectListScope(SysRoleQuery role) {
        return super.selectListScope(role);
    }

    /**
     * 根据Id查询角色信息对象 | 含权限
     *
     * @param id Id
     * @return 角色信息对象
     */
    @Override
    public SysRoleDto selectAuthById(Long id) {
        return subCorrelates(selectById(id), SysRoleCorrelate.AUTH_SEL);
    }

    /**
     * 修改角色功能权限
     *
     * @param role 角色对象
     * @return 结果
     */
    @Override
    public int editRoleAuth(SysRoleDto role) {
        return editCorrelates(role, SysRoleCorrelate.AUTH_EDIT);
    }

    /**
     * 修改角色组织权限
     *
     * @param role 角色对象
     * @return 结果
     */
    @Override
    @DSTransactional
    public int updateDataScope(SysRoleDto role) {
        int row = baseManager.updateDataScope(role.getId(), role.getRoleKey(), role.getDataScope());
        if (row > 0) {
            organizeService.editRoleOrganizeMerge(role.getId(),
                    StrUtil.equals(role.getDataScope(), AuthorityConstants.DataScope.CUSTOM.getCode())
                            ? role.getOrganizeIds()
                            : new Long[]{});
        }
        return row;
    }

    /**
     * 单条操作 - 结束处理
     *
     * @param operate   服务层 - 操作类型
     * @param row       操作数据条数
     * @param originDto 源数据对象（新增时不存在）
     * @param newDto    新数据对象（删除时不存在）
     */
    protected void endHandle(OperateConstants.ServiceType operate, int row, SysRoleDto originDto, SysRoleDto newDto) {
        super.endHandle(operate, row, originDto, newDto);
        if (row > 0) {
            if (Objects.requireNonNull(operate) == OperateConstants.ServiceType.ADD) {
                organizeService.addRoleOrganizeMerge(newDto.getId(),
                        StrUtil.equals(newDto.getDataScope(), AuthorityConstants.DataScope.CUSTOM.getCode())
                                ? newDto.getOrganizeIds()
                                : new Long[]{});
            }
        }
    }
}
