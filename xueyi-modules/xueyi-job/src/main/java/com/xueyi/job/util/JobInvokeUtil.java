package com.xueyi.job.util;

import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.NumberUtil;
import com.xueyi.common.core.utils.core.SpringUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.job.api.domain.dto.SysJobDto;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * 任务执行工具
 *
 * @author xueyi
 */
public class JobInvokeUtil {

    /**
     * 执行方法
     *
     * @param sysJob 系统任务
     */
    public static void invokeMethod(SysJobDto sysJob) throws Exception {
        String invokeTarget = sysJob.getInvokeTarget();
        String invokeTenant = sysJob.getInvokeTenant();
        String beanName = getBeanName(invokeTarget);
        String methodName = getMethodName(invokeTarget);
        List<Object[]> methodParams = getMethodParams(invokeTarget, invokeTenant);

        if (!isValidClassName(beanName)) {
            Object bean = SpringUtil.getBean(beanName);
            invokeMethod(bean, methodName, methodParams);
        } else {
            Object bean = Class.forName(beanName).newInstance();
            invokeMethod(bean, methodName, methodParams);
        }
    }

    /**
     * 调用任务方法
     *
     * @param bean         目标对象
     * @param methodName   方法名称
     * @param methodParams 方法参数
     */
    private static void invokeMethod(Object bean, String methodName, List<Object[]> methodParams)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        if (CollUtil.isNotEmpty(methodParams)) {
            Method method = bean.getClass().getMethod(methodName, getMethodParamsType(methodParams));
            method.invoke(bean, getMethodParamsValue(methodParams));
        } else {
            Method method = bean.getClass().getMethod(methodName);
            method.invoke(bean);
        }
    }

    /**
     * 校验是否为为class包名
     *
     * @param invokeTarget 名称
     * @return true是 false否
     */
    public static boolean isValidClassName(String invokeTarget) {
        return StrUtil.count(invokeTarget, ".") > NumberUtil.One;
    }

    /**
     * 获取bean名称
     *
     * @param invokeTarget 目标字符串
     * @return bean名称
     */
    public static String getBeanName(String invokeTarget) {
        String beanName = StrUtil.subBefore(invokeTarget, StrUtil.PARENTHESES_START);
        return StrUtil.subBeforeLast(beanName, StrUtil.DOT);
    }

    /**
     * 获取bean方法
     *
     * @param invokeTarget 目标字符串
     * @return method方法
     */
    public static String getMethodName(String invokeTarget) {
        String methodName = StrUtil.subBefore(invokeTarget, StrUtil.PARENTHESES_START);
        return StrUtil.subAfterLast(methodName, StrUtil.DOT);
    }

    /**
     * 获取method方法参数相关列表
     *
     * @param invokeTarget 目标字符串
     * @return method方法相关参数列表
     */
    public static List<Object[]> getMethodParams(String invokeTarget, String invokeTenant) {
        String targetStr = StrUtil.subBetween(invokeTarget, StrUtil.PARENTHESES_START, StrUtil.PARENTHESES_END);
        String methodStr = StrUtil.isNotEmpty(targetStr) ? StrUtil.concat(true, invokeTenant, StrUtil.COMMA, targetStr) : invokeTenant;
        String[] methodParams = methodStr.split(",(?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)");
        List<Object[]> clazz = new LinkedList<>();
        for (String methodParam : methodParams) {
            String str = StrUtil.trimToEmpty(methodParam);
            // String字符串类型，以'或"开头
            if (StrUtil.startWithAny(str, "'", "\"")) {
                clazz.add(new Object[]{StrUtil.sub(str, NumberUtil.One, str.length() - NumberUtil.One), String.class});
            }
            // boolean布尔类型，等于true或者false
            else if (StrUtil.TRUE.equalsIgnoreCase(str) || StrUtil.FALSE.equalsIgnoreCase(str)) {
                clazz.add(new Object[]{Boolean.valueOf(str), Boolean.class});
            }
            // long长整形，以L结尾
            else if (StrUtil.endWith(str, "L")) {
                clazz.add(new Object[]{Long.valueOf(StrUtil.sub(str, NumberUtil.Zero, str.length() - NumberUtil.One)), Long.class});
            }
            // double浮点类型，以D结尾
            else if (StrUtil.endWith(str, "D")) {
                clazz.add(new Object[]{Double.valueOf(StrUtil.sub(str, NumberUtil.Zero, str.length() - NumberUtil.One)), Double.class});
            }
            // 其他类型归类为整形
            else {
                clazz.add(new Object[]{Integer.valueOf(str), Integer.class});
            }
        }
        return clazz;
    }

    /**
     * 获取参数类型
     *
     * @param methodParams 参数相关列表
     * @return 参数类型列表
     */
    public static Class<?>[] getMethodParamsType(List<Object[]> methodParams) {
        Class<?>[] clazz = new Class<?>[methodParams.size()];
        int index = NumberUtil.Zero;
        for (Object[] os : methodParams) {
            clazz[index] = (Class<?>) os[NumberUtil.One];
            index++;
        }
        return clazz;
    }

    /**
     * 获取参数值
     *
     * @param methodParams 参数相关列表
     * @return 参数值列表
     */
    public static Object[] getMethodParamsValue(List<Object[]> methodParams) {
        Object[] clazz = new Object[methodParams.size()];
        int index = NumberUtil.Zero;
        for (Object[] os : methodParams) {
            clazz[index] = (Object) os[NumberUtil.Zero];
            index++;
        }
        return clazz;
    }
}
