package com.xueyi.system.organize.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.system.organize.manager.SysUserManager;
import com.xueyi.system.organize.mapper.SysUserMapper;
import com.xueyi.system.organize.service.ISysUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.xueyi.common.core.constant.basic.TenantConstants.ISOLATE;

/**
 * 用户管理 服务层处理
 *
 * @author xueyi
 */
@Service
@DS(ISOLATE)
public class SysUserServiceImpl extends BaseServiceImpl<SysUserDto, SysUserManager, SysUserMapper> implements ISysUserService {

    /**
     * 用户登录校验 | 查询用户信息
     *
     * @param userName 用户账号
     * @param password 密码
     * @return 用户对象
     */
    @Override
    public SysUserDto userLogin(String userName, String password) {
        return baseManager.userLogin(userName, password);
    }

    /**
     * 新增用户 | 内部调用
     *
     * @param user 用户对象
     * @return 结果
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int addInner(SysUserDto user) {
        return baseManager.insert(user);
    }

    /**
     * 修改用户基本信息
     *
     * @param id       用户Id
     * @param nickName 用户昵称
     * @param sex      用户性别
     * @param profile  个人简介
     * @return 结果
     */
    @Override
    public int updateUserProfile(Long id, String nickName, String sex, String profile) {
        return baseManager.updateUserProfile(id, nickName, sex, profile);
    }

    /**
     * 更新用户邮箱
     *
     * @param id    用户Id
     * @param email 邮箱
     * @return 结果
     */
    @Override
    public int updateEmail(Long id, String email) {
        return baseManager.updateEmail(id, email);
    }

    /**
     * 更新用户手机号
     *
     * @param id    用户Id
     * @param phone 手机号
     * @return 结果
     */
    @Override
    public int updatePhone(Long id, String phone) {
        return baseManager.updatePhone(id, phone);
    }

    /**
     * 修改用户头像
     *
     * @param Id     用户Id
     * @param avatar 头像地址
     * @return 结果
     */
    @Override
    public int updateUserAvatar(Long Id, String avatar) {
        return baseManager.updateUserAvatar(Id, avatar);
    }

    /**
     * 重置用户密码
     *
     * @param Id       用户Id
     * @param password 密码
     * @return 结果
     */
    @Override
    public int resetUserPassword(Long Id, String password) {
        return baseManager.resetUserPassword(Id, password);
    }

    /**
     * 校验用户编码是否唯一
     *
     * @param Id       用户Id
     * @param userCode 用户编码
     * @return 结果 | true/false 唯一/不唯一
     */
    @Override
    public boolean checkUserCodeUnique(Long Id, String userCode) {
        return ObjectUtil.isNotNull(baseManager.checkUserCodeUnique(ObjectUtil.isNull(Id) ? BaseConstants.NONE_ID : Id, userCode));
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param Id    用户Id
     * @param phone 手机号码
     * @return 结果 | true/false 唯一/不唯一
     */
    @Override
    public boolean checkPhoneUnique(Long Id, String phone) {
        return ObjectUtil.isNotNull(baseManager.checkPhoneUnique(ObjectUtil.isNull(Id) ? BaseConstants.NONE_ID : Id, phone));
    }

    /**
     * 校验email是否唯一
     *
     * @param Id    用户Id
     * @param email email
     * @return 结果 | true/false 唯一/不唯一
     */
    @Override
    public boolean checkEmailUnique(Long Id, String email) {
        return ObjectUtil.isNotNull(baseManager.checkEmailUnique(ObjectUtil.isNull(Id) ? BaseConstants.NONE_ID : Id, email));
    }

    /**
     * 校验用户是否允许操作
     *
     * @param Id 用户Id
     * @return 结果 | true/false 允许/禁止
     */
    @Override
    public boolean checkUserAllowed(Long Id) {
        SysUserDto user = baseManager.selectById(Id);
        return SysUserDto.isNotAdmin(user.getUserType());
    }
}
