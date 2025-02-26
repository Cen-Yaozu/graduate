package com.example.serve.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author wu
 */
@Data
@NoArgsConstructor
@ToString
public class LoginUser implements UserDetails {

    //权限列表
    private List<String> list;
    private User user;

    public LoginUser(User user, List<String> list) {
        this.user = user;
        this.list=list;
    }

    //自定义权限列表集合
    @JSONField(serialize = false)
    List<SimpleGrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //父类
        if (authorities!=null){
            return authorities;
        }
        authorities=new ArrayList<>();
        for (String item : list) {
            SimpleGrantedAuthority authority=new SimpleGrantedAuthority(item);
            authorities.add(authority);
        }
        for (SimpleGrantedAuthority authority:authorities){
            System.out.println("权限列表"+authority);
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getStudentNumber();
    }

    /**
     * 账户是否未过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
