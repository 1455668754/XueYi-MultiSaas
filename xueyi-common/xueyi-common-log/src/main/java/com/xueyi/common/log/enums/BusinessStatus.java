package com.xueyi.common.log.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作状态
 *
 * @author xueyi
 */
@Getter
@AllArgsConstructor
public enum BusinessStatus {

    SUCCESS("0", "成功"),
    FAIL("1", "失败");

    private final String code;
    private final String info;

}
