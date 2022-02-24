package com.xueyi.common.core.web.result;

import com.xueyi.common.core.constant.basic.HttpConstants;
import com.xueyi.common.core.utils.StringUtils;

import java.util.HashMap;

/**
 * 操作消息提醒
 *
 * @author xueyi
 */
public class AjaxResult extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    /** 状态码 */
    public static final String CODE_TAG = "code";

    /** 返回内容 */
    public static final String MSG_TAG = "message";

    /** 返回类型 */
    public static final String TYPE_TAG = "type";

    /** 数据对象 */
    public static final String RESULT_TAG = "result";

    /**
     * 初始化一个新创建的 AjaxResult 对象，使其表示一个空消息。
     */
    public AjaxResult() {
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     */
    public AjaxResult(int code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param code 状态码
     * @param type 返回类型
     * @param msg  返回内容
     * @param data 数据对象
     */
    public AjaxResult(int code, String type, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(TYPE_TAG, type);
        super.put(MSG_TAG, msg);
        if (StringUtils.isNotNull(data)) {
            super.put(RESULT_TAG, data);
        }
    }

    /**
     * 方便链式调用
     *
     * @param key   键
     * @param value 值
     */
    @Override
    public AjaxResult put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static AjaxResult success() {
        return AjaxResult.success(HttpConstants.ResultType.SUCCESS.getInfo());
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static AjaxResult success(Object data) {
        return AjaxResult.success(HttpConstants.ResultType.SUCCESS.getInfo(), data);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static AjaxResult success(String msg) {
        return AjaxResult.success(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static AjaxResult success(String msg, Object data) {
        return new AjaxResult(HttpConstants.Status.SUCCESS.getCode(), HttpConstants.ResultType.SUCCESS.getCode(), msg, data);
    }

    /**
     * 返回错误消息
     *
     * @return 错误内容
     */
    public static AjaxResult error() {
        return AjaxResult.error(HttpConstants.ResultType.ERROR.getInfo());
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static AjaxResult error(String msg) {
        return AjaxResult.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static AjaxResult error(String msg, Object data) {
        return new AjaxResult(HttpConstants.Status.ERROR.getCode(), HttpConstants.ResultType.ERROR.getCode(), msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg  返回内容
     * @return 警告消息
     */
    public static AjaxResult error(int code, String msg) {
        return new AjaxResult(code, HttpConstants.ResultType.ERROR.getCode(), msg, null);
    }
}
