package com.xueyi.common.datascope.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.system.AuthorityConstants;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.system.api.model.LoginUser;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.HexValue;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.schema.Column;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.xueyi.common.core.constant.basic.SecurityConstants.CREATE_BY;

/**
 * 数据过滤处理
 *
 * @author xueyi
 */
@Component
public class XueYiDataScopeInterceptor implements DataPermissionHandler {

    /**
     * @param where             Where 条件表达式
     * @param mappedStatementId Mapper接口方法ID
     * @return 结果
     */
    @Override
    public Expression getSqlSegment(Expression where, String mappedStatementId) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (ObjectUtil.isNull(loginUser) || loginUser.getUser().isAdmin()) {
            return where;
        }
        Long userId = loginUser.getUserId();
        String dataScope = loginUser.getScope().getDataScope();
        System.out.println("222222222222222222");
        try {
            Expression expression = new HexValue(" 1 = 1 ");
//            Expression expression = StrUtil.isEmpty(dataScope) ? new HexValue(" 1 = 0 ") : new HexValue(" 1 = 1 ");
            if (where == null) {
                where = expression;
            }
            System.out.println("111111111111111111");
            if(true) {
                EqualsTo selfEqualsTo1 = new EqualsTo();
                selfEqualsTo1.setLeftExpression(new Column("1"));
                selfEqualsTo1.setRightExpression(new LongValue("0"));
                return new AndExpression(where, selfEqualsTo1);
            }
            System.out.println(where);
            switch (Objects.requireNonNull(AuthorityConstants.DataScope.getValue(dataScope))) {
                case ALL:
                    return where;
                case CUSTOM:
                case DEPT:
                case DEPT_AND_CHILD:
                case POST:
                    // 创建IN 表达式
                    // 创建IN范围的元素集合
                    Set<Long> userScope = loginUser.getScope().getUserScope();
                    if (CollUtil.isNotEmpty(userScope)) {
                        // 把集合转变为JSQLParser需要的元素列表
                        ItemsList itemsList = new ExpressionList(userScope.stream().map(LongValue::new).collect(Collectors.toList()));
                        InExpression inExpression = new InExpression(new Column(CREATE_BY), itemsList);
                        return new AndExpression(where, inExpression);
                    } else {
                        EqualsTo selfEqualsTo = new EqualsTo();
                        selfEqualsTo.setLeftExpression(new Column(CREATE_BY));
                        selfEqualsTo.setRightExpression(new LongValue(SecurityConstants.EMPTY_USER_ID));
                        return new AndExpression(where, selfEqualsTo);
                    }
                case SELF:
                    EqualsTo selfEqualsTo = new EqualsTo();
                    selfEqualsTo.setLeftExpression(new Column(CREATE_BY));
                    selfEqualsTo.setRightExpression(new LongValue(userId));
                    return new AndExpression(where, selfEqualsTo);
            }
        } catch (Exception ignored) {
        }
        return where;
    }
}
