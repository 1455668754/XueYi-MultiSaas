package com.xueyi.common.core.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.xueyi.common.core.constant.basic.DictConstants;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.utils.core.ConvertUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * 获取当前线程变量中的 企业Id | 企业账号 | 租户类型 | 用户id | 用户账号 | 用户昵称 | 用户类型 | Token等信息
 * 注意： 必须在网关通过请求头的方法传入，同时在HeaderInterceptor拦截器设置值。 否则这里无法获取
 *
 * @author xueyi
 */
public class SecurityContextHolder {

    private static final TransmittableThreadLocal<Map<String, Object>> THREAD_LOCAL = new TransmittableThreadLocal<>();

    /**
     * 获取企业Id
     */
    public static Long getEnterpriseId() {
        return ConvertUtil.toLong(get(SecurityConstants.BaseSecurity.ENTERPRISE_ID.getCode()), SecurityConstants.EMPTY_TENANT_ID);
    }

    /**
     * 设置企业Id
     */
    public static void setEnterpriseId(String enterpriseId) {
        Long lastEnterpriseId = getEnterpriseId();
        set(SecurityConstants.BaseSecurity.ENTERPRISE_ID.getCode(), enterpriseId);
        setLastEnterpriseId(lastEnterpriseId);
    }

    /**
     * 记录上一次企业Id
     */
    private static void setLastEnterpriseId(Long enterpriseId) {
        if (ObjectUtil.isNotNull(enterpriseId) && ObjectUtil.notEqual(SecurityConstants.EMPTY_TENANT_ID, enterpriseId)) {
            set(SecurityConstants.BaseSecurity.LAST_ENTERPRISE_ID.getCode(), enterpriseId.toString());
        }
    }

    /**
     * 回滚上一次企业Id | 如若无上一次则不变更
     */
    public static void rollLastEnterpriseId() {
        String lastEnterpriseId = get(SecurityConstants.BaseSecurity.LAST_ENTERPRISE_ID.getCode());
        if (StrUtil.isNotBlank(lastEnterpriseId)) {
            setEnterpriseId(lastEnterpriseId);
        }
    }

    /**
     * 指定租户方法执行 | Supplier
     *
     * @param enterpriseId 租户Id
     * @param supplier     SupplierFun
     * @return 返回结果
     */
    public static <T> T setEnterpriseIdFun(String enterpriseId, Supplier<T> supplier) {
        return setEnterpriseIdFun(enterpriseId, supplier, Boolean.TRUE);
    }

    /**
     * 指定租户方法执行 | Runnable
     *
     * @param enterpriseId 租户Id
     * @param supplier     SupplierFun
     * @param isExecute    是否执行控制
     */
    public static <T> T setEnterpriseIdFun(String enterpriseId, Supplier<T> supplier, Boolean isExecute) {
        if (isExecute) {
            setEnterpriseId(enterpriseId);
        }
        T info = supplier.get();
        if (isExecute) {
            rollLastEnterpriseId();
        }
        return info;
    }

    /**
     * 指定租户方法执行 | Runnable
     *
     * @param enterpriseId 租户Id
     * @param runnable     RunnableFun
     */
    public static void setEnterpriseIdFun(String enterpriseId, Runnable runnable) {
        setEnterpriseIdFun(enterpriseId, runnable, Boolean.TRUE);
    }

    /**
     * 指定租户方法执行 | Runnable
     *
     * @param enterpriseId 租户Id
     * @param runnable     RunnableFun
     * @param isExecute    是否执行控制
     */
    public static void setEnterpriseIdFun(String enterpriseId, Runnable runnable, Boolean isExecute) {
        if (isExecute) {
            setEnterpriseId(enterpriseId);
        }
        runnable.run();
        if (isExecute) {
            rollLastEnterpriseId();
        }
    }

    /**
     * 获取企业名称
     */
    public static String getEnterpriseName() {
        return get(SecurityConstants.BaseSecurity.ENTERPRISE_NAME.getCode());
    }

    /**
     * 设置企业名称
     */
    public static void setEnterpriseName(String enterpriseName) {
        set(SecurityConstants.BaseSecurity.ENTERPRISE_NAME.getCode(), enterpriseName);
    }

    /**
     * 获取租户权限标识
     */
    public static String getIsLessor() {
        return get(SecurityConstants.BaseSecurity.IS_LESSOR.getCode());
    }

