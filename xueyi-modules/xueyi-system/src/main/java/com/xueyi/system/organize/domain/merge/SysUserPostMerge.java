package com.xueyi.system.organize.domain.merge;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.web.tenant.TBasisEntity;

/**
 * 用户-岗位关联 持久化对象
 *
 * @author xueyi
 */
@TableName("sys_user_post_merge")
public class SysUserPostMerge extends TBasisEntity {

    private static final long serialVersionUID = 1L;

    /** 用户Id */
    @TableField("user_id")
    private Long userId;

    /** 岗位Id */
    @TableField("post_id")
    private Long postId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
}
