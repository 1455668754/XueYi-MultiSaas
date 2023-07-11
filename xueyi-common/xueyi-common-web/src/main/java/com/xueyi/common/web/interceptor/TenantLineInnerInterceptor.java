package com.xueyi.common.web.interceptor;

import com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper;
import com.baomidou.mybatisplus.core.toolkit.ClassUtils;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.BaseMultiTableInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.toolkit.PropertyMapper;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.NumberUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.web.handler.TenantLineHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.WithItem;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * 租户拦截器
 *
 * @author xueyi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TenantLineInnerInterceptor extends BaseMultiTableInnerInterceptor implements InnerInterceptor {

    /** 租户处理器 */
    private TenantLineHandler tenantLineHandler;

    /**
     * before -> query
     */
    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        if (InterceptorIgnoreHelper.willIgnoreTenantLine(ms.getId())) return;
        PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
        mpBs.sql(this.parserSingle(mpBs.sql(), null));
    }

    /**
     * before -> prepare
     */
    @Override
    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
        PluginUtils.MPStatementHandler mpSh = PluginUtils.mpStatementHandler(sh);
        MappedStatement ms = mpSh.mappedStatement();
        SqlCommandType sct = ms.getSqlCommandType();
        if (sct == SqlCommandType.INSERT || sct == SqlCommandType.UPDATE || sct == SqlCommandType.DELETE) {
            if (InterceptorIgnoreHelper.willIgnoreTenantLine(ms.getId()))
                return;
            PluginUtils.MPBoundSql mpBs = mpSh.mPBoundSql();
            mpBs.sql(this.parserMulti(mpBs.sql(), null));
        }
    }

    /**
     * select process
     */
    @Override
    protected void processSelect(Select select, int index, String sql, Object obj) {
        String whereSegment = (String) obj;
        this.processSelectBody(select.getSelectBody(), whereSegment);
        List<WithItem> withItemsList = select.getWithItemsList();
        if (CollUtil.isNotEmpty(withItemsList))
            withItemsList.forEach((withItem) -> this.processSelectBody(withItem, whereSegment));
    }

    /**
     * insert 语句处理
     */
    @Override
    protected void processInsert(Insert insert, int index, String sql, Object obj) {
        String tableName = insert.getTable().getName();
        // 忽略租户控制退出执行
        if (this.tenantLineHandler.ignoreTable(tableName))
            return;

        List<Column> columns = insert.getColumns();
        // 针对不给列名的insert 不处理
        if (CollUtil.isEmpty(columns))
            return;

        // 增加租户列
        String tenantIdColumn = this.tenantLineHandler.getTenantIdColumn();
        columns.add(new Column(tenantIdColumn));
        List<Expression> duplicateUpdateColumns = insert.getDuplicateUpdateExpressionList();
        if (CollUtil.isNotEmpty(duplicateUpdateColumns)) {
            EqualsTo equalsTo = this.tenantLineHandler.getInsertTenantEqualsTo(tableName);
            duplicateUpdateColumns.add(equalsTo);
        }

        Select select = insert.getSelect();
        if (select != null) {
            this.processInsertSelect(select.getSelectBody(), (String) obj);
        } else {
            if (insert.getItemsList() == null) {
                throw ExceptionUtils.mpe("Failed to process multiple-table update, please exclude the tableName or statementId", new Object[0]);
            }
            ItemsList itemsList = insert.getItemsList();
            Expression tenantId = this.tenantLineHandler.getInsertTenantId(tableName);
            if (itemsList instanceof MultiExpressionList multiExpressionList) {
                multiExpressionList.getExpressionLists()
                        .forEach((el) -> el.getExpressions().add(tenantId));
            } else {
                ((ExpressionList) itemsList).getExpressions().add(tenantId);
            }
        }
    }

    /**
     * update 语句处理
     */
    @Override
    protected void processUpdate(Update update, int index, String sql, Object obj) {
        Table table = update.getTable();
        // 过滤退出执行
        if (this.tenantLineHandler.ignoreTable(table.getName()))
            return;
        update.setWhere(this.buildTableExpression(table, update.getWhere(), (String) obj));
    }

    /**
     * delete 语句处理
     */
    @Override
    protected void processDelete(Delete delete, int index, String sql, Object obj) {
        // 过滤退出执行
        if (tenantLineHandler.ignoreTable(delete.getTable().getName()))
            return;
        delete.setWhere(this.buildTableExpression(delete.getTable(), delete.getWhere(), (String) obj));
    }

    /**
     * 处理 insert into select
     *
     * @param selectBody   SelectBody
     * @param whereSegment whereSegment
     */
    protected void processInsertSelect(SelectBody selectBody, final String whereSegment) {
        PlainSelect plainSelect = (PlainSelect) selectBody;
        FromItem fromItem = plainSelect.getFromItem();
        if (fromItem instanceof Table) {
            this.processPlainSelect(plainSelect, whereSegment);
            this.appendSelectItem(plainSelect.getSelectItems());
        } else if (fromItem instanceof SubSelect subSelect) {
            this.appendSelectItem(plainSelect.getSelectItems());
            this.processInsertSelect(subSelect.getSelectBody(), whereSegment);
        }
    }

    /**
     * 追加 SelectItem
     *
     * @param selectItems SelectItem
     */
    protected void appendSelectItem(List<SelectItem> selectItems) {
        if (CollUtil.isEmpty(selectItems))
            return;
        if (selectItems.size() == NumberUtil.One) {
            SelectItem item = selectItems.get(NumberUtil.Zero);
            if (item instanceof AllColumns || item instanceof AllTableColumns)
                return;
        }
        selectItems.add(new SelectExpressionItem(new Column(this.tenantLineHandler.getTenantIdColumn())));
    }

    /**
     * 构造处理条件
     */
    protected Expression builderExpression(Expression currentExpression, List<Table> tables, final String whereSegment) {
        // 没有表需要处理直接返回
        if (CollUtil.isEmpty(tables))
            return currentExpression;
        Expression injectExpression = null;
        Expression tenantId = this.tenantLineHandler.getTenantId();
        Expression commonTenantId = this.tenantLineHandler.getCommonTenantId();
        // 构造每张表的租户控制条件
        for (Table table : tables) {
            if (!this.tenantLineHandler.ignoreTable(table.getName())) {
                // 注入的表达式
                if (this.tenantLineHandler.isCommonTable(table.getName())) {
                    InExpression inExpression = new InExpression();
                    inExpression.setLeftExpression(this.tenantLineHandler.getAliasColumn(table));
                    inExpression.setRightExpression(commonTenantId);
                    injectExpression = ObjectUtil.isNotNull(injectExpression)
                            ? new AndExpression(injectExpression, inExpression)
                            : inExpression;
                } else {
                    injectExpression = ObjectUtil.isNotNull(injectExpression)
                            ? new AndExpression(injectExpression, new EqualsTo(this.tenantLineHandler.getAliasColumn(table), tenantId))
                            : new EqualsTo(this.tenantLineHandler.getAliasColumn(table), tenantId);
                }
            }
        }
        // 返回处理后的表达式
        return ObjectUtil.isNotNull(injectExpression)
                ? ObjectUtil.isNotNull(currentExpression)
                ? new AndExpression(currentExpression instanceof OrExpression ? new Parenthesis(currentExpression) : currentExpression, injectExpression)
                : injectExpression
                : currentExpression;
    }

    public Expression buildTableExpression(final Table table, final Expression where, final String whereSegment) {
        return this.tenantLineHandler.updateExpression(table, where);
    }

    @Override
    public void setProperties(Properties properties) {
        PropertyMapper.newInstance(properties).whenNotBlank("tenantLineHandler", ClassUtils::newInstance, this::setTenantLineHandler);
    }
}
