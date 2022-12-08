package com.xueyi.common.web.entity.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.core.utils.core.ArrayUtil;
import com.xueyi.common.core.utils.core.NumberUtil;
import com.xueyi.common.core.web.entity.base.BasisEntity;
import com.xueyi.common.web.annotation.AutoInject;
import com.xueyi.common.web.entity.domain.SqlField;
import com.xueyi.common.web.utils.SqlUtil;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 数据层 基类通用数据处理
 *
 * @param <P> Po
 * @author xueyi
 */
public interface BasicMapper<P extends BasisEntity> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<P> {

    /**
     * 自定义批量插入
     */
    @AutoInject
    int insertBatch(@Param("collection") Collection<P> list);

    /**
     * 自定义批量更新，条件为主键
     */
    @AutoInject(key = false, isInsert = false)
    int updateBatch(@Param("collection") Collection<P> list);

    /**
     * 根据动态SQL控制对象查询数据对象集合
     *
     * @param field 动态SQL控制对象
     * @return 数据对象集合
     */
    default List<P> selectListByField(SqlField... field) {
        if (ArrayUtil.isNotEmpty(field))
            return selectList(
                    Wrappers.<P>query().lambda()
                            .func(i -> SqlUtil.fieldCondition(i, field)));
        return new ArrayList<>();
    }

    /**
     * 根据动态SQL控制对象查询数据对象
     *
     * @param field 动态SQL控制对象
     * @return 数据对象
     */
    default P selectByField(SqlField... field) {
        if (ArrayUtil.isNotEmpty(field))
            return selectOne(
                    Wrappers.<P>query().lambda()
                            .func(i -> SqlUtil.fieldCondition(i, field))
                            .last(SqlConstants.LIMIT_ONE));
        return null;
    }

    /**
     * 根据动态SQL控制对象更新数据对象
     *
     * @param field 动态SQL控制对象
     * @return 结果
     */
    default int updateByField(SqlField... field) {
        if (ArrayUtil.isNotEmpty(field))
            return update(null,
                    Wrappers.<P>update().lambda()
                            .func(i -> SqlUtil.fieldCondition(i, field)));
        return NumberUtil.Zero;
    }

    /**
     * 根据动态SQL控制对象删除数据对象
     *
     * @param field 动态SQL控制对象
     * @return 结果
     */
    default int deleteByField(SqlField... field) {
        if (ArrayUtil.isNotEmpty(field))
            return delete(Wrappers.<P>update().lambda()
                    .func(i -> SqlUtil.fieldCondition(i, field)));
        return NumberUtil.Zero;
    }
}
