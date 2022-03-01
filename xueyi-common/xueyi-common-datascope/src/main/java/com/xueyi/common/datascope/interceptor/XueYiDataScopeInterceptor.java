package com.xueyi.common.datascope.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.system.AuthorityConstants;
import com.xueyi.common.security.service.TokenService;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 数据过滤处理
 *
 * @author xueyi
 */
public class XueYiDataScopeInterceptor implements DataPermissionHandler {

    @Autowired
    private TokenService tokenService;

    /**
     * @param where             Where 条件表达式
     * @param mappedStatementId Mapper接口方法ID
     * @return 结果
     */
    @Override
    public Expression getSqlSegment(Expression where, String mappedStatementId) {
        List<String> split = StrUtil.split(mappedStatementId, '.');
        int index = split.size();
        String method = split.get(index - 1);
        String mapper = split.get(index - 2);
        LoginUser loginUser = tokenService.getLoginUser();
        if (ObjectUtil.isNotNull(loginUser) || loginUser.getUser().isAdmin()) {
            return where;
        }
        Long userId = loginUser.getUserId();
        String dataScope = loginUser.getDataScope().getDataScope();
        try {
            // 1. 获取权限过滤相关信息
//            log.info("进行权限过滤,dataScope:{} , where: {},mappedStatementId: {}", dataScope, where, mappedStatementId);
            Expression expression = new HexValue(" 1 = 1 ");
            if (where == null) {
                where = expression;
            }
            switch (Objects.requireNonNull(AuthorityConstants.DataScope.getValue(dataScope))) {
                case ALL:
                    return where;
                case CUSTOM:
                case DEPT:
                case DEPT_AND_CHILD:
                case POST:
                    // 创建IN 表达式
                    // 创建IN范围的元素集合
                    Set<Long> userScope = loginUser.getDataScope().getUserScope();
                    if (CollUtil.isNotEmpty(userScope)) {
                        // 把集合转变为JSQLParser需要的元素列表
                        ItemsList itemsList = new ExpressionList(userScope.stream().map(LongValue::new).collect(Collectors.toList()));
                        InExpression inExpression = new InExpression(new Column("create_by"), itemsList);
                        return new AndExpression(where, inExpression);
                    } else {
                        EqualsTo selfEqualsTo = new EqualsTo();
                        selfEqualsTo.setLeftExpression(new Column("create_by"));
                        selfEqualsTo.setRightExpression(new LongValue(SecurityConstants.EMPTY_USER_ID));
                        return new AndExpression(where, selfEqualsTo);
                    }
                case SELF:
                    EqualsTo selfEqualsTo = new EqualsTo();
                    selfEqualsTo.setLeftExpression(new Column("create_by"));
                    selfEqualsTo.setRightExpression(new LongValue(userId));
                    return new AndExpression(where, selfEqualsTo);
            }
        } catch (Exception ignored) {
        }
        return where;
    }
}
