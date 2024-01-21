package com.xueyi.common.web.entity.controller;

import com.github.pagehelper.PageInfo;
import com.xueyi.common.core.utils.core.LocalDateTimeUtil;
import com.xueyi.common.core.utils.page.PageUtil;
import com.xueyi.common.core.web.page.TableDataInfo;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.web.correlate.service.CorrelateService;
import com.xueyi.common.web.correlate.utils.CorrelateUtil;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * web层 通用数据处理
 *
 * @author xueyi
 */
public class BasisController {

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // LocalDateTime 类型转换
        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(LocalDateTimeUtil.parseDateTime(text));
            }
        });
        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(LocalDateTimeUtil.parseDate(text));
            }
        });
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage() {
        PageUtil.startPage();
    }

    /**
     * 清理分页的线程变量
     */
    protected void clearPage() {
        PageUtil.clearPage();
    }

    /**
     * 设置请求关联映射
     */
    protected <C extends Enum<? extends Enum<?>> & CorrelateService> void startCorrelates(C correlateEnum) {
        CorrelateUtil.startCorrelates(correlateEnum);
    }

    /**
     * 清理关联映射的线程变量
     */
    protected void clearCorrelates() {
        CorrelateUtil.clearCorrelates();
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected AjaxResult getDataTable(List<?> list) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setItems(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return success(rspData);
    }

    /**
     * 返回成功消息
     */
    public AjaxResult success() {
        return AjaxResult.success();
    }

    /**
     * 返回成功消息
     */
    public AjaxResult success(String message) {
        return AjaxResult.success(message);
    }

    /**
     * 返回成功消息
     */
    public AjaxResult success(Object data) {
        return AjaxResult.success(data);
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error() {
        return AjaxResult.error();
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error(String message) {
        return AjaxResult.error(message);
    }

    /**
     * 返回警告消息
     */
    public AjaxResult warn(String message) {
        return AjaxResult.warn(message);
    }

    /**
     * 返回警告消息
     */
    public AjaxResult warn(String message, Integer code) {
        return AjaxResult.warn(message, code);
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows) {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    protected AjaxResult toAjax(boolean result) {
        return result ? success() : error();
    }
}
