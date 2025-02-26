package com.example.serve.mapper;

import com.example.serve.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author author
 * @since 2025-02-15
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select a.authority from t_authority a,t_user_authority ua,user u where a.id=ua.authority_id and ua.user_id=u.id and u.id=#{user_id}")
    List<String> getAuthoritiesByUserId(Integer userId);
}
