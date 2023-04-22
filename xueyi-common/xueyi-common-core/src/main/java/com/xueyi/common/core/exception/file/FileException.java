package com.xueyi.common.core.exception.file;

import com.xueyi.common.core.exception.base.BaseException;

import java.io.Serial;

/**
 * 文件信息异常类
 *
 * @author xueyi
 */
public class FileException extends BaseException {
    @Serial
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args, String msg) {
        super("file", code, args, msg);
    }
}
