package com.xueyi.common.core.web.entity.base;

import com.baomidou.mybatisplus.annotation.OrderBy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.web.validate.V_E;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * Basis 基类
 *
 * @author xueyi
 */
@Data
public class BasisEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Id */
    @TableId
    @OrderBy
    @NotNull(message = "id不能为空", groups = {V_E.class})
    protected Long id;

    /** 显示顺序 */
    @TableField(exist = false)
    protected Integer sort;

    /** 数据源名称 */
    @JsonIgnore
    @TableField(exist = false)
    protected String sourceName;

    /** 操作类型 */
    @JsonIgnore
    @TableField(exist = false)
    protected BaseConstants.Operate operate;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    protected Map<String, Object> params;

    @JsonIgnore
    public String getIdStr() {
        return ObjectUtil.isNotNull(id) ? id.toString() : null;
    }

    @JsonIgnore
    @SuppressWarnings("all")
    public void initId() {
        id = null;
    }

    /**
     * 初始化操作类型
     *
     * @param operate 操作类型
     */
    @JsonIgnore
    public void initOperate(BaseConstants.Operate operate) {
        if (ObjectUtil.isNull(this.operate))
            this.operate = operate;
    }
}
