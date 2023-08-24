package com.xueyi.system.organize.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.ConvertUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.security.utils.SecurityUserUtils;
import com.xueyi.common.web.entity.manager.impl.BaseManagerImpl;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.system.api.organize.domain.po.SysUserPo;
import com.xueyi.system.api.organize.domain.query.SysUserQuery;
import com.xueyi.system.authority.domain.model.SysRoleConverter;
import com.xueyi.system.authority.mapper.SysRoleMapper;
import com.xueyi.system.organize.domain.merge.SysOrganizeRoleMerge;
import com.xueyi.system.organize.domain.merge.SysUserPostMerge;
import com.xueyi.system.organize.domain.model.SysDeptConverter;
import com.xueyi.system.organize.domain.model.SysPostConverter;
import com.xueyi.system.organize.domain.model.SysUserConverter;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 系统服务 | 组织模块 | 用户管理 数据封装层处理
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
        SysUserDto userDto = mapperDto(baseMapper.selectOne(
                Wrappers.<SysUserPo>query().lambda()
                        .eq(SysUserPo::getUserName, userName)));
        // check password is true
        if (ObjectUtil.isNull(userDto) || !SecurityUserUtils.matchesPassword(password, userDto.getPassword())) {
            return null;
        }

        // select posts in user && select depts in post
        List<SysUserPostMerge> userPostMerges = userPostMergeMapper.selectList(
                Wrappers.<SysUserPostMerge>query().lambda()
                        .eq(SysUserPostMerge::getUserId, userDto.getId()));

        List<Long> postIds = userPostMerges.stream().map(SysUserPostMerge::getPostId).collect(Collectors.toList());
        Set<Long> roleDeptIds = new HashSet<>();
        // if exist posts, must exist depts
        if (CollUtil.isNotEmpty(userPostMerges)) {
            userDto.setPosts(postConverter.mapperDto(postMapper.selectBatchIds(postIds)));
            if (CollUtil.isNotEmpty(userDto.getPosts())) {
                Set<Long> deptIds = userDto.getPosts().stream().map(SysPostDto::getDeptId).collect(Collectors.toSet());
                List<SysDeptDto> depts = deptConverter.mapperDto(deptMapper.selectBatchIds(deptIds));
                depts.forEach(item -> {
                    if (StrUtil.isNotBlank(item.getAncestors())) {
                        roleDeptIds.add(item.getId());
                        List<String> deptIdStrList = StrUtil.splitTrim(item.getAncestors(), StrUtil.COMMA);
                        roleDeptIds.addAll(deptIdStrList.stream().map(ConvertUtil::toLong).collect(Collectors.toSet()));
                    }
                });
                Map<Long, SysDeptDto> deptMap = depts.stream().collect(Collectors.toMap(SysDeptDto::getId, Function.identity()));
                userDto.getPosts().forEach(post -> post.setDept(deptMap.get(post.getDeptId())));
            }
        }
        // 是否为超管用户 ? 无角色集合 : 获取角色集合
        if (userDto.isAdmin()) {
            userDto.setRoles(new ArrayList<>());
        } else {
            // select roles in user
            List<SysOrganizeRoleMerge> organizeRoleMerges = organizeRoleMergeMapper.selectList(
                    Wrappers.<SysOrganizeRoleMerge>query().lambda()
                            .eq(SysOrganizeRoleMerge::getUserId, userDto.getId())
                            .func(i -> {
                                if (CollUtil.isNotEmpty(postIds)) {
                                    i.or().in(SysOrganizeRoleMerge::getPostId, postIds);
                                }
                            })
                            .func(i -> {
                                if (CollUtil.isNotEmpty(roleDeptIds)) {
                                    i.or().in(SysOrganizeRoleMerge::getDeptId, roleDeptIds);
                                }
                            }));
            userDto.setRoles(CollUtil.isNotEmpty(organizeRoleMerges)
                    ? roleConverter.mapperDto(roleMapper.selectBatchIds(organizeRoleMerges.stream().map(SysOrganizeRoleMerge::getRoleId).collect(Collectors.toList())))
                    : new ArrayList<>());
        }
        return userDto;
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
        return baseMapper.update(null,
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
        return baseMapper.update(null,
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
        return baseMapper.update(null,
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
        return baseMapper.update(null,
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
        return baseMapper.update(null,
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
        return baseMapper.update(null,
                Wrappers.<SysUserPo>update().lambda()
                        .set(SysUserPo::getPassword, password)
                        .eq(SysUserPo::getId, id));
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
        return mapperDto(baseMapper.selectOne(
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
        return mapperDto(baseMapper.selectOne(
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
        return mapperDto(baseMapper.selectOne(
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
        return mapperDto(baseMapper.selectOne(
                Wrappers.<SysUserPo>query().lambda()
                        .ne(SysUserPo::getId, id)
                        .eq(SysUserPo::getEmail, email)
                        .last(SqlConstants.LIMIT_ONE)));
    }

    /**
     * 查询条件构造 | 列表查询
     *
     * @param query 数据查询对象
     * @return 条件构造器
     */
    @Override
    protected LambdaQueryWrapper<SysUserPo> selectListQuery(SysUserQuery query) {
        return Wrappers.<SysUserPo>query(query).lambda()
                .func(i -> {
                    if (ObjectUtil.isNotNull(query.getPostId()))
                        i.apply("id in ( select user_id from sys_user_post_merge where post_id = {0} )", query.getPostId());
                })
                .func(i -> {
                    if (ObjectUtil.isNotNull(query.getDeptId())) {
                        i.apply("id in ( select upm.user_id from sys_user_post_merge upm " +
                                "left join sys_post p on upm.post_id = p.id where p.dept_id = {0} )", query.getDeptId());
                    }
                });
    }
}
