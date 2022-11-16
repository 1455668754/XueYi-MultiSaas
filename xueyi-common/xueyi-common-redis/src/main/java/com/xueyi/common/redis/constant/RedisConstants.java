package com.xueyi.common.redis.constant;

import com.xueyi.common.core.utils.core.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

public class RedisConstants {

    /** 缓存类型 */
    @Getter
    @AllArgsConstructor
    public enum OperateType {

        REFRESH_ALL("refresh_all", "更新全部"),
        REFRESH("refresh", "更新"),
        REMOVE("remove", "删除");

        private final String code;
        private final String info;

        public static OperateType getByCode(String code) {
            return Objects.requireNonNull(EnumUtil.getByCode(OperateType.class, code));
        }

    }

}
