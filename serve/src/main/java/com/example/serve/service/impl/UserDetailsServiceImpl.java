package com.example.serve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.serve.mapper.UserMapper;
import com.example.serve.pojo.LoginUser;
import com.example.serve.pojo.User;
import com.example.serve.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author wu
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户信息
        // 查询数据库
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("studentNumber", username);
        User user = userMapper.selectOne(queryWrapper);

        // 判断密码是否正确
        if(Objects.isNull(user)){
            throw new RuntimeException("用户名不存在");
        }


        //权限操作
        List<String> list=userMapper.getAuthoritiesByUserId(user.getId());
//         返回UserDetails对象
        return new LoginUser(user,list);

    }
}
