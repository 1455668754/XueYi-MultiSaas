package com.xueyi.common.datascope.aspect;

import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.web.entity.BaseEntity;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.system.api.model.LoginUser;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * 数据过滤处理
 *
 * @author xueyi
 */
@Aspect
@Component
public class DataScopeAspect {

    /** 数据权限过滤关键字 | 查询 */
    public static final String DATA_SCOPE = "dataScope";

    /** 数据权限过滤关键字 | 更新 */
    public static final String UPDATE_SCOPE = "updateScope";

    @Before("@annotation(controllerDataScope)")
    public void doBefore(JoinPoint point, DataScope controllerDataScope) throws Throwable {
        clearDataScope(point);
        handleDataScope(point, controllerDataScope);
    }

    protected void handleDataScope(final JoinPoint joinPoint, DataScope controllerDataScope) {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser)) {
            if (StringUtils.isNotNull(loginUser.getUser())) {
                dataScopeFilter(joinPoint, loginUser, controllerDataScope);
            }
        }
    }

    /**
     * 数据范围过滤
     *
     * @param joinPoint           切点
     * @param loginUser           用户信息
     * @param controllerDataScope 切片数据
     */
    public static void dataScopeFilter(JoinPoint joinPoint, LoginUser loginUser, DataScope controllerDataScope) {
        SysEnterpriseDto enterprise = loginUser.getEnterprise();
        SysUserDto user = loginUser.getUser();

        String deptAlias = controllerDataScope.deptAlias();
        String postAlias = controllerDataScope.postAlias();
        String userAlias = controllerDataScope.userAlias();

        //默认只获取第一个参数
        Object params = joinPoint.getArgs()[0];
        StringBuilder sqlString = new StringBuilder();
        StringBuilder upSqlString = new StringBuilder();
    }

    /**
     * 拼接权限sql前先清空params.dataScope参数防止注入
     */
    private void clearDataScope(final JoinPoint joinPoint) {
        Object params = joinPoint.getArgs()[0];
        if (StringUtils.isNotNull(params) && params instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) params;
            baseEntity.getParams().put(DATA_SCOPE, "");
            baseEntity.getParams().put(UPDATE_SCOPE, "");
        }
    }
}