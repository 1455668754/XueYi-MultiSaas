package com.xueyi.system.organize.service;

import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.system.api.domain.organize.dto.SysUserDto;

/**
 * 用户管理 服务层
 *
 * @author xueyi
 */
public interface ISysUserService extends IBaseService<SysUserDto> {

    /**
     * 用户登录校验 | 查询用户信息
     *
     * @param userName     用户账号
     * @param password     密码
     * @param enterpriseId 企业Id
     * @param sourceName   策略源
     * @return 用户对象
     */
    SysUserDto userLogin(String userName, String password, Long enterpriseId, String sourceName);

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    int updateUserProfile(SysUserDto user);

    /**
     * 修改用户头像
     *
     * @param Id     用户Id
     * @param avatar 头像地址
     * @return 结果
     */
    int updateUserAvatar(Long Id, String avatar);

    /**
     * 重置用户密码
     *
     * @param Id       用户Id
     * @param password 密码
     * @return 结果
     */
    int resetUserPassword(Long Id, String password);

    /**
     * 校验用户编码是否唯一
     *
     * @param Id       用户Id
     * @param userCode 用户编码
     * @return 结果 | true/false 唯一/不唯一
     */
    boolean checkUserCodeUnique(Long Id, String userCode);

    /**
     * 校验手机号码是否唯一
     *
     * @param Id    用户Id
     * @param phone 手机号码
     * @return 结果 | true/false 唯一/不唯一
     */
    boolean checkPhoneUnique(Long Id, String phone);

    /**
     * 校验email是否唯一
     *
     * @param Id    用户Id
     * @param email email
     * @return 结果 | true/false 唯一/不唯一
     */
    boolean checkEmailUnique(Long Id, String email);

    /**
     * 校验用户是否允许操作
     *
     * @param Id 用户Id
     * @return 结果 | true/false 允许/禁止
     */
    boolean checkUserAllowed(Long Id);
}
