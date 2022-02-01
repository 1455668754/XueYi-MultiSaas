package com.xueyi.common.core.constant;

/**
 * 租户通用常量
 *
 * @author xueyi
 */
public class TenantConstants {

    /** 注册租户默认策略Id */
    public static final Long REGISTER_TENANT_STRATEGY_ID = 0L;

    /** 公共租户Id */
    public static final Long COMMON_TENANT_ID = 0L;

    /** 具备公共数据的表名 */
    public static final String[] COMMON_TENANT_TABLE = {"sys_menu", "sys_module"};

    /** 不进行租户控制的表名 */
    public static final String[] EXCLUDE_TENANT_TABLE = {"te_tenant", "te_strategy", "te_strategy_source_merge",
            "te_source", "te_source_separation_merge", "sys_dict_type", "sys_dict_data", "sys_config", "gen_table", "gen_table_column"};

    /** 策略源标识 */
    public static final String ISOLATE = "#isolute";

    /** 主数据源标识 */
    public static final String MASTER = "#master";

    /** 数据源 */
    public enum Source {

        MASTER("master", "默认数据源"), SLAVE("slave", "从数据源"), REGISTER("slave", "注册数据源");

        private final String code;
        private final String info;

        Source(String code, String info) {
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

    /** 数据源类型 */
    public enum SourceType {

        READ_WRITE("0", "读写"), READ("1", "只读"), WRITE("2", "只写");

        private final String code;
        private final String info;

        SourceType(String code, String info) {
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

    /** 源同步策略类型 */
    public enum SyncType {

        UNCHANGED("0", "不变"), REFRESH("1", "刷新"), ADD("2", "新增"), DELETE("2", "删除");

        private final String code;
        private final String info;

        SyncType(String code, String info) {
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
