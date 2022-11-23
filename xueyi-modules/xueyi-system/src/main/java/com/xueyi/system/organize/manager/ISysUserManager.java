package com.xueyi.system.organize.manager;

import com.xueyi.common.web.entity.manager.IBaseManager;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.system.api.organize.domain.query.SysUserQuery;

import java.io.Serializable;

/**
 * 用户管理 数据封装层
 *
 * @author xueyi
 */
public interface ISysUserManager extends IBaseManager<SysUserQuery, SysUserDto> {

    /**
     * 用户登录校验 | 查询用户信息
     *
     * @param userName 用户账号
     * @param password 用户密码
     * @return 用户对象
     */
    SysUserDto userLogin(String userName, String password);

    /**
     * 修改用户基本信息
     *
     * @param id       用户Id
     * @param nickName 用户昵称
     * @param sex      用户性别
     * @param profile  个人简介
     * @return 结果
     */
     int updateUserProfile(Long id, String nickName, String sex, String profile);

    /**
     * 更新用户账号
     *
     * @param id       用户Id
     * @param userName 用户账号
     * @return 结果
     */
     int updateUserName(Long id, String userName);

    /**
     * 更新用户邮箱
     *
     * @param id    用户Id
     * @param email 邮箱
     * @return 结果
     */
     int updateEmail(Long id, String email);

    /**
     * 更新用户手机号
     *
     * @param id    用户Id
     * @param phone 手机号
     * @return 结果
     */
     int updatePhone(Long id, String phone);

    /**
     * 修改用户头像
     *
     * @param id     用户Id
     * @param avatar 头像地址
     * @return 结果
     */
     int updateUserAvatar(Long id, String avatar);

    /**
     * 重置用户密码
     *
     * @param id       用户Id
     * @param password 密码
     * @return 结果
     */
     int resetUserPassword(Long id, String password);

    /**
     * 校验用户编码是否唯一
     *
     * @param id   用户Id
     * @param code 用户编码
     * @return 用户对象
     */
     SysUserDto checkUserCodeUnique(Long id, String code);

    /**
     * 校验用户账号是否唯一
     *
     * @param id       Id
     * @param userName 用户账号
     * @return 结果 | true/false 唯一/不唯一
     */
     SysUserDto checkUserNameUnique(Serializable id, String userName);

    /**
     * 校验手机号码是否唯一
     *
     * @param id    用户Id
     * @param phone 手机号码
     * @return 用户对象
     */
     SysUserDto checkPhoneUnique(Long id, String phone);

    /**
     * 校验email是否唯一
     *
     * @param id    用户Id
     * @param email email
     * @return 用户对象
     */
     SysUserDto checkEmailUnique(Long id, String email);
}
