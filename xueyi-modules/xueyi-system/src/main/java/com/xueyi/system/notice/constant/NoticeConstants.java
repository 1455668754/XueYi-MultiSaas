package com.xueyi.system.notice.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通知通用常量
 *
 * @author xueyi
 */
public class NoticeConstants {

    /** 通知状态 */
    @Getter
    @AllArgsConstructor
    public enum NoticeStatus {

        READY("0", "待发送"),
        SUCCESS("1", "已发送"),
        CLOSED("2", "已关闭"),
        FAIL("3", "发送失败"),
        ABNORMAL("4", "发送异常");

        private final String code;
        private final String info;

    }
}
