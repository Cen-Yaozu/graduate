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
}
