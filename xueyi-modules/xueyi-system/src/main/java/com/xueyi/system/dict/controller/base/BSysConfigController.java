package com.xueyi.system.dict.controller.base;

import com.xueyi.common.cache.constants.CacheConstants;
import com.xueyi.common.cache.utils.DictUtil;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.api.dict.domain.dto.SysConfigDto;
import com.xueyi.system.api.dict.domain.query.SysConfigQuery;
import com.xueyi.system.dict.service.ISysConfigService;

import java.util.List;

/**
 * 参数配置管理 业务处理
 *
 * @author xueyi
 */
public class BSysConfigController extends BaseController<SysConfigQuery, SysConfigDto, ISysConfigService> {

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "参数";
    }

    /**
     * 查询参数
     *
     * @param code 参数编码
     * @return 参数
     */
    public AjaxResult getValueByCode(String code) {
        Object obj = DictUtil.getConfigCache(CacheConstants.ConfigType.getByCode(code));
        return success(obj);
    }

    /**
     * 前置校验 （强制）增加/修改
     */
    @Override
    protected void AEHandle(BaseConstants.Operate operate, SysConfigDto config) {
        if (baseService.checkConfigCodeUnique(config.getId(), config.getCode())) {
            warn(StrUtil.format("{}{}{}失败，参数编码已存在", operate.getInfo(), getNodeName(), config.getName()));
        }
    }

    /**
     * 前置校验 （强制）删除
     *
     * @param idList Id集合
     */
    @Override
    protected void RHandle(BaseConstants.Operate operate, List<Long> idList) {
        if (operate.isDelete()) {
            int size = idList.size();
            // remove oneself or admin
            for (int i = idList.size() - 1; i >= 0; i--) {
                if (baseService.checkIsBuiltIn(idList.get(i))) {
                    idList.remove(i);
                }
            }
            if (CollUtil.isEmpty(idList)) {
                warn(StrUtil.format("{}失败，不能删除内置参数！", operate.getInfo()));
            } else if (idList.size() != size) {
                baseService.deleteByIds(idList);
                warn(StrUtil.format("成功{}除内置参数外的所有参数！", operate.getInfo()));
            }
        }
    }
}