    /**
     * 设置租户权限标识
     */
    public static void setIsLessor(String isLessor) {
        set(SecurityConstants.BaseSecurity.IS_LESSOR.getCode(), isLessor);
    }

    /**
     * 获取用户Id
     */
    public static Long getUserId() {
        return ConvertUtil.toLong(get(SecurityConstants.BaseSecurity.USER_ID.getCode()), SecurityConstants.EMPTY_USER_ID);
    }

    /**
     * 设置用户Id
     */
    public static void setUserId(String userId) {
        set(SecurityConstants.BaseSecurity.USER_ID.getCode(), userId);
    }

    /**
     * 获取用户名称
     */
    public static String getUserName() {
        return get(SecurityConstants.BaseSecurity.USER_NAME.getCode());
    }

    /**
     * 设置用户名称
     */
    public static void setUserName(String userName) {
        set(SecurityConstants.BaseSecurity.USER_NAME.getCode(), userName);
    }

    /**
     * 获取用户昵称
     */
    public static String getNickName() {
        return get(SecurityConstants.BaseSecurity.NICK_NAME.getCode());
    }

    /**
     * 设置用户昵称
     */
    public static void setNickName(String nickName) {
        set(SecurityConstants.BaseSecurity.NICK_NAME.getCode(), nickName);
    }

    /**
     * 获取用户权限标识
     */
    public static String getUserType() {
        return get(SecurityConstants.BaseSecurity.USER_TYPE.getCode());
    }

    /**
     * 设置用户权限标识
     */
    public static void setUserType(String userType) {
        set(SecurityConstants.BaseSecurity.USER_TYPE.getCode(), userType);
    }

    /**
     * 获取用户令牌
     */
    public static String getAccessToken() {
        return get(SecurityConstants.BaseSecurity.ACCESS_TOKEN.getCode());
    }

    /**
     * 设置用户令牌
     */
    public static void setAccessToken(String accessToken) {
        set(SecurityConstants.BaseSecurity.ACCESS_TOKEN.getCode(), accessToken);
    }

    /**
     * 获取租户策略源
     */
    public static String getSourceName() {
        return get(SecurityConstants.BaseSecurity.SOURCE_NAME.getCode());
    }

    /**
     * 设置租户策略源
     */
    public static void setSourceName(String sourceName) {
        String lastSourceName = getSourceName();
        set(SecurityConstants.BaseSecurity.SOURCE_NAME.getCode(), sourceName);
        setLastSourceName(lastSourceName);
    }

    /**
     * 记录上一次租户策略源
     */
    private static void setLastSourceName(String sourceName) {
        if (StrUtil.isNotBlank(sourceName)) {
            set(SecurityConstants.BaseSecurity.LAST_SOURCE_NAME.getCode(), sourceName);
        }
    }

    /**
     * 回滚上一次租户策略源 | 如若无上一次则不变更
     */
    public static void rollLastSourceName() {
        String lastSourceName = get(SecurityConstants.BaseSecurity.LAST_SOURCE_NAME.getCode());
        if (StrUtil.isNotBlank(lastSourceName)) {
            setSourceName(lastSourceName);
        }
    }

    /**
     * 指定租户策略源方法执行 | Supplier
     *
     * @param sourceName 策略源名称
     * @param supplier   SupplierFun
     * @return 返回结果
     */
    public static <T> T setSourceNameFun(String sourceName, Supplier<T> supplier) {
        return setSourceNameFun(sourceName, supplier, Boolean.TRUE);
    }

    /**
     * 指定租户策略源方法执行 | Supplier
     *
     * @param sourceName 策略源名称
     * @param supplier   SupplierFun
     * @param isExecute  是否执行控制
     * @return 返回结果
     */
    public static <T> T setSourceNameFun(String sourceName, Supplier<T> supplier, Boolean isExecute) {
        if (isExecute) {
            setSourceName(sourceName);
        }
        T info = supplier.get();
        if (isExecute) {
            rollLastSourceName();
        }
        return info;
    }

    /**
     * 指定租户策略源方法执行 | Runnable
     *
     * @param sourceName 策略源名称
     * @param runnable   RunnableFun
     */
    public static void setSourceNameFun(String sourceName, Runnable runnable) {
        setSourceNameFun(sourceName, runnable, Boolean.TRUE);
    }

