package com.xueyi.common.web.entity.domain;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.core.web.entity.base.BasisEntity;
import com.xueyi.common.web.utils.SqlUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;

/**
 * 动态SQL控制对象
 *
 * @author xueyi
 */
@Data
@NoArgsConstructor
public class SqlField implements Serializable {

    /** SQL - 操作类型 */
    private SqlConstants.OperateType operateType;

    /** SQL - 操作键名 */
    private String fieldStr;

    /** SQL - 操作值 */
    private Object object;

    /** SQL - 操作集合 */
    private Collection<Object> coll;

    public SqlField(SqlConstants.OperateType operateType, SFunction<? extends BasisEntity, Object> fieldFun, Object object) {
        this.operateType = operateType;
        this.fieldStr = SqlUtil.getFieldName(fieldFun);
        this.object = object;
    }

    public SqlField(SqlConstants.OperateType operateType, String fieldStr, Object object) {
        this.operateType = operateType;
        this.fieldStr = fieldStr;
        this.object = object;
    }

    public SqlField(SqlConstants.OperateType operateType, SFunction<? extends BasisEntity, Object> fieldFun, Collection<Object> coll) {
        this.operateType = operateType;
        this.fieldStr = SqlUtil.getFieldName(fieldFun);
        this.coll = coll;
    }

    public SqlField(SqlConstants.OperateType operateType, String fieldStr, Collection<Object> coll) {
        this.operateType = operateType;
        this.fieldStr = fieldStr;
        this.coll = coll;
    }


}
