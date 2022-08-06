package com.xueyi.common.core.constant.basic;

/**
 * 服务名称通用常量
 * 
 * @author xueyi
 */
public class ServiceConstants {

    /** 认证服务的serviceId */
    public static final String AUTH_SERVICE = "xueyi-auth";

    /** 文件服务的serviceId */
    public static final String FILE_SERVICE = "xueyi-file";

    /** 系统模块的serviceId */
    public static final String SYSTEM_SERVICE = "xueyi-system";

    /** 租管模块的serviceId */
    public static final String TENANT_SERVICE = "xueyi-tenant";

    /** 定时任务模块的serviceId */
    public static final String JOB_SERVICE = "xueyi-job";

    /** 访问来源 */
    public enum FromSource {
        CLOUD("cloud", "vue2后台前端"),
        MULTI("multi", "vue3后台前端");

        private final String code;
        private final String info;

        FromSource(String code, String info) {
            this.code = code;
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }
    }
}
