package com.xueyi.system.authority.controller;

import cn.hutool.core.util.StrUtil;
import com.xueyi.common.core.constant.AuthorityConstants;
import com.xueyi.common.core.constant.BaseConstants;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.web.entity.controller.SubBaseController;
import com.xueyi.system.api.domain.authority.dto.SysMenuDto;
import com.xueyi.system.api.domain.authority.dto.SysModuleDto;
import com.xueyi.system.authority.service.ISysMenuService;
import com.xueyi.system.authority.service.ISysModuleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 模块管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/module")
public class SysModuleController extends SubBaseController<SysModuleDto, ISysModuleService, SysMenuDto, ISysMenuService> {

    /** 定义节点名称 */
    protected String getNodeName() {
        return "模块";
    }

    /** 定义子数据名称 */
    protected String getSubName() {
        return "菜单";
    }

//    /**
//     * 查询首页可展示模块信息列表
//     */
//    @GetMapping("/getRoutes")
//    public AjaxResult getRoutes() {
//        return AjaxResult.success(baseService.getRoutes());
//    }

    /**
     * 新增/修改 前置校验
     */
    @Override
    protected void baseRefreshValidated(BaseConstants.Operate operate, SysModuleDto module) {
        if (StringUtils.equals(AuthorityConstants.IsCommon.YES.getCode(), module.getIsCommon()) && !SecurityUtils.isAdminTenant())
            throw new ServiceException(StrUtil.format("{}{}{}失败，没有操作权限", operate.getInfo(), getNodeName(), module.getName()));
    }

}
