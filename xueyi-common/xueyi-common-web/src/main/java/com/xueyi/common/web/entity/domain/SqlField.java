package com.xueyi.common.web.entity.domain;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.core.exception.UtilException;
import com.xueyi.common.core.utils.core.ArrayUtil;
import com.xueyi.common.core.utils.core.NumberUtil;
import com.xueyi.common.core.web.entity.base.BasisEntity;
import com.xueyi.common.web.utils.SqlHandleUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SqlField implements Serializable {

    /** SQL - 操作类型 */
    private SqlConstants.OperateType operateType;

    /** SQL - 操作键名 */
    private String fieldStr;

    /** SQL - 操作值 */
    private Object object;

    /** SQL - 操作集合 */
    private Collection<Object> coll;

    /** 联动操作控制 */
    private SlaveRelation.LinkageOperate linkageOperate;

    public SqlField(SlaveRelation.LinkageOperate... linkageOperate) {
        initOperate(linkageOperate);
    }

    public SqlField(SqlConstants.OperateType operateType, SFunction<? extends BasisEntity, Object> fieldFun, Object object, SlaveRelation.LinkageOperate... linkageOperate) {
        this.operateType = operateType;
        this.fieldStr = SqlHandleUtil.getFieldName(fieldFun);
        this.object = object;
        initOperate(linkageOperate);
    }

    public SqlField(SqlConstants.OperateType operateType, SFunction<? extends BasisEntity, Object> fieldFun, Serializable serial, SlaveRelation.LinkageOperate... linkageOperate) {
        this.operateType = operateType;
        this.fieldStr = SqlHandleUtil.getFieldName(fieldFun);
        this.object = serial;
        initOperate(linkageOperate);
    }

    public SqlField(SqlConstants.OperateType operateType, String fieldStr, Object object, SlaveRelation.LinkageOperate... linkageOperate) {
        this.operateType = operateType;
        this.fieldStr = fieldStr;
        this.object = object;
        initOperate(linkageOperate);
    }

    public SqlField(SqlConstants.OperateType operateType, SFunction<? extends BasisEntity, Object> fieldFun, Collection<Object> coll, SlaveRelation.LinkageOperate... linkageOperate) {
        this.operateType = operateType;
        this.fieldStr = SqlHandleUtil.getFieldName(fieldFun);
        this.coll = coll;
        initOperate(linkageOperate);
    }

    public SqlField(SqlConstants.OperateType operateType, String fieldStr, Collection<Object> coll, SlaveRelation.LinkageOperate... linkageOperate) {
        this.operateType = operateType;
        this.fieldStr = fieldStr;
        this.coll = coll;
        initOperate(linkageOperate);
    }

    public void setField(SFunction<? extends BasisEntity, Object> fieldFun) {
        this.fieldStr = SqlHandleUtil.getFieldName(fieldFun);
    }

    /**
     * 初始化操作
     *
     * @param linkageOperate 联动操作控制
     */
    private void initOperate(SlaveRelation.LinkageOperate... linkageOperate) {
        if (ArrayUtil.isEmpty(linkageOperate))
            this.linkageOperate = new SlaveRelation.LinkageOperate(Boolean.FALSE);
        else if (linkageOperate.length > NumberUtil.One)
            throw new UtilException("linkageOperate at most one is allowed!");
        else
            this.linkageOperate = linkageOperate[NumberUtil.Zero];
    }
}
