package com.xueyi.system.organize.manager.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.core.utils.core.ArrayUtil;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.web.entity.manager.impl.BaseManagerImpl;
import com.xueyi.system.api.authority.domain.model.SysRoleConverter;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.system.api.organize.domain.model.SysDeptConverter;
import com.xueyi.system.api.organize.domain.model.SysPostConverter;
import com.xueyi.system.api.organize.domain.model.SysUserConverter;
import com.xueyi.system.api.organize.domain.po.SysUserPo;
import com.xueyi.system.api.organize.domain.query.SysUserQuery;
import com.xueyi.system.authority.mapper.SysRoleMapper;
import com.xueyi.system.organize.domain.merge.SysOrganizeRoleMerge;
import com.xueyi.system.organize.domain.merge.SysUserPostMerge;
import com.xueyi.system.organize.manager.ISysUserManager;
import com.xueyi.system.organize.mapper.SysDeptMapper;
import com.xueyi.system.organize.mapper.SysPostMapper;
import com.xueyi.system.organize.mapper.SysUserMapper;
import com.xueyi.system.organize.mapper.merge.SysOrganizeRoleMergeMapper;
import com.xueyi.system.organize.mapper.merge.SysUserPostMergeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户管理 数据封装层处理
 *
 * @author xueyi
 */
@Component
public class SysUserManagerImpl extends BaseManagerImpl<SysUserQuery, SysUserDto, SysUserPo, SysUserMapper, SysUserConverter> implements ISysUserManager {

    @Autowired
    SysUserPostMergeMapper userPostMergeMapper;

    @Autowired
    SysOrganizeRoleMergeMapper organizeRoleMergeMapper;

    @Autowired
    SysPostMapper postMapper;

    @Autowired
    SysPostConverter postConverter;

    @Autowired
    SysDeptMapper deptMapper;

    @Autowired
    SysDeptConverter deptConverter;

    @Autowired
    SysRoleMapper roleMapper;

    @Autowired
    SysRoleConverter roleConverter;

    /**
     * 用户登录校验 | 查询用户信息
     *
     * @param userName 用户账号
     * @param password 用户密码
     * @return 用户对象
     */
    @Override
    public SysUserDto userLogin(String userName, String password) {
        SysUserDto userDto = baseConverter.mapperDto(baseMapper.selectOne(
                Wrappers.<SysUserPo>query().lambda()
                        .eq(SysUserPo::getUserName, userName)));
        // check password is true
        if (ObjectUtil.isNull(userDto) || !SecurityUtils.matchesPassword(password, userDto.getPassword()))
            return null;

        // select posts in user && select depts in post
        List<SysUserPostMerge> userPostMerges = userPostMergeMapper.selectList(
                Wrappers.<SysUserPostMerge>query().lambda()
                        .eq(SysUserPostMerge::getUserId, userDto.getId()));

        List<Long> postIds = userPostMerges.stream().map(SysUserPostMerge::getPostId).collect(Collectors.toList());
        List<Long> deptIds = null;
        // if exist posts, must exist depts
        if (CollUtil.isNotEmpty(userPostMerges)) {
            userDto.setPosts(postConverter.mapperDto(postMapper.selectBatchIds(postIds)));
            if (CollUtil.isNotEmpty(userDto.getPosts())) {
                deptIds = userDto.getPosts().stream().map(SysPostDto::getDeptId).collect(Collectors.toList());
                List<SysDeptDto> depts = deptConverter.mapperDto(deptMapper.selectBatchIds(deptIds));
                for (SysDeptDto deptDto : depts) {
                    for (int i = 0; i < userDto.getPosts().size(); i++) {
                        if (ObjectUtil.equal(userDto.getPosts().get(i).getDeptId(), deptDto.getId())) {
                            userDto.getPosts().get(i).setDept(deptDto);
                            break;
                        }
                    }
                }
            }
        }
        // 是否为超管用户 ? 无角色集合 : 获取角色集合
        if (userDto.isAdmin()) {
            userDto.setRoles(new ArrayList<>());
        } else {
            // select roles in user
            List<Long> finalDeptIds = deptIds;
            List<SysOrganizeRoleMerge> organizeRoleMerges = organizeRoleMergeMapper.selectList(
                    Wrappers.<SysOrganizeRoleMerge>query().lambda()
                            .eq(SysOrganizeRoleMerge::getUserId, userDto.getId())
                            .func(i -> {
                                if (CollUtil.isNotEmpty(postIds))
                                    i.or().in(SysOrganizeRoleMerge::getPostId, postIds);
                            })
                            .func(i -> {
                                if (CollUtil.isNotEmpty(finalDeptIds))
                                    i.or().in(SysOrganizeRoleMerge::getDeptId, finalDeptIds);
                            }));
            userDto.setRoles(CollUtil.isNotEmpty(organizeRoleMerges)
                    ? roleConverter.mapperDto(roleMapper.selectBatchIds(organizeRoleMerges.stream().map(SysOrganizeRoleMerge::getRoleId).collect(Collectors.toList())))
                    : new ArrayList<>());
        }
        return userDto;
    }