    /**
     * 指定租户策略源方法执行 | Runnable
     *
     * @param sourceName 策略源名称
     * @param runnable   RunnableFun
     * @param isExecute  是否执行控制
     */
    public static void setSourceNameFun(String sourceName, Runnable runnable, Boolean isExecute) {
        if (isExecute) {
            setSourceName(sourceName);
        }
        runnable.run();
        if (isExecute) {
            rollLastSourceName();
        }
    }

    /**
     * 获取账户类型
     */
    public static String getAccountType() {
        return get(SecurityConstants.BaseSecurity.ACCOUNT_TYPE.getCode());
    }

    /**
     * 设置账户类型
     */
    public static void setAccountType(String accountType) {
        set(SecurityConstants.BaseSecurity.ACCOUNT_TYPE.getCode(), accountType);
    }

    /**
     * 获取用户key
     */
    public static String getUserKey() {
        return get(SecurityConstants.BaseSecurity.USER_KEY.getCode());
    }

    /**
     * 设置用户key
     */
    public static void setUserKey(String userKey) {
        set(SecurityConstants.BaseSecurity.USER_KEY.getCode(), userKey);
    }

    /**
     * 获取租户忽略控制
     */
    public static Boolean getTenantIgnore() {
        return ObjectUtil.equals(DictConstants.DicYesNo.YES.getCode(), get(SecurityConstants.BaseSecurity.TENANT_IGNORE.getCode()));
    }

    /**
     * 设置租户忽略控制
     */
    public static void setTenantIgnore(String tenantIgnore) {
        if (ObjectUtil.equals(DictConstants.DicYesNo.YES.getCode(), tenantIgnore)) {
            setTenantIgnore();
        }
    }

    /**
     * 设置租户忽略控制
     */
    public static void setTenantIgnore() {
        set(SecurityConstants.BaseSecurity.TENANT_IGNORE.getCode(), DictConstants.DicYesNo.YES.getCode());
    }

    /**
     * 移除租户忽略控制
     */
    public static void clearTenantIgnore() {
        set(SecurityConstants.BaseSecurity.TENANT_IGNORE.getCode(), DictConstants.DicYesNo.NO.getCode());
    }

    /**
     * 指定租户忽略控制方法执行 | Supplier
     *
     * @param supplier SupplierFun
     * @return 返回结果
     */
    public static <T> T setTenantIgnoreFun(Supplier<T> supplier) {
        return setTenantIgnoreFun(supplier, Boolean.TRUE);
    }

    /**
     * 指定租户忽略控制方法执行 | Supplier
     *
     * @param supplier  SupplierFun
     * @param isExecute 是否执行控制
     * @return 返回结果
     */
    public static <T> T setTenantIgnoreFun(Supplier<T> supplier, Boolean isExecute) {
        if (isExecute) {
            setTenantIgnore();
        }
        T info = supplier.get();
        if (isExecute) {
            clearTenantIgnore();
        }
        return info;
    }

    /**
     * 指定租户忽略控制方法执行 | Runnable
     *
     * @param runnable RunnableFun
     */
    public static void setTenantIgnoreFun(Runnable runnable) {
        setTenantIgnoreFun(runnable, Boolean.TRUE);
    }

    /**
     * 指定租户忽略控制方法执行 | Runnable
     *
     * @param runnable  RunnableFun
     * @param isExecute 是否执行控制
     */
    public static void setTenantIgnoreFun(Runnable runnable, Boolean isExecute) {
        if (isExecute) {
            setTenantIgnore();
        }
        runnable.run();
        if (isExecute) {
            clearTenantIgnore();
        }
    }

    public static void set(String key, Object value) {
        Map<String, Object> map = getLocalMap();
        map.put(key, value == null ? StrUtil.EMPTY : value);
    }

    public static String get(String key) {
        Map<String, Object> map = getLocalMap();
        return ConvertUtil.toStr(map.getOrDefault(key, StrUtil.EMPTY));
    }

    public static <T> T get(String key, Class<T> clazz) {
        Map<String, Object> map = getLocalMap();
        return ConvertUtil.convert(clazz, map.getOrDefault(key, null));
    }

    public static Map<String, Object> getLocalMap() {
        Map<String, Object> map = THREAD_LOCAL.get();
        if (map == null) {
            map = new ConcurrentHashMap<>();
            THREAD_LOCAL.set(map);
        }
        return map;
    }

    public static void setLocalMap(Map<String, Object> threadLocalMap) {
        THREAD_LOCAL.set(threadLocalMap);
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
