package com.example.serve.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.serve.pojo.StudentDorm;
import org.apache.ibatis.annotations.Mapper;

/**
 * 学生宿舍信息数据访问接口
 *
 * @author author
 * @since 2025-02-23
 */
@Mapper
public interface StudentDormMapper extends BaseMapper<StudentDorm> {
}