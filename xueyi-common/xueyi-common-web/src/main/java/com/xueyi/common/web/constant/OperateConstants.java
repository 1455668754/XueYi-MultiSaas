package com.xueyi.common.web.constant;

import com.xueyi.common.core.utils.core.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作类型通用常量
 *
 * @author xueyi
 */
public class OperateConstants {


    /** 服务层 - 操作类型 */
    @Getter
    @AllArgsConstructor
    public enum ServiceType {

        ADD("ADD", "新增"),
        BATCH_ADD("BATCH_ADD", "批量新增"),
        EDIT("EDIT", "修改"),
        BATCH_EDIT("BATCH_EDIT", "批量修改"),
        EDIT_STATUS("EDIT_STATUS", "修改状态"),
        DELETE("DELETE", "删除"),
        BATCH_DELETE("BATCH_DELETE", "批量删除");

        private final String code;
        private final String info;

        /** 是否为单条操作 */
        public boolean isSingle() {
            return StrUtil.equalsAny(name(), ADD.name(), EDIT.name(), EDIT_STATUS.name(), DELETE.name());
        }

        /** 是否为批量操作 */
        public boolean isBatch() {
            return StrUtil.equalsAny(name(), BATCH_ADD.name(), BATCH_EDIT.name(), BATCH_DELETE.name());
        }

    }
}
