package com.xueyi.common.web.handler;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.xueyi.common.core.constant.AuthorityConstants;
import com.xueyi.common.core.constant.TenantConstants;
import com.xueyi.common.security.utils.SecurityUtils;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.util.cnfexpression.MultipleExpression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 租户处理器
 *
 * @author xueyi
 */
public class XueYiTenantLineHandler implements TenantLineHandler {

    @Override
    public Expression getTenantId() {
        return new LongValue(SecurityUtils.getEnterpriseId());
    }

    public Expression getCommonTenantId() {
        List<Expression> childList = new ArrayList<>();
        if (SecurityUtils.isNotEmptyTenant())
            childList.add(new LongValue(TenantConstants.COMMON_TENANT_ID));
        childList.add(new LongValue(SecurityUtils.getEnterpriseId()));
        return new MultipleExpression(childList) {
            @Override
            public String getStringExpression() {
                return ",";
            }
        };
    }

    @Override
    public String getTenantIdColumn() {
        return "tenant_id";
    }

    @Override
    public boolean ignoreTable(String tableName) {
        return Arrays.asList(TenantConstants.EXCLUDE_TENANT_TABLE).contains(tableName);
    }

    @Override
    public boolean ignoreInsert(List<Column> columns, String tenantIdColumn) {
        return TenantLineHandler.super.ignoreInsert(columns, tenantIdColumn);
    }

    public boolean isCommonTable(String tableName) {
        return Arrays.asList(TenantConstants.COMMON_TENANT_TABLE).contains(tableName);
    }

    public boolean isLessor() {
        return ObjectUtil.equals(AuthorityConstants.TenantType.ADMIN.getCode(), SecurityUtils.getIsLessor());
    }
}
