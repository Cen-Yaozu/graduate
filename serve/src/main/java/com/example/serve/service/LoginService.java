package com.example.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.serve.pojo.User;


import java.util.List;


public interface LoginService extends IService<User> {
    String login(User user);
    User findByUsername(String username);
    List<String> getAuthoritiesByUserId(Integer userId);
}
