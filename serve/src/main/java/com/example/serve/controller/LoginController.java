package com.example.serve.controller;

import com.example.serve.pojo.User;
import com.example.serve.service.LoginService;
import com.example.serve.tools.AppHttpCodeEnum;
import com.example.serve.tools.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user) {
        System.out.println("1111111");
        String jwt = loginService.login(user);
        if (StringUtils.hasLength(jwt)) {
            Map<String, String> result = new HashMap<>(1);
            User u = loginService.findByUsername(user.getStudentNumber());
            System.out.println(u);
            List<String> authorities = loginService.getAuthoritiesByUserId(u.getId());
            // 检查是否为游客权限（authority_id = 0）
            if (authorities == null || authorities.isEmpty()) {
                result.put("auth", "ROLE_GUEST");
                result.put("studentNumber", u.getStudentNumber());
                result.put("token", jwt);
                return ResponseResult.okResult(result);
            }
            String authority = authorities.get(0);
            // 如果是游客权限，直接返回
            if ("ROLE_GUEST".equals(authority)) {
                result.put("auth", authority);
                result.put("studentNumber", u.getStudentNumber());
                result.put("token", jwt);
                return ResponseResult.okResult(result);
            }
            result.put("auth", authority);
            result.put("studentNumber", u.getStudentNumber());
            result.put("token", jwt);
            return ResponseResult.okResult(result);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR);
    }
    
    /**
     * 修改密码
     * @param passwordInfo 包含旧密码和新密码的信息
     * @return 操作结果
     */
    @PostMapping("/updatePassword")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseResult updatePassword(@RequestBody Map<String, String> passwordInfo) {
        try {
            // 获取请求中的参数
            String oldPassword = passwordInfo.get("oldPassword");
            String newPassword = passwordInfo.get("newPassword");
            String username = passwordInfo.get("username"); // 当前登录用户名/学号
            
            // 参数校验
            if (!StringUtils.hasLength(oldPassword) || !StringUtils.hasLength(newPassword) || !StringUtils.hasLength(username)) {
                return ResponseResult.errorResult(AppHttpCodeEnum.ERROR, "参数不完整");
            }
            
            // 调用服务层方法修改密码
            boolean result = loginService.updatePassword(username, oldPassword, newPassword);
            
            if (result) {
                return ResponseResult.okResult("密码修改成功");
            } else {
                return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, "原密码不正确或修改失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, "密码修改失败: " + e.getMessage());
        }
    }
}
