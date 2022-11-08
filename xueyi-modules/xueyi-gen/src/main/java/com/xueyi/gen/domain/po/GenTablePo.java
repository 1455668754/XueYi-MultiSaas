package com.xueyi.gen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.web.entity.base.SubBaseEntity;
import com.xueyi.gen.domain.dto.GenTableColumnDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;

/**
 * 业务 持久化对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "gen_table", excludeProperty = {"status", "sort", "delFlag"})
public class GenTablePo extends SubBaseEntity<GenTableColumnDto> {

    private static final long serialVersionUID = 1L;

    /** 表描述 */
    @NotBlank(message = "表描述不能为空")
    @TableField(condition = LIKE)
    protected String comment;

    /** 实体类名称(首字母大写) */
    @NotBlank(message = "实体类名称不能为空")
    protected String className;

    /** 实体类名称前缀(首字母大写) */
    protected String prefix;

    /** 使用的模板（base单表操作 tree树表操作 subBase主子单表操作 subTree主子树表操作） */
    protected String tplCategory;

    /** 生成包路径 */
    @NotBlank(message = "生成包路径不能为空")
    protected String packageName;

    /** 生成模块路径 */
    @NotBlank(message = "生成模块路径不能为空")
    protected String moduleName;

    /** 生成业务名 */
    @NotBlank(message = "生成业务名不能为空")
    protected String businessName;

    /** 生成权限名 */
    @NotBlank(message = "生成权限名不能为空")
    protected String authorityName;

    /** 生成功能名 */
    @NotBlank(message = "生成功能名不能为空")
    protected String functionName;

    /** 生成作者 */
    @NotBlank(message = "作者不能为空")
    protected String functionAuthor;

    /** 生成代码方式（0zip压缩包 1自定义路径） */
    protected String genType;

    /** 后端生成路径（不填默认项目路径） */
    protected String genPath;

    /** 前端生成路径（不填默认项目路径） */
    protected String uiPath;

    /** 其它生成选项 */
    protected String options;

}