    /**
     * 根据Id查询单条用户对象 | 附加数据
     *
     * @param id Id
     * @return 用户对象
     */
    @Override
    public SysUserDto selectById(Serializable id) {
        SysUserDto user = baseConverter.mapperDto(baseMapper.selectById(id));
        if (ObjectUtil.isNotNull(user)) {
            List<SysUserPostMerge> userPostMerges = userPostMergeMapper.selectList(
                    Wrappers.<SysUserPostMerge>query().lambda()
                            .eq(SysUserPostMerge::getUserId, user.getId()));
            user.setPostIds(userPostMerges.stream().map(SysUserPostMerge::getPostId).toArray(Long[]::new));
        }
        return user;
    }

    /**
     * 新增用户对象
     *
     * @param user 用户对象
     * @return 结果
     */
    @Override
    @DSTransactional
    public int insert(SysUserDto user) {
        int row = baseMapper.insert(user);
        if (row > 0) {
            if (ArrayUtil.isNotEmpty(user.getPostIds())) {
                userPostMergeMapper.insertBatch(
                        Arrays.stream(user.getPostIds())
                                .map(postId -> new SysUserPostMerge(user.getId(), postId))
                                .collect(Collectors.toSet()));
            }
        }
        return row;
    }

    /**
     * 修改用户对象
     *
     * @param user 用户对象
     * @return 结果
     */
    @Override
    @DSTransactional
    public int update(SysUserDto user) {
        int row = baseMapper.updateById(user);
        if (row > 0) {
            // 查询原关联，判断前后是否变更 ？ 新增/移除变更 : 不操作
            List<SysUserPostMerge> userPostMerges = userPostMergeMapper.selectList(
                    Wrappers.<SysUserPostMerge>query().lambda()
                            .eq(SysUserPostMerge::getUserId, user.getId()));
            List<Long> delPostIds = userPostMerges.stream().map(SysUserPostMerge::getPostId).collect(Collectors.toList());

            if (ArrayUtil.isNotEmpty(user.getPostIds())) {
                List<Long> postIds = Arrays.asList(user.getPostIds());
                List<Long> addPostIds = CollUtil.subtractToList(postIds, delPostIds);
                delPostIds.removeAll(postIds);
                if (CollUtil.isNotEmpty(addPostIds)) {
                    userPostMergeMapper.insertBatch(
                            addPostIds.stream()
                                    .map(postId -> new SysUserPostMerge(user.getId(), postId))
                                    .collect(Collectors.toSet()));
                }
            }
            if (CollUtil.isNotEmpty(delPostIds)) {
                userPostMergeMapper.delete(
                        Wrappers.<SysUserPostMerge>update().lambda()
                                .in(SysUserPostMerge::getPostId, delPostIds)
                                .eq(SysUserPostMerge::getUserId, user.getId()));
            }
        }
        return row;
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
        return baseMapper.update(new SysUserDto(),
                Wrappers.<SysUserPo>update().lambda()
                        .set(SysUserPo::getNickName, nickName)
                        .set(SysUserPo::getSex, sex)
                        .set(SysUserPo::getProfile, profile)
                        .eq(SysUserPo::getId, id));
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
        return baseMapper.update(new SysUserDto(),
                Wrappers.<SysUserPo>update().lambda()
                        .set(SysUserPo::getUserName, userName)
                        .eq(SysUserPo::getId, id));
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
        return baseMapper.update(new SysUserDto(),
                Wrappers.<SysUserPo>update().lambda()
                        .set(SysUserPo::getEmail, email)
                        .eq(SysUserPo::getId, id));
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
        return baseMapper.update(new SysUserDto(),
                Wrappers.<SysUserPo>update().lambda()
                        .set(SysUserPo::getPhone, phone)
                        .eq(SysUserPo::getId, id));
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
        return baseMapper.update(new SysUserDto(),
                Wrappers.<SysUserPo>update().lambda()
                        .set(SysUserPo::getAvatar, avatar)
                        .eq(SysUserPo::getId, id));
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
        return baseMapper.update(new SysUserDto(),
                Wrappers.<SysUserPo>update().lambda()
                        .set(SysUserPo::getPassword, password)
                        .eq(SysUserPo::getId, id));
    }

