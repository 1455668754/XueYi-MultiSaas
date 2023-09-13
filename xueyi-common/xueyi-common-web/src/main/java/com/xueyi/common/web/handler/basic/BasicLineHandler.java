package com.xueyi.common.web.handler.basic;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.basic.DictConstants;
import com.xueyi.common.core.constant.basic.TenantConstants;
import com.xueyi.common.core.utils.core.ArrayUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.web.config.properties.TenantProperties;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.HexValue;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.util.cnfexpression.MultipleExpression;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用租户处理器
 *
 * @author xueyi
 */
public interface BasicLineHandler {

    /**
     * 获取租户字段名
     *
     * @return 租户字段名
     */
    String getTenantIdColumn();

    /**
     * 租户表租户控制
     *
     * @return 租户值
     */
    Expression getTenantId();

    /**
     * 公共表租户控制 | insert
     *
     * @return 租户值
     */
    default Expression getMixTenantId() {
        CaseExpression caseExpression = new CaseExpression();
        WhenClause commonCase = new WhenClause();
        commonCase.setWhenExpression(new EqualsTo(new HexValue(TenantConstants.COMMON_ID), new StringValue(DictConstants.DicCommonPrivate.COMMON.getCode())));
        commonCase.setThenExpression(new LongValue(BaseConstants.COMMON_ID));
        caseExpression.setSwitchExpression(commonCase);
        caseExpression.setElseExpression(getTenantId());
        return caseExpression;
    }

    /**
     * 公共表租户控制 | select
     *
     * @return 租户值
     */
    default MultipleExpression getCommonTenantId() {
        List<Expression> childList = new ArrayList<>();
        if (SecurityUtils.isNotEmptyTenant())
            childList.add(new LongValue(BaseConstants.COMMON_ID));
        childList.add(getTenantId());
        return new MultipleExpression(childList) {
            @Override
            public String getStringExpression() {
                return StrUtil.COMMA;
            }
        };
    }

    /**
     * 根据表名判断是否忽略拼接多租户条件
     * <p>
     * 默认都要进行解析并拼接多租户条件
     *
     * @param tableName 表名
     * @return 是否忽略, true:表示忽略，false:需要解析并拼接多租户条件
     */
    default boolean ignoreTable(String tableName) {
        return ObjectUtil.equals(Boolean.TRUE, TenantProperties.getIgnoreTenant()) || !isTenantTable(tableName);
    }

    /**
     * 忽略插入租户字段逻辑
     *
     * @param columns        插入字段
     * @param tenantIdColumn 租户 ID 字段
     * @return 结果
     */
    default boolean ignoreInsert(List<Column> columns, String tenantIdColumn) {
        return columns.stream().map(Column::getColumnName).anyMatch(i -> i.equalsIgnoreCase(tenantIdColumn));
    }

    /**
     * 判断是否为公共表
     *
     * @return 结果
     */
    default boolean isCommonTable(String tableName) {
        return ArrayUtil.contains(TenantProperties.getCommonTable(), tableName);
    }

    /**
     * 判断是否为非租户表
     *
     * @return 结果
     */
    default boolean isExcludeTable(String tableName) {
        return ArrayUtil.contains(TenantProperties.getExcludeTable(), tableName);
    }

    /**
     * 判断是否为租户表
     *
     * @return 结果
     */
    default boolean isTenantTable(String tableName) {
        return !isExcludeTable(tableName);
    }

    /**
     * 判断是否为租管租户
     *
     * @return 结果
     */
    default boolean isLessor() {
        return SecurityUtils.isAdminTenant();
    }

    /**
     * 构造更新、删除租户表达式 | delete/update -> where
     *
     * @param table 表对象
     * @param where 表达式条件对象
     * @return Expression
     */
    default Expression updateExpression(Table table, Expression where) {
        return isCommonTable(table.getName()) && isLessor() ? inExpression(table, where) : andExpression(table, where);
    }

    /**
     * 租户表达式 | insert -> insert
     *
     * @param tableName 表名
     * @return Expression
     */
    default Expression getInsertTenantId(String tableName) {
        return isCommonTable(tableName) ? getMixTenantId() : getTenantId();
    }

    /**
     * 租户表达式 | insert -> insert
     *
     * @param tableName 表名
     * @return EqualsTo
     */
    default EqualsTo getInsertTenantEqualsTo(String tableName) {
        return new EqualsTo(new StringValue(getTenantIdColumn()), getInsertTenantId(tableName));
    }

    /**
     * 租户表达式 | delete/update -> where
     *
     * @param table 表对象
     * @param where 表达式条件对象
     * @return Expression
     */
    default BinaryExpression andExpression(Table table, Expression where) {
        EqualsTo equalsTo = new EqualsTo(getAliasColumn(table), getTenantId());
        return ObjectUtil.isNotNull(where)
                ? where instanceof OrExpression ? new AndExpression(new Parenthesis(where), equalsTo) : new AndExpression(where, equalsTo)
                : equalsTo;
    }

    /**
     * 混合租户表达式 | delete/update -> where common
     *
     * @param table 表对象
     * @param where 表达式条件对象
     * @return Expression
     */
    default Expression inExpression(Table table, Expression where) {
        InExpression inExpression = new InExpression();
        inExpression.setLeftExpression(getAliasColumn(table));
        inExpression.setRightExpression(getCommonTenantId());
        return ObjectUtil.isNotNull(where)
                ? where instanceof OrExpression ? new AndExpression(new Parenthesis(where), inExpression) : new AndExpression(where, inExpression)
                : inExpression;
    }

    /**
     * 获取租户别名列
     *
     * @param table 表对象
     * @return 租户别名列
     */
    default Column getAliasColumn(Table table) {
        String column = (ObjectUtil.isNotNull(table.getAlias()) ? table.getAlias().getName() : table.getName()) +
                StrUtil.DOT + getTenantIdColumn();
        return new Column(column);
    }
}
