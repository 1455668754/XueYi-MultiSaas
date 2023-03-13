package com.xueyi.common.web.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.xueyi.common.security.utils.base.BaseSecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * 自动填充处理器
 *
 * @author xueyi
 */
@Component
public class XueYiMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject.hasSetter("createBy"))
            this.strictInsertFill(metaObject, "createBy", BaseSecurityUtils::getUserId, Long.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.hasSetter("updateBy"))
            this.strictUpdateFill(metaObject, "updateBy", BaseSecurityUtils::getUserId, Long.class);
    }
}