    /**
     * 根据Id删除用户对象
     *
     * @param id Id
     * @return 结果
     */
    @Override
    @DSTransactional
    public int deleteById(Serializable id) {
        int row = baseMapper.deleteById(id);
        if (row > 0) {
            organizeRoleMergeMapper.delete(
                    Wrappers.<SysOrganizeRoleMerge>update().lambda()
                            .eq(SysOrganizeRoleMerge::getUserId, id));
            userPostMergeMapper.delete(
                    Wrappers.<SysUserPostMerge>update().lambda()
                            .eq(SysUserPostMerge::getUserId, id));
        }
        return row;
    }

    /**
     * 根据Id集合批量删除用户对象
     *
     * @param idList Id集合
     * @return 结果
     */
    @Override
    @DSTransactional
    public int deleteByIds(Collection<? extends Serializable> idList) {
        int rows = baseMapper.deleteBatchIds(idList);
        if (rows > 0) {
            organizeRoleMergeMapper.delete(
                    Wrappers.<SysOrganizeRoleMerge>update().lambda()
                            .in(SysOrganizeRoleMerge::getUserId, idList));
            userPostMergeMapper.delete(
                    Wrappers.<SysUserPostMerge>update().lambda()
                            .in(SysUserPostMerge::getUserId, idList));
        }
        return rows;
    }

    /**
     * 校验用户编码是否唯一
     *
     * @param id   用户Id
     * @param code 用户编码
     * @return 用户对象
     */
    @Override
    public SysUserDto checkUserCodeUnique(Long id, String code) {
        return baseConverter.mapperDto(baseMapper.selectOne(
                Wrappers.<SysUserPo>query().lambda()
                        .ne(SysUserPo::getId, id)
                        .eq(SysUserPo::getCode, code)
                        .last(SqlConstants.LIMIT_ONE)));
    }

    /**
     * 校验用户账号是否唯一
     *
     * @param id       Id
     * @param userName 用户账号
     * @return 结果 | true/false 唯一/不唯一
     */
    @Override
    public SysUserDto checkUserNameUnique(Serializable id, String userName) {
        return baseConverter.mapperDto(baseMapper.selectOne(
                Wrappers.<SysUserPo>query().lambda()
                        .ne(SysUserPo::getId, id)
                        .eq(SysUserPo::getUserName, userName)
                        .last(SqlConstants.LIMIT_ONE)));
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param id       用户Id
     * @param userName 用户名称
     * @return 用户对象
     */
    @Override
    public SysUserDto checkNameUnique(Serializable id, String userName) {
        return baseConverter.mapperDto(baseMapper.selectOne(
                Wrappers.<SysUserPo>query().lambda()
                        .ne(SysUserPo::getId, id)
                        .eq(SysUserPo::getUserName, userName)
                        .last(SqlConstants.LIMIT_ONE)));
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param id    用户Id
     * @param phone 手机号码
     * @return 用户对象
     */
    @Override
    public SysUserDto checkPhoneUnique(Long id, String phone) {
        return baseConverter.mapperDto(baseMapper.selectOne(
                Wrappers.<SysUserPo>query().lambda()
                        .ne(SysUserPo::getId, id)
                        .eq(SysUserPo::getPhone, phone)
                        .last(SqlConstants.LIMIT_ONE)));
    }

    /**
     * 校验email是否唯一
     *
     * @param id    用户Id
     * @param email email
     * @return 用户对象
     */
    @Override
    public SysUserDto checkEmailUnique(Long id, String email) {
        return baseConverter.mapperDto(baseMapper.selectOne(
                Wrappers.<SysUserPo>query().lambda()
                        .ne(SysUserPo::getId, id)
                        .eq(SysUserPo::getEmail, email)
                        .last(SqlConstants.LIMIT_ONE)));
    }
}
