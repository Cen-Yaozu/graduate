package com.example.serve.common;

import lombok.Data;

/**
 * 通用返回结果类
 */
@Data
public class Result {
    private Integer code; // 状态码
    private String message; // 提示信息
    private Object data; // 数据

    public Result() {
    }

    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功返回结果
     */
    public static Result success() {
        return new Result(200, "操作成功");
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static Result success(Object data) {
        return new Result(200, "操作成功", data);
    }

    /**
     * 成功返回结果
     *
     * @param message 提示信息
     * @param data 获取的数据
     */
    public static Result success(String message, Object data) {
        return new Result(200, message, data);
    }

    /**
     * 失败返回结果
     *
     * @param message 提示信息
     */
    public static Result error(String message) {
        return new Result(500, message);
    }

    /**
     * 失败返回结果
     *
     * @param code 错误码
     * @param message 提示信息
     */
    public static Result error(Integer code, String message) {
        return new Result(code, message);
    }
} 