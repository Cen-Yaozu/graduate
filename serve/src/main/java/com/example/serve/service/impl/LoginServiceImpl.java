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
}
