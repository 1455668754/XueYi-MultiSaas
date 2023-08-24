package com.xueyi.system.organize.service.impl;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.utils.core.DesensitizedUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.common.datasource.annotation.Isolate;
import com.xueyi.common.web.correlate.contant.CorrelateConstants;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.system.api.organize.domain.query.SysUserQuery;
import com.xueyi.system.organize.domain.correlate.SysUserCorrelate;
import com.xueyi.system.organize.manager.ISysUserManager;
import com.xueyi.system.organize.service.ISysUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统服务 | 组织模块 | 用户管理 服务层处理
 *
 * @author xueyi
 */
@Service
@Isolate
public class SysUserServiceImpl extends BaseServiceImpl<SysUserQuery, SysUserDto, SysUserCorrelate, ISysUserManager> implements ISysUserService {

    /**
     * 默认方法关联配置定义
     */
    @Override
    protected Map<CorrelateConstants.ServiceType, SysUserCorrelate> defaultCorrelate() {
        return new HashMap<>() {{
            put(CorrelateConstants.ServiceType.SELECT_ID_SINGLE, SysUserCorrelate.INFO_LIST);
            put(CorrelateConstants.ServiceType.ADD, SysUserCorrelate.BASE_ADD);
            put(CorrelateConstants.ServiceType.EDIT, SysUserCorrelate.BASE_EDIT);
            put(CorrelateConstants.ServiceType.DELETE, SysUserCorrelate.BASE_DEL);
        }};
    }

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
        return super.insert(user);
    }

    /**
     * 查询用户对象列表 | 数据权限 | 附加数据
     *
     * @param user 用户对象
     * @return 用户对象集合
     */
    @Override
    @DataScope(userAlias = "id", mapperScope = {"SysUserMapper"})
    public List<SysUserDto> selectListScope(SysUserQuery user) {
        List<SysUserDto> list = super.selectListScope(user);
        return subCorrelates(list, SysUserCorrelate.INFO_LIST);
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
     * 更新用户账号
     *
     * @param id       用户Id
     * @param userName 用户账号
     * @return 结果
     */
    @Override
    public int updateUserName(Long id, String userName) {
        return baseManager.updateUserName(id, userName);
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
     * @param id     用户Id
     * @param avatar 头像地址
     * @return 结果
     */
    @Override
    public int updateUserAvatar(Long id, String avatar) {
        return baseManager.updateUserAvatar(id, avatar);
    }

    /**
     * 重置用户密码
     *
     * @param id       用户Id
     * @param password 密码
     * @return 结果
     */
    @Override
    public int resetUserPassword(Long id, String password) {
        return baseManager.resetUserPassword(id, password);
    }

    /**
     * 校验用户账号是否唯一
     *
     * @param id       Id
     * @param userName 用户账号
     * @return 结果 | true/false 唯一/不唯一
     */
    @Override
    public boolean checkUserNameUnique(Serializable id, String userName) {
        return ObjectUtil.isNotNull(baseManager.checkUserNameUnique(ObjectUtil.isNull(id) ? BaseConstants.NONE_ID : id, userName));
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param id    用户Id
     * @param phone 手机号码
     * @return 结果 | true/false 唯一/不唯一
     */
    @Override
    public boolean checkPhoneUnique(Long id, String phone) {
        return ObjectUtil.isNotNull(baseManager.checkPhoneUnique(ObjectUtil.isNull(id) ? BaseConstants.NONE_ID : id, phone));
    }

    /**
     * 校验email是否唯一
     *
     * @param id    用户Id
     * @param email email
     * @return 结果 | true/false 唯一/不唯一
     */
    @Override
    public boolean checkEmailUnique(Long id, String email) {
        return ObjectUtil.isNotNull(baseManager.checkEmailUnique(ObjectUtil.isNull(id) ? BaseConstants.NONE_ID : id, email));
    }

    /**
     * 校验用户是否允许操作
     *
     * @param id 用户Id
     * @return 结果 | true/false 允许/禁止
     */
    @Override
    public boolean checkUserAllowed(Long id) {
        SysUserDto user = baseManager.selectById(id);
        return SysUserDto.isNotAdmin(user.getUserType());
    }

    /**
     * 用户数据脱敏
     *
     * @param user 用户对象
     */
    @Override
    public void userDesensitized(SysUserDto user) {
        user.setPhone(DesensitizedUtil.mobilePhone(user.getPhone()));
        user.setEmail(DesensitizedUtil.email(user.getEmail()));
        user.setPassword(StrUtil.EMPTY);
    }
}
