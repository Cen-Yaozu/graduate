package com.example.serve.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.serve.pojo.Student;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-12-29
 */
@Mapper
public interface StudentMapper extends BaseMapper<Student> {

}
