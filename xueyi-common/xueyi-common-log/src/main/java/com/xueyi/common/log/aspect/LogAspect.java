package com.xueyi.common.log.aspect;

import com.alibaba.fastjson2.JSON;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.TenantConstants;
import com.xueyi.common.core.utils.core.ArrayUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.utils.ip.IpUtil;
import com.xueyi.common.core.utils.servlet.ServletUtil;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessStatus;
import com.xueyi.common.log.filter.PropertyPreExcludeFilter;
import com.xueyi.common.log.service.AsyncLogService;
import com.xueyi.common.security.service.TokenService;
import com.xueyi.system.api.log.domain.dto.SysOperateLogDto;
import com.xueyi.system.api.model.LoginUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Map;

/**
 * 操作日志记录处理
 *
 * @author xueyi
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    /** 排除敏感属性字段 */
    public static final String[] EXCLUDE_PROPERTIES = {"password", "oldPassword", "newPassword", "confirmPassword"};

    /** 计算操作消耗时间 */
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new NamedThreadLocal<>("Cost Time");

    @Autowired
    private AsyncLogService asyncLogService;

    @Autowired
    private TokenService tokenService;

    /**
     * 处理请求前执行
     */
    @Before(value = "@annotation(controllerLog)")
    public void boBefore(JoinPoint joinPoint, Log controllerLog) {
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        handleLog(joinPoint, controllerLog, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, Log controllerLog, final Exception e, Object jsonResult) {
        try {
            // *========数据库日志=========*//
            SysOperateLogDto operateLog = new SysOperateLogDto();
            operateLog.setStatus(BusinessStatus.SUCCESS.getCode());
            // 请求的地址
            String ip = IpUtil.getIpAddr();
            operateLog.setIp(ip);

            operateLog.setUrl(StrUtil.sub(ServletUtil.getRequest().getRequestURI(), 0, 255));
            LoginUser loginUser = tokenService.getLoginUser();
            String sourceName = ObjectUtil.isNotNull(loginUser) ? loginUser.getSourceName() : null;
            Long userId = ObjectUtil.isNotNull(loginUser) ? loginUser.getUserId() : null;
            Long enterpriseId = ObjectUtil.isNotNull(loginUser) ? loginUser.getEnterpriseId() : null;
            String userName = ObjectUtil.isNotNull(loginUser) && ObjectUtil.isNotNull(loginUser.getUser())
                    ? loginUser.getUser().getUserName() : StrUtil.EMPTY;
            String userNick = ObjectUtil.isNotNull(loginUser) && ObjectUtil.isNotNull(loginUser.getUser())
                    ? loginUser.getUser().getNickName() : StrUtil.EMPTY;
            operateLog.setSourceName(StrUtil.isNotEmpty(sourceName) ? sourceName : TenantConstants.Source.SLAVE.getCode());
            operateLog.setUserId(ObjectUtil.isNotNull(userId) ? userId : SecurityConstants.EMPTY_USER_ID);
            operateLog.setUserName(userName);
            operateLog.setUserNick(userNick);
            operateLog.setEnterpriseId(ObjectUtil.isNotNull(enterpriseId) ? enterpriseId : SecurityConstants.EMPTY_TENANT_ID);
            if (e != null) {
                operateLog.setStatus(BusinessStatus.FAIL.getCode());
                operateLog.setErrorMsg(StrUtil.sub(e.getMessage(), 0, 2000));
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operateLog.setMethod(className + StrUtil.DOT + methodName + StrUtil.PARENTHESES);
            // 设置请求方式
            operateLog.setRequestMethod(ServletUtil.getRequest().getMethod());
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, controllerLog, operateLog, jsonResult);
            // 设置消耗时间
            operateLog.setCostTime(System.currentTimeMillis() - TIME_THREADLOCAL.get());
            // 传递线程信息
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            RequestContextHolder.setRequestAttributes(servletRequestAttributes, true);
            // 保存数据库
            asyncLogService.saveOperateLog(operateLog);
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        } finally {
            TIME_THREADLOCAL.remove();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param log          日志
     * @param operationLog 操作日志
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, Log log, SysOperateLogDto operationLog, Object jsonResult) throws Exception {
        // 设置action动作
        operationLog.setBusinessType(String.valueOf(log.businessType().getCode()));
        // 设置标题
        operationLog.setTitle(log.title());
        // 设置操作人类别
        operationLog.setOperateType(String.valueOf(log.operatorType().getCode()));
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData()) {
            // 获取参数的信息，传入到数据库中
            setRequestValue(joinPoint, operationLog, log.excludeParamNames());
        }
        // 是否需要保存response，参数和值
        if (log.isSaveResponseData() && ObjectUtil.isNotNull(jsonResult)) {
            operationLog.setJsonResult(StrUtil.sub(JSON.toJSONString(jsonResult), 0, 2000));
        }
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param operateLog 操作日志
     * @param excludeParamNames 排除指定的请求参数
     * @throws Exception 异常
     */
    private void setRequestValue(JoinPoint joinPoint, SysOperateLogDto operateLog, String[] excludeParamNames) throws Exception {
        String requestMethod = operateLog.getRequestMethod();
        if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
            String params = argsArrayToString(joinPoint.getArgs(), excludeParamNames);
            operateLog.setParam(StrUtil.sub(params, 0, 2000));
        } else {
            Map<?, ?> paramsMap = ServletUtil.getParamMap(ServletUtil.getRequest());
            operateLog.setParam(StrUtil.sub(JSON.toJSONString(paramsMap, excludePropertyPreFilter(excludeParamNames)), 0, 2000));
        }
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray, String[] excludeParamNames) {
        StringBuilder params = new StringBuilder();
        if (ArrayUtil.isNotEmpty(paramsArray)) {
            for (Object o : paramsArray) {
                if (ObjectUtil.isNotNull(o) && !isFilterObject(o)) {
                    try {
                        String jsonObj = JSON.toJSONString(o, excludePropertyPreFilter(excludeParamNames));
                        params.append(jsonObj).append(StrUtil.SPACE);
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        return params.toString().trim();
    }

    /**
     * 忽略敏感属性
     */
    public PropertyPreExcludeFilter excludePropertyPreFilter(String[] excludeParamNames) {
        return new PropertyPreExcludeFilter().addExcludes(ArrayUtil.addAll(EXCLUDE_PROPERTIES, excludeParamNames));
    }

    /**
     * 判断是否需要过滤的对象
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }
}
