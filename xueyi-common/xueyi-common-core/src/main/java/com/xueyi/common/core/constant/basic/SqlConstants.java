package com.xueyi.common.core.constant.basic;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * sql 通用常量
 *
 * @author xueyi
 */
public class SqlConstants {

    /** sql 查询返回限制 */
    public static final int DEFAULT_BATCH_SIZE = 1000;

    /** sql 限制只查一条 */
    public static final String LIMIT_ONE = " limit 1 ";

    /** sql 恒等查询 */
    public static final String ALWAYS_FIND = " 1 = 1 ";

    /** sql 空查询 */
    public static final String NONE_FIND = " 1 = 0 ";

    /** sql find_in_set函数 */
    public static final String ANCESTORS_FIND = "find_in_set({0}, ancestors)";

    /** sql 祖籍字符串部分更新 */
    public static final String ANCESTORS_PART_UPDATE = "{} = insert({},{},{},'{}')";

    /** sql 树层级更新 */
    public static final String TREE_LEVEL_UPDATE = "{} = {} + {}";

    /** SQL - 操作类型 */
    @Getter
    @AllArgsConstructor
    public enum OperateType {

        SET("set", " {} = {} ", "赋值"),
        EQ("eq", " {} = {} ", "等于 ="),
        NE("ne", " {} <> {} ", "不等于 <>"),
        GT("gt", " {} > {} ", "大于 >"),
        GE("ge", " {} >= {} ", "大于等于 >="),
        LT("lt", " {} < {} ", "小于 <"),
        LE("le", " {} <= {} ", "小于等于 <="),
        BETWEEN("between", " {} BETWEEN {} AND {} ", "BETWEEN 值1 AND 值2"),
        NOT_BETWEEN("notBetween", " {} NOT BETWEEN {} AND {} ", "NOT BETWEEN 值1 AND 值2"),
        LIKE("like", " {} LIKE '%{}%' ", "LIKE '%值%'"),
        NOT_LIKE("notLike", " {} NOT LIKE '%{}%' ", "NOT LIKE '%值%'"),
        LIKE_LEFT("likeLeft", " {} LIKE '%{}' ", "LIKE '%值'"),
        LIKE_RIGHT("likeRight", " {} LIKE '{}%' ", "LIKE '值%'"),
        IS_NULL("isNull", " {} IS NULL ", "字段 IS NULL"),
        IS_NOT_NULL("isNotNull", " {} IS NOT NULL ", "字段 IS NOT NULL"),
        IN("in", " {} IN ({}) ", "字段 IN (value.get(0), value.get(1), ...)"),
        NOT_IN("notIn", " {} NOT IN ({}) ", "NOT IN (value.get(0), value.get(1), ...)");

        private final String code;
        private final String sql;
        private final String info;

    }

    /** 数据库字段映射 */
    @Getter
    @AllArgsConstructor
    public enum Entity {

        ID("id", "Id字段"),
        PARENT_ID("parent_id", "父级Id字段"),
        STATUS("status", "状态字段"),
        SORT("sort", "排序字段"),
        ANCESTORS("ancestors", "祖籍字段"),
        LEVEL("level", "层级字段");

        private final String code;
        private final String info;

    }

}
