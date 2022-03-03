package com.xueyi.common.web.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.xueyi.common.security.utils.SecurityUtils;
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
        System.out.println("-------|||||-----");
        System.out.println(metaObject.hasSetter("updateBy"));
        // 先判断是否存在该字段
        if (metaObject.hasSetter("createBy"))
            this.strictUpdateFill(metaObject, "createBy", SecurityUtils::getUserId, Long.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        System.out.println("--------------------");
        System.out.println(metaObject.hasSetter("updateBy"));
        // 先判断是否存在该字段
        if (metaObject.hasSetter("updateBy"))
            this.strictUpdateFill(metaObject, "updateBy", SecurityUtils::getUserId, Long.class);
    }
}