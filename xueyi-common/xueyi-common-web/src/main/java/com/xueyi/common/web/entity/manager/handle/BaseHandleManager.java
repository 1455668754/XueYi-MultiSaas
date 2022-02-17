package com.xueyi.common.web.entity.manager.handle;

import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 数据封装层 操作方法 基类通用数据处理
 *
 * @param <D>  Dto
 * @param <DM> DtoMapper
 * @author xueyi
 */

public class BaseHandleManager<D extends BaseEntity, DM extends BaseMapper<D>> {

    @Autowired
    protected DM baseMapper;

}
