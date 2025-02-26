package com.example.serve.command;

import com.example.serve.tools.ResponseResult;

/**
 * 学生信息命令接口 - 命令模式的命令接口
 */
public interface StudentInfoCommand<T> {
    
    /**
     * 执行命令
     */
    ResponseResult<T> execute();
} 