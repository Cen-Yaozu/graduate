package com.example.serve.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.serve.pojo.Student;
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
 * @since 2024-12-29
 */
@Mapper
public interface StudentMapper extends BaseMapper<Student> {

    @Select({
            "<script>",
            "SELECT * FROM student",
            "WHERE studentNumber IN",
            "<foreach collection='studentNumbers' item='studentNumber' open='(' separator=',' close=')'>",
            "#{studentNumber}",
            "</foreach>",
            "</script>"
    })
    List<Student> selectStudentsByStudentNumbers(@Param("studentNumbers") List<String> studentNumbers);
}
