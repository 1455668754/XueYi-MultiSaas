package com.xueyi.common.core.web.feign;

import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.web.result.R;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 远程查询服务层
 *
 * @param <D> Dto
 * @author xueyi
 */
public interface RemoteSelectService<D extends BaseEntity> {

    /**
     * 根据Id查询数据信息
     *
     * @param id Id
     * @return 数据信息
     */
    @GetMapping(value = "/id", headers = SecurityConstants.FROM_SOURCE_INNER)
    R<D> selectByIdInner(Serializable id);

    /**
     * 根据Ids查询数据信息集合
     *
     * @param ids Ids
     * @return 数据信息集合
     */
    @GetMapping(value = "/ids", headers = SecurityConstants.FROM_SOURCE_INNER)
    R<List<D>> selectListByIdsInner(Collection<? extends Serializable> ids);
}
