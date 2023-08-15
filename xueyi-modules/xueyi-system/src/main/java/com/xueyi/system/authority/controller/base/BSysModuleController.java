package com.xueyi.system.authority.controller.base;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.security.utils.SecurityUserUtils;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.api.authority.domain.dto.SysModuleDto;
import com.xueyi.system.api.authority.domain.query.SysModuleQuery;
import com.xueyi.system.authority.service.ISysModuleService;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 系统服务 | 权限模块 | 模块管理 | 通用 业务处理
 *
 * @author xueyi
 */
public class BSysModuleController extends BaseController<SysModuleQuery, SysModuleDto, ISysModuleService> {

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "模块";
    }

    /**
     * 前置校验 新增/修改
     */
    @Override
    protected void AEHandle(BaseConstants.Operate operate, SysModuleDto module) {
        if (baseService.checkNameUnique(module.getId(), module.getName())) {
            warn(StrUtil.format("{}{}{}失败，{}名称已存在！", operate.getInfo(), getNodeName(), module.getName(), getNodeName()));
        }

        switch (operate) {
            case ADD -> {
            }
            case EDIT -> {
                SysModuleDto original = baseService.selectById(module.getId());
                if (ObjectUtil.isNull(original)) {
                    warn("数据不存在！");
                }
                module.setIsCommon(original.getIsCommon());
            }
        }

        if (module.isCommon() && SecurityUserUtils.isNotAdminTenant()) {
            warn(StrUtil.format("{}{}{}失败，无操作权限！", operate.getInfo(), getNodeName(), module.getName()));
        }
    }

    /**
     * 前置校验 删除
     */
    protected void RHandle(BaseConstants.Operate operate, List<Long> idList) {
        List<SysModuleDto> moduleList = baseService.selectListByIds(idList);
        boolean isTenant = SecurityUserUtils.isAdminTenant();
        Map<Long, SysModuleDto> moduleMap = moduleList.stream().filter(item -> isTenant || item.isNotCommon())
                .collect(Collectors.toMap(SysModuleDto::getId, Function.identity()));
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
