package com.xueyi.system.authority.controller.base;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.system.AuthorityConstants;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.security.utils.SecurityUserUtils;
import com.xueyi.common.web.entity.controller.TreeController;
import com.xueyi.system.api.authority.domain.dto.SysMenuDto;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.api.authority.domain.query.SysMenuQuery;
import com.xueyi.system.authority.service.ISysMenuService;
import com.xueyi.system.authority.service.ISysModuleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 系统服务 | 权限模块 | 菜单管理 | 通用 业务处理
 *
 * @author xueyi
 */
public class BSysMenuController extends TreeController<SysMenuQuery, SysMenuDto, ISysMenuService> {

    @Autowired
    protected ISysModuleService moduleService;

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "菜单";
    }

    /** 定义父数据名称 */
    protected String getParentName() {
        return "模块";
    }

    /**
     * 构造树型Top节点
     *
     * @param query 数据查询对象
     * @return Top节点对象
     */
    @Override
    protected SysMenuDto TopNodeBuilder(SysMenuQuery query) {
        SysMenuDto topMenu = super.TopNodeBuilder(query);
        topMenu.setTitle(topMenu.getName());
        topMenu.setFrameType(AuthorityConstants.FrameType.NORMAL.getCode());
        topMenu.setMenuType(AuthorityConstants.MenuType.DIR.getCode());
        topMenu.setModuleId(query.getModuleId());
        return topMenu;
    }

    /**
     * 前置校验 新增/修改
     */
    @Override
    protected void AEHandle(BaseConstants.Operate operate, SysMenuDto menu) {
        if (baseService.checkNameUnique(menu.getId(), menu.getParentId(), menu.getName())) {
            warn(StrUtil.format("{}{}{}失败，{}名称已存在！", operate.getInfo(), getNodeName(), menu.getTitle(), getNodeName()));
        }

        switch (operate) {
            case ADD -> {
            }
            case EDIT -> {
                SysMenuDto original = baseService.selectById(menu.getId());
                if (ObjectUtil.isNull(original)) {
                    warn("数据不存在！");
                }
                menu.setIsCommon(original.getIsCommon());
            }
        }

        if (menu.isCommon()) {
            if (SecurityUserUtils.isNotAdminTenant()) {
                warn(StrUtil.format("{}{}{}失败，无操作权限！", operate.getInfo(), getNodeName(), menu.getTitle()));
            }
            SysModuleDto module = moduleService.selectById(menu.getModuleId());
            if (ObjectUtil.isNull(module)) {
                warn("数据不存在！");
            }
            if (module.isNotCommon()) {
                warn(StrUtil.format("{}{}{}失败，公共{}必须挂载在公共{}下！", operate.getInfo(), getNodeName(), menu.getTitle(), getNodeName(), getParentName()));
            }

            SysMenuDto parentMenu = baseService.selectById(menu.getParentId());
            if (ObjectUtil.isNull(parentMenu)) {
                warn("数据不存在！");
            }
            if (parentMenu.isNotCommon()) {
                warn(StrUtil.format("{}{}{}失败，公共{}必须挂载在公共{}下！", operate.getInfo(), getNodeName(), menu.getTitle(), getNodeName(), getNodeName()));
            }
        }
    }

    /**
     * 前置校验 删除
     */
    @Override
    protected void RHandle(BaseConstants.Operate operate, List<Long> idList) {
        List<SysMenuDto> moduleList = baseService.selectListByIds(idList);
        boolean isTenant = SecurityUserUtils.isAdminTenant();
        Map<Long, SysMenuDto> moduleMap = moduleList.stream().filter(item -> isTenant || item.isNotCommon())
                .collect(Collectors.toMap(SysMenuDto::getId, Function.identity()));
        for (int i = idList.size() - 1; i >= 0; i--) {
            if (!moduleMap.containsKey(idList.get(i))) {
                idList.remove(i);
            }
        }
        if (CollUtil.isEmpty(idList)) {
            warn(StrUtil.format("无待删除{}！", getNodeName()));
        }
    }
}
