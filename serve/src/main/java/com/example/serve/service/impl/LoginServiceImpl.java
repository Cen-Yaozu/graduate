package com.example.serve.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.serve.pojo.LoginUser;
import com.example.serve.pojo.User;
import com.example.serve.mapper.UserMapper;
import com.example.serve.service.LoginService;
import com.example.serve.tools.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class LoginServiceImpl extends ServiceImpl<UserMapper, User> implements LoginService {

    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserMapper userMapper;

    @Override
    public String login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user.getStudentNumber(), user.getPassword());
        try {
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);
            if (Objects.isNull(authenticate)) {
                throw new RuntimeException("登录失败");
            }
            LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
            String userJson = JSON.toJSONString(loginUser);
            String jwt = JwtUtil.createJWT(userJson);
            System.out.println(jwt + "00000");
            // stringRedisTemplate.opsForValue().set("login:"+loginUser.getUser().getId(),
            // userJson);
            return jwt;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    public User findByUsername(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getStudentNumber, username);
        User user = userMapper.selectOne(queryWrapper);
        return user;
    }

    @Override
    public List<String> getAuthoritiesByUserId(Integer userId) {
        return userMapper.getAuthoritiesByUserId(userId);
    }
    
    @Override
    public boolean updatePassword(String username, String oldPassword, String newPassword) {
        try {
            // 查询用户
            User user = findByUsername(username);
            if (user == null) {
                System.out.println("用户不存在: " + username);
                return false;
            }
            
            // 验证旧密码 - 处理{noop}前缀
            String dbPassword = user.getPassword();
            if (dbPassword.startsWith("{noop}")) {
                String actualPassword = dbPassword.substring(6); // 去掉{noop}前缀
                if (!oldPassword.equals(actualPassword)) {
                    System.out.println("原密码不正确");
                    return false;
                }
            } else {
                // 如果密码没有{noop}前缀，则直接比较
                if (!oldPassword.equals(dbPassword)) {
                    System.out.println("原密码不正确");
                    return false;
                }
            }
            
            // 更新密码 - 添加{noop}前缀
            String newStoredPassword = "{noop}" + newPassword;
            user.setPassword(newStoredPassword);
            
            // 保存更新
            int result = userMapper.updateById(user);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
