package com.xueyi.tenant.source.controller.base;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.datasource.utils.DSUtil;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.tenant.api.source.domain.dto.TeSourceDto;
import com.xueyi.tenant.api.source.domain.query.TeSourceQuery;
import com.xueyi.tenant.source.service.ITeSourceService;
import com.xueyi.tenant.source.service.ITeStrategyService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 数据源管理 通用业务处理
 *
 * @author xueyi
 */
public class BTeSourceController extends BaseController<TeSourceQuery, TeSourceDto, ITeSourceService> {

    @Autowired
    private ITeStrategyService strategyService;

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "数据源";
    }

    /**
     * 前置校验 （强制）增加/修改
     */
    @Override
    protected void AEHandle(BaseConstants.Operate operate, TeSourceDto source) {
        DSUtil.testSlaveDs(source);
        if (baseService.checkNameUnique(source.getId(), source.getName())) {
            warn(StrUtil.format("{}{}{}失败，{}名称已存在", operate.getInfo(), getNodeName(), source.getName(), getNodeName()));
        }
    }

    /**
     * 前置校验 （强制）删除
     */
    @Override
    protected void RHandle(BaseConstants.Operate operate, List<Long> idList) {
        int size = idList.size();
        for (int i = idList.size() - 1; i >= 0; i--) {
            if (strategyService.checkSourceExist(idList.get(i))) {
                idList.remove(i);
            } else if (baseService.checkIsDefault(idList.get(i))) {
                idList.remove(i);
            }
        }
        if (CollUtil.isEmpty(idList)) {
            warn(StrUtil.format("删除失败，默认{}及已被使用的{}不允许删除！", getNodeName(), getNodeName()));
        } else if (idList.size() != size) {
            baseService.deleteByIds(idList);
            warn(StrUtil.format("默认{}及已被使用的{}不允许删除，其余{}删除成功！", getNodeName(), getNodeName(), getNodeName()));
        }
    }
}