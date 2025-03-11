package com.example.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.serve.pojo.User;

import java.util.List;

public interface LoginService extends IService<User> {
    String login(User user);

    User findByUsername(String username);

    List<String> getAuthoritiesByUserId(Integer userId);
    
    /**
     * 修改用户密码
     * @param username 用户名/学号
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否修改成功
     */
    boolean updatePassword(String username, String oldPassword, String newPassword);
}
