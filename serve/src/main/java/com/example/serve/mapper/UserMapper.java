package com.example.serve.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.serve.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author author
 * @since 2025-02-15
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select a.authority from t_authority a,t_user_authority ua,user u where a.id=ua.authority_id and ua.user_id=u.id and u.id=#{user_id}")
    List<String> getAuthoritiesByUserId(Integer userId);
    
    /**
     * 根据权限名称获取权限ID
     * @param authorityName 权限名称
     * @return 权限ID
     */
    @Select("SELECT id FROM t_authority WHERE authority = #{authorityName}")
    Integer getAuthorityIdByName(@Param("authorityName") String authorityName);
    
    /**
     * 为用户分配权限
     * @param userId 用户ID
     * @param authorityId 权限ID
     * @return 影响行数
     */
    @Insert("INSERT INTO t_user_authority (user_id, authority_id) VALUES (#{userId}, #{authorityId})")
    int insertUserAuthority(@Param("userId") Integer userId, @Param("authorityId") Integer authorityId);
}
