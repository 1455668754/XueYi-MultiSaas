package com.xueyi.auth.form;

import com.alibaba.fastjson2.JSONObject;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.tenant.api.tenant.domain.dto.TeTenantDto;

/**
 * 用户注册对象
 *
 * @author xueyi
 */
public class RegisterBody {

    /** 租户信息 */
    private TeTenantDto tenant;

    /** 部门信息 */
    private SysDeptDto dept;

    /** 岗位信息 */
    private SysPostDto post;

    /** 用户信息 */
    private SysUserDto user;

    public TeTenantDto getTenant() {
        return tenant;
    }

    public void setTenant(TeTenantDto tenant) {
        this.tenant = tenant;
    }

    public SysDeptDto getDept() {
        return dept;
    }

    public void setDept(SysDeptDto dept) {
        this.dept = dept;
    }

    public SysPostDto getPost() {
        return post;
    }

    public void setPost(SysPostDto post) {
        this.post = post;
    }

    public SysUserDto getUser() {
        return user;
    }

    public void setUser(SysUserDto user) {
        this.user = user;
    }

    /** 构造租户注册对象 */
    public JSONObject buildJson(){
        JSONObject json = new JSONObject();
        json.put("tenant", getTenant());
        json.put("dept", getDept());
        json.put("post", getPost());
        json.put("user", getUser());
        return json;
    }
}
