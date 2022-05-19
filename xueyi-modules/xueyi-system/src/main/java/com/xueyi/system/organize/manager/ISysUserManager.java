package com.xueyi.system.organize.manager;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.xueyi.common.web.entity.manager.IBaseManager;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.system.api.organize.domain.query.SysUserQuery;

import java.io.Serializable;
import java.util.Collection;

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
    public SysUserDto userLogin(String userName, String password);

    /**
     * 根据Id查询单条用户对象 | 附加数据
     *
     * @param id Id
     * @return 用户对象
     */
    public SysUserDto selectByIdExtra(Serializable id);

    /**
     * 新增用户对象
     *
     * @param user 用户对象
     * @return 结果
     */
    @Override
    @DSTransactional
    public int insert(SysUserDto user);

    /**
     * 修改用户对象
     *
     * @param user 用户对象
     * @return 结果
     */
    @Override
    @DSTransactional
    public int update(SysUserDto user);

    /**
     * 修改用户基本信息
     *
     * @param id       用户Id
     * @param nickName 用户昵称
     * @param sex      用户性别
     * @param profile  个人简介
     * @return 结果
     */
    public int updateUserProfile(Long id, String nickName, String sex, String profile);

    /**
     * 更新用户账号
     *
     * @param id       用户Id
     * @param userName 用户账号
     * @return 结果
     */
    public int updateUserName(Long id, String userName);

    /**
     * 更新用户邮箱
     *
     * @param id    用户Id
     * @param email 邮箱
     * @return 结果
     */
    public int updateEmail(Long id, String email);

    /**
     * 更新用户手机号
     *
     * @param id    用户Id
     * @param phone 手机号
     * @return 结果
     */
    public int updatePhone(Long id, String phone);

    /**
     * 修改用户头像
     *
     * @param id     用户Id
     * @param avatar 头像地址
     * @return 结果
     */
    public int updateUserAvatar(Long id, String avatar);

    /**
     * 重置用户密码
     *
     * @param id       用户Id
     * @param password 密码
     * @return 结果
     */
    public int resetUserPassword(Long id, String password);

    /**
     * 根据Id删除用户对象
     *
     * @param id Id
     * @return 结果
     */
    @Override
    @DSTransactional
    public int deleteById(Serializable id);

    /**
     * 根据Id集合批量删除用户对象
     *
     * @param idList Id集合
     * @return 结果
     */
    @Override
    @DSTransactional
    public int deleteByIds(Collection<? extends Serializable> idList);

    /**
     * 校验用户编码是否唯一
     *
     * @param id   用户Id
     * @param code 用户编码
     * @return 用户对象
     */
    public SysUserDto checkUserCodeUnique(Long id, String code);

    /**
     * 校验用户账号是否唯一
     *
     * @param id       Id
     * @param userName 用户账号
     * @return 结果 | true/false 唯一/不唯一
     */
    public SysUserDto checkUserNameUnique(Serializable id, String userName);

    /**
     * 校验用户名称是否唯一
     *
     * @param id       用户Id
     * @param userName 用户名称
     * @return 用户对象
     */
    @Override
    public SysUserDto checkNameUnique(Serializable id, String userName);

    /**
     * 校验手机号码是否唯一
     *
     * @param id    用户Id
     * @param phone 手机号码
     * @return 用户对象
     */
    public SysUserDto checkPhoneUnique(Long id, String phone);

    /**
     * 校验email是否唯一
     *
     * @param id    用户Id
     * @param email email
     * @return 用户对象
     */
    public SysUserDto checkEmailUnique(Long id, String email);
}
