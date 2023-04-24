package com.xueyi.system.dict.controller.base;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.api.dict.domain.dto.SysDictTypeDto;
import com.xueyi.system.api.dict.domain.query.SysDictTypeQuery;
import com.xueyi.system.dict.service.ISysDictTypeService;

/**
 * 字典类型管理 业务处理
 *
 * @author xueyi
 */
public class BSysDictTypeController extends BaseController<SysDictTypeQuery, SysDictTypeDto, ISysDictTypeService> {

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "字典类型";
    }

    /**
     * 前置校验 （强制）增加/修改
     */
    @Override
    protected void AEHandle(BaseConstants.Operate operate, SysDictTypeDto dictType) {
        if (baseService.checkDictCodeUnique(dictType.getId(), dictType.getCode())) {
            warn(StrUtil.format("{}{}{}失败，字典编码已存在", operate.getInfo(), getNodeName(), dictType.getName()));
        }
    }
}
