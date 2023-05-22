package com.xueyi.common.web.correlate.domain;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.entity.base.BasisEntity;
import com.xueyi.common.web.correlate.utils.CorrelateUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 动态SQL控制对象
 *
 * @author xueyi
 */
@Data
public final class SqlField implements Serializable {

    /** SQL - 操作类型 */
    private SqlConstants.OperateType operateType;

    /** SQL - 操作键名 */
    private String fieldStr;

    /** SQL - 操作值 */
    private Object object;

    /** SQL - 操作集合 */
    private Collection<Object> coll;

    public SqlField(SqlConstants.OperateType operateType) {
        this.operateType = operateType;
    }

    public <T extends BasisEntity> SqlField(SqlConstants.OperateType operateType, SFunction<T, Object> fieldFun, Object object) {
        this.operateType = operateType;
        this.fieldStr = CorrelateUtil.getFieldSqlName(fieldFun);
        this.object = object;
        initData();
    }

    public <T extends BasisEntity> SqlField(SqlConstants.OperateType operateType, SFunction<T, Object> fieldFun, Serializable serial) {
        this.operateType = operateType;
        this.fieldStr = CorrelateUtil.getFieldSqlName(fieldFun);
        this.object = serial;
        initData();
    }


    public <T extends BasisEntity> SqlField(SqlConstants.OperateType operateType, SFunction<T, Object> fieldFun, Collection<Object> coll) {
        this.operateType = operateType;
        this.fieldStr = CorrelateUtil.getFieldSqlName(fieldFun);
        this.coll = coll;
        initData();
    }

    public SqlField(SqlConstants.OperateType operateType, String fieldStr, Object object) {
        this.operateType = operateType;
        this.fieldStr = fieldStr;
        this.object = object;
        initData();
    }

    public SqlField(SqlConstants.OperateType operateType, String fieldStr, Collection<Object> coll) {
        this.operateType = operateType;
        this.fieldStr = fieldStr;
        this.coll = coll;
        initData();
    }

    public <T extends BasisEntity> void setField(SFunction<T, Object> fieldFun) {
        this.fieldStr = CorrelateUtil.getFieldSqlName(fieldFun);
    }

    public void setObject(Object object) {
        this.object = object;
        initData();
    }

    public void setColl(Collection<Object> coll) {
        this.coll = coll;
        initData();
    }

    private List<? extends BaseCorrelate> initData() {
        if (ObjectUtil.isNotNull(this.object)) {
            if (this.object instanceof String str) {
                this.object = StrUtil.SINGLE_QUOTES + str + StrUtil.SINGLE_QUOTES;
            }
        }
        if (CollUtil.isNotEmpty(this.coll)) {
            this.coll = this.coll.stream().filter(ObjectUtil::isNotNull).map(obj -> {
                if (obj instanceof String str) {
                    return StrUtil.SINGLE_QUOTES + str + StrUtil.SINGLE_QUOTES;
                }
                return obj;
            }).collect(Collectors.toSet());
        }
        return null;
    }
}
