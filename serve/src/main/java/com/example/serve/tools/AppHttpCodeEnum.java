package com.example.serve.tools;

/**
 * @author wu
 */

public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200,"操作成功"),
    ERROR(404,"操作失败"),
    // 登录
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    SYSTEM_ERROR(500,"出现错误"),
    USERNAME_EXIST(501,"用户名已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    LOGIN_ERROR(505,"用户名或密码错误"),
    USERNAME_NOT_NULL(508, "用户名不能为空"),
    PASSWORD_NOT_NULL(510, "密码不能为空"),
    NICKNAME_EXIST(512, "昵称已存在"),
    DATA_DELETE_ERROR(409,"数据删除失败"),
    FILE_TYPE_ERROR(507,"文件类型错误"),
    FILE_UPLOAD_ERROR(508,"文件上传失败"),
    CONTENT_REPEAT_HIGH(509,"题目重复率过高");


    private int code;
    private String msg;

    AppHttpCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
}
