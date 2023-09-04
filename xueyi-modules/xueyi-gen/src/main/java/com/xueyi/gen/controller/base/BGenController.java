package com.xueyi.gen.controller.base;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.gen.domain.dto.GenTableDto;
import com.xueyi.gen.domain.query.GenTableQuery;
import com.xueyi.gen.service.IGenTableColumnService;
import com.xueyi.gen.service.IGenTableService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * 代码生成管理 业务处理
 *
 * @author xueyi
 */
public class BGenController extends BaseController<GenTableQuery, GenTableDto, IGenTableService> {

    @Autowired
    protected IGenTableColumnService subService;

    /**
     * 定义节点名称
     */
    @Override
    protected String getNodeName() {
        return "业务表";
    }

    /**
     * 生成zip文件
     */
    protected void genCode(HttpServletResponse response, byte[] data) throws IOException {
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"xueyi.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }

    /**
     * 前置校验 增加/修改
     */
    @Override
    protected void AEHandle(BaseConstants.Operate operate, GenTableDto table) {
        if (operate.isEdit())
            baseService.validateEdit(table);
    }
}
