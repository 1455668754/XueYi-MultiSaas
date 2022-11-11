package com.xueyi.common.core.constant.job;

import com.xueyi.common.core.utils.core.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 任务调度通用常量
 *
 * @author xueyi
 */
public class ScheduleConstants {

    /** 执行目标name */
    public static final String TASK_CLASS_NAME = "TASK_CLASS_NAME";

    /** 执行目标key */
    public static final String TASK_PROPERTIES = "TASK_PROPERTIES";

    /** 定时任务白名单配置（仅允许访问的包名，如其他需要可以自行添加） */
    public static final String[] JOB_WHITELIST_STR = {"com.xueyi"};

    /** 定时任务违规的字符 */
    public static final String[] JOB_ERROR_STR = {"java.net.URL", "javax.naming.InitialContext", "org.yaml.snakeyaml",
            "org.springframework", "org.apache", "com.xueyi.common.core.utils.file"};

    /** 任务状态 */
    @Getter
    @AllArgsConstructor
    public enum Status {

        NORMAL("0", "正常"), PAUSE("1", "暂停");

        private final String code;
        private final String info;

    }

    /** 错误策略 */
    @Getter
    @AllArgsConstructor
    public enum Misfire {

        DEFAULT("0", "默认"),
        IGNORE_MISFIRES("1", "立即触发执行"),
        FIRE_AND_PROCEED("2", "触发一次执行"),
        DO_NOTHING("3", "不触发立即执行");

        private final String code;
        private final String info;

        public static Misfire getByCode(String code) {
            return Objects.requireNonNull(EnumUtil.getByCode(Misfire.class, code));
        }

    }
}
