package com.xueyi.system.organize.domain.merge;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.web.tenant.base.TBasisEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 用户-岗位关联 持久化对象
 *
 * @author xueyi
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user_post_merge")
public class SysUserPostMerge extends TBasisEntity {

    private static final long serialVersionUID = 1L;

    /** 用户Id */
    private Long userId;

    /** 岗位Id */
    private Long postId;

    public SysUserPostMerge(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
    }

}
