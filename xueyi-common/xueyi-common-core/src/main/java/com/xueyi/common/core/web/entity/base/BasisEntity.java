package com.xueyi.common.core.web.entity.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.web.validate.V_E;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

/**
 * Basis 基类
 *
 * @author xueyi
 */
@Data
public class BasisEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Id */
    @TableId
    @NotNull(message = "id不能为空", groups = {V_E.class})
    protected Long id;

    /** 数据源名称 */
    @JsonIgnore
    @TableField(exist = false)
    protected String sourceName;

    /** 操作类型 */
    @JsonIgnore
    @TableField(exist = false)
    protected BaseConstants.Operate operate;

    /** 请求参数 */
    @JsonIgnore
    @TableField(exist = false)
    protected Map<String, Object> params;

    public String getIdStr() {
        return ObjectUtil.isNotNull(id) ? id.toString() : null;
    }
}
