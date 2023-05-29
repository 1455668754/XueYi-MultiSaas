package com.xueyi.system.organize.domain.merge;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.annotation.Correlation;
import com.xueyi.common.core.annotation.Correlations;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.web.tenant.base.TBasisEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.xueyi.system.api.organize.domain.merge.MergeGroup.POST_SysUserPostMerge_GROUP;
import static com.xueyi.system.api.organize.domain.merge.MergeGroup.USER_SysUserPostMerge_GROUP;

/**
 * 系统服务 | 组织模块 | 用户-岗位关联 持久化对象
 *
 * @author xueyi
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user_post_merge")
public class SysUserPostMerge extends TBasisEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 用户Id */
    @Correlations({
            @Correlation(groupName = USER_SysUserPostMerge_GROUP, keyType = OperateConstants.SubKeyType.MERGE_MAIN),
            @Correlation(groupName = POST_SysUserPostMerge_GROUP, keyType = OperateConstants.SubKeyType.MERGE_SLAVE)
    })
    private Long userId;

    /** 岗位Id */
    @Correlations({
            @Correlation(groupName = USER_SysUserPostMerge_GROUP, keyType = OperateConstants.SubKeyType.MERGE_SLAVE),
            @Correlation(groupName = POST_SysUserPostMerge_GROUP, keyType = OperateConstants.SubKeyType.MERGE_MAIN)
    })
    private Long postId;

    public SysUserPostMerge(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
    }

}
