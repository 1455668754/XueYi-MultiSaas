package com.xueyi.auth.service;

import com.xueyi.common.core.web.model.BaseLoginUser;

/**
 * 日志记录 服务层
 *
 * @author xueyi
 */
public interface ISysLogService {

    /**
     * 记录登录信息 | 无企业信息
     *
     * @param enterpriseName 企业名称
     * @param userName       用户名
     * @param status         状态
     * @param message        消息内容
     */
    void recordLoginInfo(String enterpriseName, String userName, String status, String message);

    /**
     * 记录登录信息 | 无用户信息
     *
     * @param sourceName     索引数据源源
     * @param enterpriseId   企业Id
     * @param enterpriseName 企业名称
     * @param userName       用户名
     * @param status         状态
     * @param message        消息内容
     */
    void recordLoginInfo(String sourceName, Long enterpriseId, String enterpriseName, String userName, String status, String message);

    /**
     * 记录登录信息
     *
     * @param loginUser 用户登录信息
     * @param status    状态
     * @param message   消息内容
     */
    <LoginUser extends BaseLoginUser<?>> void recordLoginInfo(LoginUser loginUser, String status, String message);

    /**
     * 记录登录信息
     *
     * @param sourceName     索引数据源源
     * @param enterpriseId   企业Id
     * @param enterpriseName 企业名称
     * @param userId         用户Id
     * @param userName       用户名
     * @param status         状态
     * @param message        消息内容
     */
    void recordLoginInfo(String sourceName, Long enterpriseId, String enterpriseName, Long userId, String userName, String userNick, String status, String message);

}
